package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.powerable.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class Turret implements Subsystem {

    NextFTCOpMode opMode;
    RobotConfig config;
    Servo pitchServo;
    MotorGroup shootMotors;
    MotorEx rotationMotor;

    public void init(RobotConfig config, NextFTCOpMode opMode) {
        this.config = config;
        this.opMode = opMode;

        this.pitchServo = config.TurretPitch.servo;
        this.rotationMotor = config.TurretRotation.motor;

        this.shootMotors = new MotorGroup(config.ShootMotor1.motor, config.ShootMotor2.motor);
    }

    public Command shoot() {
        return new SetPower(
                shootMotors,
                1
        );
    }

    public Command stop() {
        return new SetPower(
                shootMotors,
                0
        );
    }

}
