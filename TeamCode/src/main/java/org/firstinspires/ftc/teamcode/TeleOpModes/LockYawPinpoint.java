package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.HoldHeadingPinpoint;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.Stopwatch;

@TeleOp(name = "LockYaw Pinpoint", group = "bb testing") // pinpoint go brrr
@Disabled
public class LockYawPinpoint extends NextFTCOpMode {

    public LockYawPinpoint() {
        super();
    }

    private final ElapsedTime deltaTimer = new ElapsedTime();
    Stopwatch StopWatch = new Stopwatch();

    RobotConfig robotConfig;
    HoldHeadingPinpoint holdHeadingPinpoint;


    double deltaTime;
    IMU imu;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        holdHeadingPinpoint = new HoldHeadingPinpoint(this, robotConfig);

        setUseBulkReading(true);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection. BACKWARD;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));
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
