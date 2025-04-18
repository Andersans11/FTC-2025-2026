package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.VerticalLift;


@TeleOp(name = "Lift Teleop", group = "cc don't fucking use this")
@Disabled
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(VerticalLift.INSTANCE);
    }

    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
        VerticalLift.INSTANCE.initialize();
        VerticalLift.INSTANCE.configure(robotConfig);
    }

    @Override
    public void onStartButtonPressed() {
        robotCentricDrive.Start();

        robotConfig.playerOne.DpadUp.setPressedCommand(VerticalLift.INSTANCE::toHigh);
        robotConfig.playerOne.DpadDown.setPressedCommand(VerticalLift.INSTANCE::toLow);
        robotConfig.playerOne.DpadLeft.setPressedCommand(VerticalLift.INSTANCE::toMiddle);
        robotConfig.playerOne.Y.setPressedCommand(VerticalLift.INSTANCE::resetEncoders);
    }

    @Override
    public void onUpdate() {

        telemetry.addData("Lift current pos:", VerticalLift.INSTANCE.ticksToInches(VerticalLift.INSTANCE.motors.getLeader().getCurrentPosition(), 751.8));

        telemetry.update();

        gamepadManager.updateGamepads();
    }
}
