package org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class PoseResult {

    public PoseResult() {
        this.isValid = false;
    }

    public PoseResult(double x, double y, double yaw) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
        this.isValid = true;
    }

    public static PoseResult fromLL(LLResult result) {
        if (!result.isValid()) {
            return new PoseResult();
        }

        Pose3D pose3D = result.getBotpose_MT2();
        return new PoseResult(pose3D.getPosition().x, pose3D.getPosition().y, pose3D.getOrientation().getYaw());
    }
    private double x;
    private double y;
    private double yaw;
    private final boolean isValid;

    public Pose getPose() {
        return new Pose(x, y, yaw);
    }

    public boolean isValid() {
        return isValid;
    }

    public Pose assignIfThreshold(Pose other, double threshold, boolean trustResult) {
        boolean shouldAssign = trustResult ? other.distanceFrom(getPose()) > threshold : other.distanceFrom(getPose()) < threshold;
        return shouldAssign ? getPose() : other;
    }

}
