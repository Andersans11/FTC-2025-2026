package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

@Config
public class Shooter implements IAmBetterSubsystem {

    public static final Shooter INSTANCE = new Shooter();

    MotorEx[] shooterMotors;
    MotorGroup shooters;
    ServoEx hood;
    ServoEx kicker;

    // ------------------------ CONFIG ------------------------ //
    public static double shootingSpeed = 0.75;

    // --------------------- OPMODE -------------------------- //
    @Override
    public void initSystem() {
        shooterMotors = new MotorEx[] {
                RobotConfig.ShootMotor1.motor,
                RobotConfig.ShootMotor2.motor
        };
        shooters = new MotorGroup(
                shooterMotors[0],
                shooterMotors[1]
        );
        hood = RobotConfig.HoodServo.servo;
        kicker = RobotConfig.Kicker.servo;
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
        return new SetPower(shooters, 0.6);
    }
    public Command kick() {
        return new SetPosition(kicker, 0.2);
    }
    public Command resetKicker() {
        return new SetPosition(kicker, 1);
    }

    public Command shoot() {
        return new SequentialGroup(
                this.spinUp(),
                this.kick(),
                new Delay(shootingSpeed),
                this.resetKicker()
        );
    }

    public Command setHoodPos(double pos) {
        return new SetPosition(hood, pos);
    }
}
