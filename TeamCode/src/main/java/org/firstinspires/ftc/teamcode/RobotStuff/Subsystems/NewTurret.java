package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.dfrobot.HuskyLens;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Supplier;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

@Config
public class NewTurret implements IAmBetterSubsystem {

    public static final NewTurret INSTANCE = new NewTurret();

    public MotorEx rotationMotor;
    public HuskyLens camera;
    boolean isRedAlliance = true;
    double pitch;
    Pose pose;
    Pose oldPose;
    public double targetAngle = 0;
    boolean isManualControl = false;
    boolean isSweeping = true;
    boolean started = false;
    Timer timer;
    public ControlSystem controller;
    public double tagPos;
    public enum TurretMode {
        TAG_TRACKING, // Using AprilTags
        RECOVERY, // Either position-based or sweep-based
        MANUAL
    }

    public TurretMode mode;

    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double a = 0.21875;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build();

        timer = new Timer();
    }

    @Override
    public void initSystem() {
        this.rotationMotor = RobotConfig.TurretRotation.motor;
        camera = new HuskyLens(RobotConfig.camera.getDeviceClient());
        camera.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
    }

    @Override
    public void preStart() {

    }

    public Command resetPID() {
        return new InstantCommand(() -> controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build()
        );
    }

    public Command setPosition(double pos) {
        return new InstantCommand(() -> {
            targetAngle = Math.max(-90, Math.min(90, pos));
            controller.setGoal(new KineticState(degreesToTicks(targetAngle)));
        });
    }

    public Command ChangePosition(double pos) {
        return new InstantCommand(() -> {
            targetAngle = targetAngle + pos;
            targetAngle = Math.max(-90, Math.min(90, targetAngle));
            controller.setGoal(new KineticState(degreesToTicks(targetAngle)));
        });
    }

    public double degreesToTicks(double degrees) {
        return degrees * 537.7 / 360 * 8;
    }
    public double ticksToDegrees(double ticks) {return ticks / 537.7 * 360 / 8;}

    public void passPose(Pose pose) {
        this.pose = pose;
    }



    public double waugh() { // yes, this is how we get the tag x position
        try {
            if (isRedAlliance) {
                return camera.blocks(1)[0].x;
            } else {
                return camera.blocks(2)[0].x;
            }
        } catch (RuntimeException reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee) {
            return 69420;
        }
    }


    @Override
    public void periodic() {
        double waluigiWaugh = waugh();
        
        switch (mode) {
            case TAG_TRACKING:
                if (waluigiWaugh != 69420) {
                    targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) - (a * (waluigiWaugh - 160));
                    timer.resetTimer();
                } else if (timer.getElapsedTimeSeconds() >= 0.25) {
                    mode = TurretMode.RECOVERY;
                } else {
                    double deltaHeading = pose.getHeading() - oldPose.getHeading();
                    targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) + deltaHeading;
                }
                controller.setGoal(new KineticState(degreesToTicks(Math.max(-90, Math.min(90, targetAngle)))));
                break;
            case RECOVERY:
                if (isSweeping) {
                    if (waluigiWaugh != 69420) {
                        targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) - (a * (waluigiWaugh - 160));
                        timer.resetTimer();
                        mode = TurretMode.TAG_TRACKING;
                        started = false;
                    } else {
                        if (!started) {
                            controller.setGoal(new KineticState(-90));
                            started = true;
                        } else if (ticksToDegrees(rotationMotor.getCurrentPosition()) >= 89) {
                            controller.setGoal(new KineticState(-90));
                        } else if (ticksToDegrees(rotationMotor.getCurrentPosition()) <= -89) {
                            controller.setGoal(new KineticState(90));
                        }
                    }
                } else {
                    if (pose != oldPose) {
                        if (!isRedAlliance) {
                            targetAngle = Math.toDegrees(Math.atan((144 - pose.getY()) / pose.getX())); // get angle with right triangle rules
                            targetAngle = 180 - targetAngle; // get supplement of angle
                            targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                            if (targetAngle < 0) {
                                targetAngle = targetAngle + 360; // normalize angle
                            }
                            pitch = (pose.distanceFrom(new Pose(144, 0)) / 144) - 1.25;
                        } else {
                            targetAngle = Math.toDegrees(Math.atan((144 - pose.getY()) / (144 - pose.getX()))); // get angle with right triangle rules
                            targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                            if (targetAngle < 0) {
                                targetAngle = targetAngle + 360; // normalize angle
                            }
                            pitch = (pose.distanceFrom(new Pose(144, 144)) / 144) - 1.25;
                        }
                    }
                }
                controller.setGoal(new KineticState(degreesToTicks(Math.max(-90, Math.min(90, targetAngle)))));
        }
        rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
        oldPose = pose;
    }
}
