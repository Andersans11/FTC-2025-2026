package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.TESTRobotCentricDrive;

@TeleOp(name = "TEST Basic Bitch OpMode", group = "bb testing")
//@Disabled
public class TESTBasicBitchOpMode extends NextFTCOpMode {
    public TESTBasicBitchOpMode() {super();}
    RobotConfig robotConfig;
    TESTRobotCentricDrive testRobotCentricDrive;
    @Override public void onInit() {
        robotConfig = new RobotConfig(this);
        testRobotCentricDrive = new TESTRobotCentricDrive(this, robotConfig);
    }
    @Override public void onStartButtonPressed() {testRobotCentricDrive.Start();}
    @Override public void onUpdate() {robotConfig.gamepadUpdates();}
}