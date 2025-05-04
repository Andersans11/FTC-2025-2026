package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Basic Bitch OpMode", group = OpModeGroups.WORKING)
//@Disabled
public class BasicBitchOpMode extends NextFTCOpMode {
    public BasicBitchOpMode() {super();}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;
    @Override public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
    }
    @Override public void onStartButtonPressed() {
        robotCentricDrive.Start();
    }
    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        robotCentricDrive.updateDrive(deltaTimeNano);
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        robotConfig.gamepadUpdates();
    }
}