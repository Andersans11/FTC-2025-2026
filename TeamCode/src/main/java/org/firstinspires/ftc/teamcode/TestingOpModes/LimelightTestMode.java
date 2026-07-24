package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.LimelightWrapper;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

import java.util.Arrays;

//@Disabled
@TeleOp(name = "Test Limelight", group = Utils.TESTING)
public class LimelightTestMode extends RRoboticsOpMode {

    public LimelightTestMode() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricDrive.INSTANCE)
        );
    }

    double[] boxes = {
            0, 320,
            320, 640,
            640, 960,
            960, 1280
    };

    @Override
    public void onInit() {
        super.onInit();
        LimelightWrapper.instance.init();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

        LimelightWrapper.instance.start();

        LimelightWrapper.instance.updatePython(boxes);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("results", Arrays.toString(LimelightWrapper.instance.getPython()));
    }

    @Override
    public void onStop() {
        super.onStop();

        LimelightWrapper.instance.stop();
    }
}
