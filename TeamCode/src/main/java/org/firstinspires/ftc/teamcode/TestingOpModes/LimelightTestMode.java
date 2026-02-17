package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.LimelightWrapper;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

@TeleOp(name = "Test Limelight", group = Utils.TESTING)
public class LimelightTestMode extends RRoboticsOpMode {

    public LimelightTestMode() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricDrive.INSTANCE),
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        Selene.INSTANCE.initFollower(hardwareMap);

        LimelightWrapper.instance.init();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

        LimelightWrapper.instance.start();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        LimelightWrapper.instance.setYaw(Selene.INSTANCE.getCurrentPose().getHeading());

        Pose llPose = LimelightWrapper.instance.getPose();
        if (llPose != null)
            addData("LIMELIGHT POSE", llPose);
        else
            addData("LIMELIGHT POSE", "NONE");


        addData("PINPOINT POSE", Selene.INSTANCE.getCurrentPose());
    }

    @Override
    public void onStop() {
        super.onStop();

        LimelightWrapper.instance.stop();
    }
}
