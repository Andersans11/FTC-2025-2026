package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {

    public static final Shooter INSTANCE = new Shooter();

    MotorEx[] shooterMotors;
    MotorGroup shooters;
    RTPAxon hood;
    ServoEx kicker;

    double shootingSpeed = 0.25;

    @Override
    public void initialize() {
        shooterMotors = new MotorEx[] {
                RobotConfig.ShootMotor1.motor,
                RobotConfig.ShootMotor2.motor
        };
        shooters = new MotorGroup(
                shooterMotors[0],
                shooterMotors[1]
        );
        hood = new RTPAxon(RobotConfig.HoodServo.servo, RobotConfig.HoodENC);
        //TODO: Set Hood PID
        kicker = RobotConfig.Kicker.servo;
    }

    public Command setShooterPitch (double angleDegrees) {
        angleDegrees = Math.min(40, Math.max(angleDegrees, 0));
        hood.setTargetRotation(angleDegrees);
        return new NullCommand();
    }

    public Command spinUp() {
        return new SetPower(
                shooters,
                1
        );
    }

    public Command idle() {
        return new SetPower(
                shooters,
                0.5
        );
    }

    public Command spinDown() {
        return new SetPower(
                shooters,
                0
        );
    }

    @Override
    public void periodic() {
        hood.update();
    }

    public Command shoot() {
        return new SequentialGroup(
                spinUp(),
                new SetPosition(kicker, 1),
                new Delay(shootingSpeed),
                new ParallelGroup(
                        idle(),
                        new SetPosition(kicker, 0)
                )
        );
    }
}
