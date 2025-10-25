package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import java.util.function.Supplier;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

@Config
public class NewTurret implements IAmBetterSubsystem {

    public static final NewTurret INSTANCE = new NewTurret();

    MotorEx rotationMotor;
    boolean isRedAlliance;
    double pitch;
    Pose pose;
    Pose oldPose;
    public double targetAngle;
    boolean isManualControl = true;
    ControlSystem controller;

    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 1.0;
    public static double kI = 0.0;
    public static double kD = 0.0;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .angular(AngleType.DEGREES,
                        feedback -> feedback.posPid(kP, kI, kD)
                )
                .build();
    }

    @Override
    public void initSystem() {
        this.rotationMotor = RobotConfig.TurretRotation.motor;
    }

    @Override
    public void preStart() {

    }

    @Override
    public void periodic() {
        if (!isManualControl) {
            if (pose != oldPose) {
                if (!isRedAlliance) {
                    targetAngle = Math.tan((144 - pose.getY()) / pose.getX()); // get angle with right triangle rules
                    targetAngle = 180 - targetAngle; // get supplement of angle
                    targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                    if (targetAngle < 0) {
                        targetAngle = targetAngle + 360; // normalize angle
                    }
                    pitch = (pose.distanceFrom(new Pose(144, 0)) / 144) - 1.25;
                } else {
                    targetAngle = Math.tan((144 - pose.getY()) / (144 - pose.getX())); // get angle with right triangle rules
                    targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                    if (targetAngle < 0) {
                        targetAngle = targetAngle + 360; // normalize angle
                    }
                    pitch = (pose.distanceFrom(new Pose(144, 144)) / 144) - 1.25;
                }
                // TODO: Add actual runtoposition code
            }
        }
    }

    public Command setPower(double power) {
        return new SetPower(rotationMotor, power);
    }

}
