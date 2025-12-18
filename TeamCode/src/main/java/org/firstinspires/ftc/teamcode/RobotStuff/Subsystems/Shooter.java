package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

@Configurable
public class Shooter implements IAmBetterSubsystem {

    public static final Shooter INSTANCE = new Shooter();

    MotorEx[] shooterMotors;
    ServoGroup hoodServos;
    ServoEx hood;
    MotorGroup shooters;
    ServoEx kicker;
    Range hoodSupp;

    // ------------------------ CONFIG ------------------------ //
    public static double shootingSpeed = 0.3;
    public static double kickerPos1 = 0.15;
    public static double kickerPos0 = 0.825;

    // --------------------- OPMODE -------------------------- //
    @Override
    public void initSystem() {
        shooterMotors = new MotorEx[] {
                RobotConfig.ShootMotor1.getMotor(),
                RobotConfig.ShootMotor2.getMotor()
        };
        shooters = new MotorGroup(
                shooterMotors[0],
                shooterMotors[1]
        );
        hoodServos = new ServoGroup(
                RobotConfig.HoodServo.getServo(),
                RobotConfig.HoodServo2.getServo()
        );
        hood = RobotConfig.HoodServo.getServo();
        kicker = RobotConfig.Kicker.getServo();
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {}


    // ---------- COMMANDS ---------------------- //
    public Command spinUp() {
        return new SetPower(shooters, -1);
    }
    public Command spinDown() {
        return new SetPower(shooters, 0);
    }
    public Command idle() {
        return new SetPower(shooters, -0.35);
    }
    public Command kick() {
        return new SetPosition(kicker, kickerPos1);
    }
    public Command resetKicker() {
        return new SetPosition(kicker, kickerPos0);
    }

    public Command shoot() {
        return new SequentialGroup(
                this.kick(),
                new SequentialGroup(
                        new Delay(shootingSpeed),
                        this.resetKicker()
                )
        );
    }

    public Command setHoodPos(double pos) {
        return new SetPosition(hood, pos);
    }
}
