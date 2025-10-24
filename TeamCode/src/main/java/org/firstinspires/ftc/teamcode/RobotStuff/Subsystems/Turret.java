package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.dfrobot.HuskyLens;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.*;

public class Turret implements IBetterSubsystem {

    public static final Turret INSTANCE = new Turret();

    MotorEx rotationMotor;
    boolean isRedAlliance;
    HuskyLens camera;
    ArtifactTypes[] motif;
    HuskyLens.Algorithm trackingMode;
    double targetPos;
    double oldTargetPos;
    double pitch;
    Pose pose;
    Pose oldPose;
    double targetAngle;
    double kP = 1.0;
    double kI = 0.0;
    double kD = 0.0;
    ControlSystem controller;
    boolean isManualControl = false;
    double currentAxisPos;

    final double lensCenterFromFloor = 279.87241113;
    final double lensAngleFromVertical = 20.0; // deg

    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .angular(AngleType.DEGREES,
                        feedback -> feedback.posPid(kP, kI, kD)
                )
                .build();
    }

    @Override
    public void hardware() {
        this.rotationMotor = RobotConfig.TurretRotation.motor;
        this.camera = RobotConfig.camera;
    }

    @Override
    public void commands() {

    }

    public void setTrackingMode(HuskyLens.Algorithm mode) {
        trackingMode = mode;
    }

    public void setRedAlliance() {
        isRedAlliance = true;
    }

    public void setManualControl(boolean manualControl) {
        isManualControl = manualControl;
    }

    public ArtifactTypes[] getMotif() {
        camera.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        if (camera.blocks(2).length != 0)
            motif = new ArtifactTypes[] {
                    ArtifactTypes.GREEN,
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.PURPLE
            };
        else if (camera.blocks(3).length != 0)
            motif = new ArtifactTypes[] {
                ArtifactTypes.PURPLE,
                ArtifactTypes.GREEN,
                ArtifactTypes.PURPLE
            };
        else if (camera.blocks(4).length != 0)
            motif = new ArtifactTypes[] {
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.GREEN
            };
        camera.selectAlgorithm(trackingMode);
        return motif;
    }

    public double getServoPitch() {
        if (isRedAlliance && camera.blocks(1).length != 0) {
            int tagY = camera.blocks(1)[0].height;

            return (double) (tagY/-100) + 1; // if tag is at max height, is 1
        } else if (!isRedAlliance && camera.blocks(2).length != 0) {
            int tagY = camera.blocks(2)[0].height;

            return (double) (tagY/-100) + 1; // if tag is at max height, is 1
        }
        return 0;
    }

    public void passAxis(double value) {
        currentAxisPos = value;
    }

    public Command update() {
        if (!isManualControl) {
            if ((isRedAlliance && camera.blocks(1).length != 0) ||
                    (!isRedAlliance && camera.blocks(2).length != 0)) {
                int tagX = camera.blocks(5)[0].x;

                if (tagX < 158 || tagX > 162) {
                    double power = (double) (160 - tagX) / 80;
                    return new SetPower(rotationMotor, power);
                } else {
                    return new SetPower(rotationMotor, 0);
                }
            } else {
                if (pose != oldPose) {
                    if (!isRedAlliance) {
                        targetAngle = Math.tan((144 - pose.getY()) / pose.getX()); // get angle with right triangle rules
                        targetAngle = 180 - targetAngle; // get supplement of angle
                        targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                        if (targetAngle < 0) {
                            targetAngle = targetAngle + 360; // normalize angle
                        }
                    } else {
                        targetAngle = Math.tan((144 - pose.getY()) / (144 - pose.getX())); // get angle with right triangle rules
                        targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                        if (targetAngle < 0) {
                            targetAngle = targetAngle + 360; // normalize angle
                        }
                    }
                }
            }

            return new NullCommand(); // Put PID Tracking here; this is for when the camera can't see the tag
        } else {
            return new SetPower(rotationMotor, currentAxisPos);
        }

    } // TODO: IMPLEMENT PID TRACKING FOR PITCH AND YAW ON TURRET

    @Override
    public void binds() {

    }

    public Double getTurretPos() {
        return rotationMotor.getCurrentPosition() / 537.7 / 2880;
    }

    public void passRobotPose(Pose pose) {
        this.pose = pose;
    }

    @Override
    public void periodic() {
        update().invoke();
    }
}
