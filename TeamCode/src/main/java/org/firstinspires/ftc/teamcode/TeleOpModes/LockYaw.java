package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.HoldHeading;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "LockYaw", group = OpModeGroups.TUNE) // pid go brrr
//@Disabled
public class LockYaw extends NextFTCOpMode {

    public LockYaw() {
        super();
    }

    DeltaTimer deltaTimer = new DeltaTimer();

    RobotConfig robotConfig;
    HoldHeading holdHeading;


    long deltaTimeNano;
    IMU imu;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        holdHeading = new HoldHeading(this, robotConfig);

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
        holdHeading.Start();
    }

    @Override
    public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        holdHeading.updateDrive(deltaTimeNano);

        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        robotConfig.gamepadUpdates();

        telemetry.update();
    }
}
