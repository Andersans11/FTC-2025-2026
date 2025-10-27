package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.dfrobot.HuskyLens;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

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
    boolean isManualControl = true;
    boolean isSearching = true;
    public ControlSystem controller;
    public double tagPos;

    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double cP = 1;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build();
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

    @Override
    public void periodic() {
        if (!isManualControl) {
            if (isRedAlliance && camera.blocks(1).length > 0 && camera.blocks(1) != null) {
                targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) + ((double) camera.blocks(1)[0].x / 10 * cP);
            } else if (!isRedAlliance && camera.blocks(2).length > 0 && camera.blocks(2) != null) {
                targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) + ((double) camera.blocks(2)[0].x / 10 * cP);
            } else {
                if (isSearching) {
                    // searching stuff
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
            }
            controller.setGoal(new KineticState(degreesToTicks(Math.max(-90, Math.min(90, targetAngle)))));
        }
        rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
    }
}
