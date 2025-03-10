package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.Test_NFTC;
import org.firstinspires.ftc.teamcode.example.java.Lift;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@TeleOp(name = "Test: NFTC TeleOp", group = "ab working opmode")
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(Lift.INSTANCE);
    }

    Command mecanumDrive;

    DriveMotors motors;

    RobotConfig cfg = new RobotConfig(this);

    @Override
    public void onInit() {
        motors = new Test_NFTC(this, cfg);
        Lift.INSTANCE.initialize();
    }

    @Override
    public void onStartButtonPressed() {
        mecanumDrive = new MecanumDriverControlled(motors.driveMotors, gamepadManager.getGamepad1());
        mecanumDrive.invoke();

        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(Lift.INSTANCE::toHigh);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(Lift.INSTANCE::toLow);
    }
}
