package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.Swerve.MaybeSwerveDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.Swerve.SwerveDrive;

@TeleOp(name = "Maybe: Swerve Drive")
public class Maybe_SwerveDrive extends NextFTCOpMode {

    MaybeSwerveDrive swerveDrive;
    RobotConfig robotConfig;

    public Maybe_SwerveDrive() {
        super();
    }

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        swerveDrive = new MaybeSwerveDrive(robotConfig);
        swerveDrive.init();
    }

    @Override
    public void onWaitForStart() {
        swerveDrive.initLoop();
    }

    @Override
    public void onUpdate() {
        swerveDrive.update(robotConfig.playerOne.ForwardAxis.getValue(), robotConfig.playerOne.StrafeAxis.getValue(), robotConfig.playerOne.TurnAxis.getValue());
    }
}