package org.firstinspires.ftc.teamcode.MSDT.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes.Swerve.SwerveDrive;

@TeleOp(name = "Test: Swerve Drive")
public class Test_SwerveDrive extends NextFTCOpMode {

    SwerveDrive drive;
    RobotConfig robotConfig;

    public Test_SwerveDrive() {
        super();
    }

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        drive = new SwerveDrive(robotConfig);
        drive.init();
    }

    @Override
    public void onWaitForStart() {
        drive.initLoop();
    }

    @Override
    public void onUpdate() {
        drive.update(robotConfig.playerOne.ForwardAxis.getValue(), robotConfig.playerOne.StrafeAxis.getValue(), robotConfig.playerOne.TurnAxis.getValue());
    }
}