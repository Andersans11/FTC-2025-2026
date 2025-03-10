package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveBase;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@TeleOp(name = "Test: NFTC TeleOp")
public class Test_NFTCTeleOp extends NextFTCOpMode {
    Command mecanumDrive;

    DriveBase motors;

    @Override
    public void onInit() {
        motors = new DriveBase(this);
        motors.initDrive();
    }

    @Override
    public void onStartButtonPressed() {
        mecanumDrive = new MecanumDriverControlled(motors.driveMotors, gamepadManager.getGamepad1());
        mecanumDrive.invoke();
    }
}
