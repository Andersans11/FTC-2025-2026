package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.pedrojson.Callbacks;

import java.io.File;

import PedroJSON.main.PathLoader;

@Autonomous(name = "Test: PedroJSON Auto with Multiple PathLoaders")
public class TestMultiplePedroJSONAuto extends RoyallyFuckedUpMode {
    PathLoader base, outcome1, outcome2;
    Follower follower;
    Callbacks callbacks;
    boolean doOutcome1;
    boolean doOutcome2;

    int outcomeState = 0;

    @Override
    public void onInit() {
        super.onInit();

        follower = Constants.createFollower(hardwareMap);
        callbacks = new Callbacks(this);

        // TODO: Create Files

        base = new PathLoader(new File("org/firstinspires/ftc/teamcode/RobotStuff/pedrojson/Data/base.json"), follower, this, callbacks);
        outcome1 = new PathLoader(new File("org/firstinspires/ftc/teamcode/RobotStuff/pedrojson/Data/outcome1.json"), follower, this, callbacks);
        outcome2 = new PathLoader(new File("org/firstinspires/ftc/teamcode/RobotStuff/pedrojson/Data/outcome2.json"), follower, this, callbacks);

        base.Parse();
        outcome1.Parse();
        outcome2.Parse();
    }

    @Override
    public void onWaitForStart() {
        if (P1.dpadLeft().get()) doOutcome1 = true;
        if (P1.dpadRight().get()) doOutcome2 = true;
    }

    @Override
    public void onStartButtonPressed() {
        // starting position
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        switch (outcomeState) {
            case 0:
                base.Update();
                if (base.isComplete()) {
                    outcomeState = 1;
                }
                break;
            case 1:
                if (doOutcome1) {
                    outcome1.Update();
                    if (outcome1.isComplete()) {
                        outcomeState = 2;
                    }
                } else outcomeState = 2;
                break;
            case 2:
                if (doOutcome2) {
                    outcome2.Update();
                    if (outcome2.isComplete()) {
                        outcomeState = 3;
                    }
                } else outcomeState = 3;
                break;
            case 3:
                // maybe add a breaking function or smth idk
                break;
        }
    }
}
