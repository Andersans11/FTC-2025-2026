package org.firstinspires.ftc.teamcode.AutonomousOpModes.Old;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.OldTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.pedrojson.Callbacks;

import java.io.IOException;
import java.io.InputStream;

import PedroJSON.main.PathLoader;

@Disabled
@Autonomous(name = "Automous Ultra Pro Max Masters Edition Plus")
public class SuperAuto extends RoyallyFuckedUpMode {
    InputStream FC, TC, MC, BC, GC, PC;
    PathLoader base, top, middle, bottom, gate, park;
    Follower follower;
    Callbacks callbacks;
    boolean doTop = false;
    boolean doMiddle = false;
    boolean doBottom = false;
    boolean doGate = true;
    int outcomeState = 0;
    int outcomeMemory;
    int numberCycles = 1;

    public SuperAuto() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Artemis.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        try {
            FC = hardwareMap.appContext.getAssets().open("FirstCycle.json");
            TC = hardwareMap.appContext.getAssets().open("TopCycle.json");
            MC = hardwareMap.appContext.getAssets().open("MiddleCycle.json");
            BC = hardwareMap.appContext.getAssets().open("BottomCycle.json");
            GC = hardwareMap.appContext.getAssets().open("GateCycle.json");
            PC = hardwareMap.appContext.getAssets().open("ParkCycle.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        follower = Constants.createFollower(hardwareMap);
        callbacks = new Callbacks(this);

        base = new PathLoader(   FC, follower, follower.pathBuilder(), this, callbacks, 1);
        top = new PathLoader(    TC, follower, follower.pathBuilder(), this, callbacks, 1);
        middle = new PathLoader( MC, follower, follower.pathBuilder(), this, callbacks, 1);
        bottom = new PathLoader( BC, follower, follower.pathBuilder(), this, callbacks, 1);
        gate = new PathLoader(   GC, follower, follower.pathBuilder(), this, callbacks, 1);
        park = new PathLoader(   PC, follower, follower.pathBuilder(), this, callbacks, 1);

        P2.dpadUp().whenBecomesTrue(() -> {
            doTop = true;
            numberCycles++;
        });
        P2.dpadRight().whenBecomesTrue(() -> {
            doMiddle = true;
            numberCycles++;
        });
        P2.dpadDown().whenBecomesTrue(() -> {
            doBottom = true;
            numberCycles++;
        });

        P2.dpadLeft().whenBecomesTrue(() -> {
            doTop = false;
            doMiddle = false;
            doBottom = false;
            numberCycles = 1;
        });

        base.Parse();
        top.Parse();
        middle.Parse();
        bottom.Parse();
        gate.Parse();

        OldTurret.INSTANCE.setPosition(90);
    }

    @Override
    public void onWaitForStart() {
        super.onWaitForStart();

        OldTurret.INSTANCE.manualUpdate();

        telemetry.addData("doTop", doTop);
        telemetry.addData("doMiddle", doMiddle);
        telemetry.addData("doBottom", doBottom);
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

        Artemis.INSTANCE.stopIntake().schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (numberCycles == 3 && doGate) {
            outcomeMemory = outcomeState;
            outcomeState = 4;
            doGate = false;
        }

        switch (outcomeState) {
            case 0:
                if (base.isComplete()) {
                    outcomeState = 1;
                    numberCycles--;
                } else base.Update();
                break;
            case 1:
                if (!doTop) outcomeState = 2;
                else if (top.isComplete()) {
                    outcomeState = 2;
                    numberCycles--;
                } else top.Update();
                break;
            case 2:
                if (!doMiddle) outcomeState = 3;
                else if (middle.isComplete()) {
                    outcomeState = 3;
                    numberCycles--;
                } else middle.Update();
                break;
            case 3:
                if (!doBottom) outcomeState = 5;
                else if (bottom.isComplete()) {
                    outcomeState = 5;
                    numberCycles--;
                } else bottom.Update();
                break;
            case 4:
                if (gate.isComplete()) {
                    outcomeState = outcomeMemory;
                } else gate.Update();
                break;
            case 5:
                if (park.isComplete()) {
                    outcomeState = 6;
                } else park.Update();
                break;
            case 6:
                break;
        }
    }
}