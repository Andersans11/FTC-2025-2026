package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.RobotCentricDrive;

@TeleOp(name = "Basic Bitch OpMode", group = "aa it go")
public class BasicBitchOpMode extends NextFTCOpMode {
    public BasicBitchOpMode() {super();}
    RobotConfig robotConfig = new RobotConfig(this);
    RobotCentricDrive robotCentricDrive = new RobotCentricDrive(this, robotConfig);
    @Override public void onStartButtonPressed() {robotCentricDrive.Start();}
    @Override public void onUpdate() {robotConfig.gamepadUpdates();}
}