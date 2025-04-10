package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.FieldCentricDrive;

@TeleOp(name = "it go", group = "aaaaaaaaaaaaaaaaaaaaa it go")
public class FieldCentric extends NextFTCOpMode {
    Command vroom;
    DriveMotors motors;
    RobotConfig cfg = new RobotConfig(this);

    @Override
    public void onInit() {
        motors = new FieldCentricDrive(this, cfg);
    }

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void onStartButtonPressed() {
        vroom = new MecanumDriverControlled(motors.driveMotors, gamepadManager.getGamepad1());
        vroom.invoke();
    }

    @Override
    public void onUpdate() {
        gamepadManager.updateGamepads();
    }
}
