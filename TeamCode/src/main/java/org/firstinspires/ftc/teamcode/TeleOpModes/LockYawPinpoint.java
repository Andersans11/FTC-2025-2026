package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.HoldHeadingPinpoint;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.Stopwatch;

@TeleOp(name = "LockYaw Pinpoint", group = "abb tune") // pid go brrr
//@Disabled
public class LockYawPinpoint extends NextFTCOpMode {

    public LockYawPinpoint() {
        super();
    }

    private final ElapsedTime deltaTimer = new ElapsedTime();
    Stopwatch StopWatch = new Stopwatch();

    RobotConfig robotConfig;
    HoldHeadingPinpoint holdHeadingPinpoint;

    double deltaTime;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        holdHeadingPinpoint = new HoldHeadingPinpoint(this, robotConfig);

        setUseBulkReading(true);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }
    @Override
    public void onStartButtonPressed() {
        holdHeadingPinpoint.Start();
        deltaTimer.reset();
        deltaTime = 0;
    }

    @Override
    public void onUpdate() {
        StopWatch.reset();

        deltaTime = deltaTimer.seconds();
        telemetry.addData("deltaTime", deltaTime);
        deltaTimer.reset();

        holdHeadingPinpoint.updateDrive(deltaTime);
        robotConfig.playerOne.update_all();
        robotConfig.playerTwo.update_all();

        telemetry.update();
    }
}
