package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.FieldCentricDrive;

@TeleOp(name = "it go", group = "aaaaaaaaaaaaaaaaaaaaa it go")
public class FieldCentric extends NextFTCOpMode {

    public FieldCentric() {super();}
    RobotConfig robotConfig = new RobotConfig(this);
    DriveMotors fieldCentricDrive = new FieldCentricDrive(this, robotConfig);

    @Override
    public void onStartButtonPressed() {
        fieldCentricDrive.Start();
    }
    @Override
    public void onUpdate() {
        gamepadManager.updateGamepads();
    }
}
