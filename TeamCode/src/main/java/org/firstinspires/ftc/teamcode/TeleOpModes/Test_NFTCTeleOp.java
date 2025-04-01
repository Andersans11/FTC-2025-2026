package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.NFTCRobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.Test_NFTC;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.LiftBase;


/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@TeleOp(name = "Test: NFTC TeleOp", group = "ab working opmode")
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(LiftBase.INSTANCE);
    }



    Command mecanumDrive;

    DriveMotors motors;

    NFTCRobotConfig cfg = new NFTCRobotConfig(this);

    @Override
    public void onInit() {
        motors = new Test_NFTC(this, cfg);
        LiftBase.INSTANCE.initialize();
    }

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void onStartButtonPressed() {
        mecanumDrive = new MecanumDriverControlled(motors.driveMotors, gamepadManager.getGamepad1());
        mecanumDrive.invoke();

        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(LiftBase.INSTANCE::toHigh);
        gamepadManager.getGamepad1().getDpadLeft().setPressedCommand(LiftBase.INSTANCE::toMiddle);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(LiftBase.INSTANCE::toLow);
        gamepadManager.getGamepad2().getLeftStick().setHeldCommand(LiftBase.INSTANCE::moveLift);
    }

    @Override
    public void onUpdate() {

        telemetry.addData("Lift current pos:", (LiftBase.INSTANCE.ticksToMM(LiftBase.INSTANCE.motor.getCurrentPosition(), 751.8)));

        telemetry.update();

        gamepadManager.updateGamepads();
    }
}
