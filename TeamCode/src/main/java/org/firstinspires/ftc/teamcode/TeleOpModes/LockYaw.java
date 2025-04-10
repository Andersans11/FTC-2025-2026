package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.HoldHeading;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.Stopwatch;

@TeleOp(name = "LockYaw", group = "idfk") // pid go brrr
//@Disabled
public class LockYaw extends NextFTCOpMode {

    private final ElapsedTime frameTimer = new ElapsedTime();

    Stopwatch stopWatch = new Stopwatch();

    RobotConfig activeConfig;
    DriveMotors activeDriveMode;

    double deltaTime;

    IMU imu;

    @Override
    public void onInit() {
        setUseBulkReading(true);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        imu = hardwareMap.get(IMU.class, "imu");

        activeConfig = new RobotConfig(this);

        activeDriveMode = new HoldHeading(this, activeConfig);

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection. RIGHT;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void onStartButtonPressed() {
        frameTimer.reset();

        deltaTime = 0;
    }

    @Override
    public void onUpdate() {
        stopWatch.reset();

        deltaTime = frameTimer.seconds();
        telemetry.addData("deltaTime", deltaTime);
        frameTimer.reset();

        activeDriveMode.updateDrive(deltaTime);
        activeConfig.playerOne.update_all();
        activeConfig.playerTwo.update_all();

        telemetry.update();
    }
}
