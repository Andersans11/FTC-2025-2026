package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.HoldHeadingTESTPinpoint;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.Stopwatch;

@TeleOp(name = "LockYaw TEST Pinpoint", group = "bb testing") // pid go brrr
//@Disabled
public class LockYawTESTPinpoint extends NextFTCOpMode {

    public LockYawTESTPinpoint() {
        super();
    }

    private final ElapsedTime deltaTimer = new ElapsedTime();
    Stopwatch StopWatch = new Stopwatch();

    RobotConfig robotConfig;
    HoldHeadingTESTPinpoint holdHeadingTESTPinpoint;

    double deltaTime;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        holdHeadingTESTPinpoint = new HoldHeadingTESTPinpoint(this, robotConfig);

        setUseBulkReading(true);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }
    @Override
    public void onStartButtonPressed() {
        holdHeadingTESTPinpoint.Start();
        deltaTimer.reset();
        deltaTime = 0;
    }

    @Override
    public void onUpdate() {
        StopWatch.reset();

        deltaTime = deltaTimer.seconds();
        telemetry.addData("deltaTime", deltaTime);
        deltaTimer.reset();

        holdHeadingTESTPinpoint.updateDrive(deltaTime);
        robotConfig.playerOne.update_all();
        robotConfig.playerTwo.update_all();

        telemetry.update();
    }
}
