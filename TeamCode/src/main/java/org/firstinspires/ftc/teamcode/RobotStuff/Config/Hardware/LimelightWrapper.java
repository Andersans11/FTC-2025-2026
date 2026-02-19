package org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware;

import androidx.annotation.Nullable;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

public class LimelightWrapper {

    int pollRate = 100;
    Limelight3A limelight;

    public static final LimelightWrapper instance = new LimelightWrapper();

    public void init() {
        limelight = RobotConfig.Limelight;
    }


    public void start() {
        limelight.pipelineSwitch(0); // apriltag pipeline
        limelight.setPollRateHz(pollRate);
        limelight.start();
    }

    public void stop() {
        limelight.stop();
    }

    public void setYaw(double yaw) {
        limelight.updateRobotOrientation(yaw);
    }

    public @Nullable Pose getPose() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            Pose3D mt2Pose = result.getBotpose_MT2();
            if (mt2Pose != null) {
                return new Pose(mt2Pose.getPosition().x, mt2Pose.getPosition().y, mt2Pose.getOrientation().getYaw());
            }
        }
        return null;
    }

}
