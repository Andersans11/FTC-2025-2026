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

    public LockYaw() {super();}

    private final ElapsedTime frameTimer = new ElapsedTime();
    Stopwatch stopWatch = new Stopwatch();

    RobotConfig robotConfig = new RobotConfig(this);
    DriveMotors holdHeading = new HoldHeading(this, robotConfig);


    double deltaTime;
    IMU imu;

    @Override
    public void onInit() {
        setUseBulkReading(true);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection. RIGHT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }
    @Override
    public void onStartButtonPressed() {
        holdHeading.Start();
        frameTimer.reset();
        deltaTime = 0;
    }

    @Override
    public void onUpdate() {
        stopWatch.reset();

        deltaTime = frameTimer.seconds();
        telemetry.addData("deltaTime", deltaTime);
        frameTimer.reset();

        holdHeading.updateDrive(deltaTime);
        robotConfig.playerOne.update_all();
        robotConfig.playerTwo.update_all();

        telemetry.update();
    }
}
