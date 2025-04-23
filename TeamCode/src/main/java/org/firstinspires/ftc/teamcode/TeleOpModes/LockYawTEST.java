package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.HoldHeadingTEST;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "LockYaw TEST", group = OpModeGroups.TESTING) // pid go brrr
//@Disabled
public class LockYawTEST extends NextFTCOpMode {

    public LockYawTEST() {
        super();
    }

    DeltaTimer deltaTimer = new DeltaTimer();

    RobotConfig robotConfig;
    HoldHeadingTEST holdHeadingTEST;


    long deltaTimeNano;
    IMU imu;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        holdHeadingTEST = new HoldHeadingTEST(this, robotConfig);

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
        holdHeadingTEST.Start();
    }

    @Override
    public void onUpdate() {

        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        holdHeadingTEST.updateDrive(deltaTimeNano);
        robotConfig.gamepadUpdates();

        telemetry.update();
    }
}
