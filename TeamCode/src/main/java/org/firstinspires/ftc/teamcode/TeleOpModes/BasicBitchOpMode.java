package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.Stopwatch;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

@TeleOp(name = "Basic Bitch OpMode", group = OpModeGroups.WORKING)
//@Disabled
public class BasicBitchOpMode extends NextFTCOpMode {
    public BasicBitchOpMode() {super();}

    private final ElapsedTime deltaTimer = new ElapsedTime();
    Stopwatch StopWatch = new Stopwatch();
    double deltaTime;
    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;
    @Override public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
    }
    @Override public void onStartButtonPressed() {
        deltaTime = 0;
        robotCentricDrive.Start();
    }
    @Override public void onUpdate() {
        StopWatch.reset();

        deltaTime = deltaTimer.seconds();
        telemetry.addData("deltaTime", deltaTime);
        deltaTimer.reset();

        robotConfig.gamepadUpdates();
    }
}