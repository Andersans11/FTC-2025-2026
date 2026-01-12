package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.bindings.Range;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;

@Configurable
public class VPIDShooter implements IAmBetterSubsystem {

    public static final VPIDShooter INSTANCE = new VPIDShooter();

    public ControlSystem controller;
    ServoGroup hoodServos;
    ServoEx hood;
    public MotorGroup shooters;
    ServoEx kicker;

    // ------------------------ CONFIG ------------------------ //
    public static double shootingSpeed = 0.1;
    public static double kickerPos1 = 0.15;
    public static double kickerPos0 = 0.825;
    public static double kP = 0.0075;
    public static double kI = 0.0;
    public static double kD = 0.0;

    // --------------------- OPMODE -------------------------- //
    @Override
    public void initSystem() {
        shooters = new MotorGroup(
                RobotConfig.ShootMotor1.getMotor(),
                RobotConfig.ShootMotor2.getMotor()
        );
        hoodServos = new ServoGroup(
                RobotConfig.HoodServo.getServo(),
                RobotConfig.HoodServo2.getServo()
        );
        hood = RobotConfig.HoodServo.getServo();
        kicker = RobotConfig.Kicker.getServo();

        controller = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .build();
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {
        shooters.setPower(-controller.calculate(shooters.getState()));

    }


    // ---------- COMMANDS ---------------------- //
    public Command spinUp() {
        return setGoal(2000);
    }
    public Command spinDown() {
        return setGoal(0);
    }
    public Command idle() {
        return setGoal(750);
    }

    public Command setGoal(double vel) {
        return new InstantCommand(() -> this.controller.setGoal(new KineticState(0.0, vel)));
    }

    public Command resetPID() {
        return new InstantCommand(() -> this.controller = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .build());
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
