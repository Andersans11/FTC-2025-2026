package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.LiftBase;


@TeleOp(name = "NFTC TeleOp", group = "ab working opmode")
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(LiftBase.INSTANCE);
    }



    RobotConfig robotConfig = new RobotConfig(this);
    RobotCentricDrive robotCentricDrive = new RobotCentricDrive(this, robotConfig);

    @Override
    public void onInit() {LiftBase.INSTANCE.initialize();}

    @Override
    public void onStartButtonPressed() {
        robotCentricDrive.Start();

        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(LiftBase.INSTANCE::toHigh);
        gamepadManager.getGamepad1().getDpadLeft().setPressedCommand(LiftBase.INSTANCE::toMiddle);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(LiftBase.INSTANCE::toLow);
        gamepadManager.getGamepad2().getLeftStick().setHeldCommand(LiftBase.INSTANCE::moveLiftBetter);
        gamepadManager.getGamepad2().getX().setPressedCommand(LiftBase.INSTANCE::resetEncoders);

        LiftBase.INSTANCE.setLimits(13, 0.015);
    }

    @Override
    public void onUpdate() {

        telemetry.addData("Lift current pos:", LiftBase.INSTANCE.ticksToInches(LiftBase.INSTANCE.motor.getCurrentPosition(), 751.8));
        telemetry.addData("At limits: ", LiftBase.INSTANCE.overLimits);

        telemetry.update();

        gamepadManager.updateGamepads();
    }
}
