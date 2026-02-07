package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;

@Configurable
public class Shooter implements IAmBetterSubsystem {

    public static final Shooter INSTANCE = new Shooter();

    public ControlSystem controller;
    ServoGroup hoodServos;
    public ServoEx hood;
    public MotorGroup shooters;
    ServoEx stopper;

    // ------------------------ CONFIG ------------------------ //
    public static double shootingSpeed = 0.15;
    public static double shootPower = 1950;
    public static double shootPowerLess = 1750;
    public static double stopperDownPos = 0.65;
    public static double stopperUpPos = 1;
    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kS = 0.0;
    public static double kV = 0.00035;
    public static double kA = 1;

    public boolean isFar = false;

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
        stopper = RobotConfig.Kicker.getServo();

        controller = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {
        shooters.setPower(controller.calculate(shooters.getState()));
        if (!isFar) { // No, you cannot make this smaller, Jack. The logic must behave this way or it breaks.
            if (Turret.INSTANCE.isFar) {
                isFar = true;
                setGoal(Shooter.shootPower);
            }
        } else if (!Turret.INSTANCE.isFar) {
            isFar = false;
            setGoal(Shooter.shootPowerLess);
        }
    }


    // ---------- COMMANDS ---------------------- //
    public Command spinUp() {
        return new InstantCommand(() -> setGoal((Turret.INSTANCE.isFar ? Shooter.shootPower : Shooter.shootPowerLess)).schedule());
    }
    public Command spinUpLess() {
        return new InstantCommand(() -> setGoal(-shootPowerLess).schedule());
    }
    public Command spinDown() {
        return setGoal(0);
    }
    public Command idle() {
        return setGoal(-750);
    }

    public Command setGoal(double vel) {
        return new InstantCommand(() -> this.controller.setGoal(new KineticState(0.0, -vel)));
    }

    public Command resetPID() {
        return new InstantCommand(() -> this.controller = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build());
    }
    public Command StopperDown() {
        return new SetPosition(stopper, stopperDownPos);
    }
    public Command StopperUp() {
        return new SetPosition(stopper, stopperUpPos);
    }
    public Command setHoodPos(double pos) {
        return new SetPosition(hood, pos);
    }
}
