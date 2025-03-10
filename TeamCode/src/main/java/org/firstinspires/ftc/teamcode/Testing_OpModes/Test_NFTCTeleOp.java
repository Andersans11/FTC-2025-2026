package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.Config.Motors;
import org.firstinspires.ftc.teamcode.example.java.Lift;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@TeleOp(name = "Test: NFTC TeleOp")
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(Lift.INSTANCE);
    }

    Command mecanumDrive;

    Motors motors;

    @Override
    public void onInit() {
        motors = new Motors(this);
        motors.initDrive();
    }

    @Override
    public void onStartButtonPressed() {
        mecanumDrive = new MecanumDriverControlled(motors.driveMotors, gamepadManager.getGamepad1());
        mecanumDrive.invoke();

        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(Lift.INSTANCE::toHigh);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(Lift.INSTANCE::toLow);
    }
}
