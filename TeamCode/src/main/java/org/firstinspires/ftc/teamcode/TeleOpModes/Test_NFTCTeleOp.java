package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.NFTCRobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.Test_NFTC;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.LiftBase;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.TandemLift;


/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@TeleOp(name = "Test: NFTC TeleOp", group = "ab working opmode")
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(LiftBase.INSTANCE, TandemLift.INSTANCE);
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

        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(LiftBase.INSTANCE::liftUp);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(LiftBase.INSTANCE::liftDown);
        gamepadManager.getGamepad2().getLeftStick().setHeldCommand(LiftBase.INSTANCE::moveLift);

        /*
        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(TandemLift.INSTANCE::liftUp);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(TandemLift.INSTANCE::liftDown);
        gamepadManager.getGamepad2().getLeftStick().setHeldCommand(TandemLift.INSTANCE::moveLift);
         */
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        telemetry.addData("Lift current pos:", LiftBase.INSTANCE.motor.getCurrentPosition());
        telemetry.addData("Last pressed preset:", LiftBase.INSTANCE.liftState);

        telemetry.update();
    }
}
