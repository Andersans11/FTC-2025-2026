package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.dfrobot.HuskyLens;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

import java.util.function.Supplier;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class NewTurret implements Subsystem {

    public static final NewTurret INSTANCE = new NewTurret();

    MotorEx rotationMotor;
    boolean isRedAlliance;
    double pitch;
    Pose pose;
    Pose oldPose;
    public double targetAngle;
    double kP = 1.0;
    double kI = 0.0;
    double kD = 0.0;
    boolean isManualControl = true;

    public Supplier<Command> update;
    final double lensCenterFromFloor = 279.87241113;
    final double lensAngleFromVertical = 20.0; // deg

    @Override
    public void initialize() {
        this.rotationMotor = RobotConfig.TurretRotation.motor;
    }

    public Command setPower(double power) {
        return new SetPower(rotationMotor, power);
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
}
