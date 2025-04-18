package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Reset Encoders", group = "aa utility")
//@Disabled
public class ResetEncoders extends NextFTCOpMode {

    public ResetEncoders() {super();}
    IMU imu;

    @Override
    public void runOpMode() {

        for (DcMotor motor: hardwareMap.dcMotor){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection. RIGHT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        imu.resetYaw();

        telemetry.addLine("encoders reset");
        telemetry.update();
    }

}
