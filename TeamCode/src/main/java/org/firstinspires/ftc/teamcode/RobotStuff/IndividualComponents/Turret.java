package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class Turret extends Subsystem {
    public static final Turret INSTANCE = new Turret();
    private Turret() { } // nftc boilerplate
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
                1,
                this
        );
    }

    public Command stop() {
        return new SetPower(
                shootMotors,
                0,
                this
        );
    }

}
