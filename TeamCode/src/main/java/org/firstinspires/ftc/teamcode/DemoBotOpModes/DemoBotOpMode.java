package org.firstinspires.ftc.teamcode.DemoBotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.DemoRobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotLauncher;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotLifter;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotPivot;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Demo Bot OpMode", group = OpModeGroups.TESTING)
//@Disabled
public class DemoBotOpMode extends NextFTCOpMode {
    public DemoBotOpMode() {super(DemoBotPivot.INSTANCE, DemoBotLauncher.INSTANCE, DemoBotLifter.INSTANCE);}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    DemoRobotConfig demoRobotConfig;
    DemoBotDrive demoBotDrive;

    @Override public void onInit() {
        demoRobotConfig = new DemoRobotConfig(this);
        demoBotDrive = new DemoBotDrive(this, demoRobotConfig);
        DemoBotLifter.INSTANCE.initSystem(demoRobotConfig);
        DemoBotPivot.INSTANCE.initSystem(demoRobotConfig);
        DemoBotLauncher.INSTANCE.initSystem(demoRobotConfig);
    }
    @Override public void onStartButtonPressed() {
        DemoBotLauncher.INSTANCE.map(demoRobotConfig.playerOne.RightTrigger, DemoBotLauncher.Mappings.SHOOT);
        DemoBotLauncher.INSTANCE.map(demoRobotConfig.playerOne.RightBumper, DemoBotLauncher.Mappings.OUTTAKE);

        DemoBotPivot.INSTANCE.map(demoRobotConfig.playerOne.DpadUp, DemoBotPivot.Mappings.UP);
        DemoBotPivot.INSTANCE.map(demoRobotConfig.playerOne.DpadDown, DemoBotPivot.Mappings.DOWN);
        DemoBotPivot.INSTANCE.map(demoRobotConfig.playerOne.DpadLeft, DemoBotPivot.Mappings.NORMAL);

        DemoBotLifter.INSTANCE.map(demoRobotConfig.playerOne.Triangle, DemoBotLifter.Mappings.COLLECT);
        DemoBotLifter.INSTANCE.map(demoRobotConfig.playerOne.Square, DemoBotLifter.Mappings.DEPOSIT);
        DemoBotLifter.INSTANCE.map(demoRobotConfig.playerOne.Circle, DemoBotLifter.Mappings.HOLD);

        demoBotDrive.Start();
    }
    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        demoBotDrive.updateDrive(deltaTimeNano);
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        demoRobotConfig.gamepadUpdates();
    }
}