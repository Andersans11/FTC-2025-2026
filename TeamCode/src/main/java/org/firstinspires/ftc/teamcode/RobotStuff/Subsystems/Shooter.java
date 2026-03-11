package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.IRRoboticsSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.positionable.SetPosition;

@Configurable
public class Shooter implements IRRoboticsSubsystem {

    public static final Shooter INSTANCE = new Shooter();

    public ControlSystem controller;
    public ServoExFullRange hood;
    public MotorGroup shooters;
    public ServoExFullRange stopper;

    // ------------------------ CONFIG ------------------------ //
    public static double shootPower = 1950;
    public static double shootPowerLess = 1750;
    public static double stopperDownPos = 0.65;
    public static double stopperUpPos = 0.8;
    public static double kP = 0.001;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kS = 0.0;
    public static double kV = 0.00042;
    public static double kA = 0.0;

    public boolean isFar = false;

    // --------------------- OPMODE -------------------------- //
    @Override
    public void initSystem() {
        shooters = new MotorGroup(
                RobotConfig.ShootMotor1.getMotor(),
                RobotConfig.ShootMotor2.getMotor()
        );
        hood = RobotConfig.HoodServo.getServo();
        stopper = RobotConfig.StopperServo.getServo();

        controller = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {
        shooters.setPower(controller.calculate(shooters.getLeader().getState()));
    }

    public double calcShooterPower(double dist) {
        dist = (25.0/3.0) * dist + (2350.0/3.0) + 100;
        return Math.max(1350, Math.min(2000, dist));
    }


    // ---------- COMMANDS ---------------------- //
    public Command spinUp() {
        return new InstantCommand(() -> setGoal(-(Turret.INSTANCE.isFar ? Shooter.shootPower : Shooter.shootPowerLess)).schedule());
    }
    public Command spinUpLess() {
        return new InstantCommand(() -> setGoal(-shootPowerLess).schedule());
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
                .basicFF(kV, kA, kS)
                .build());
    }
    public Command StopperClose() {
        if (stopper.getPosition() == stopperDownPos) return new NullCommand();
        return new SetPosition(stopper, stopperDownPos);
    }
    public Command StopperOpen() {
        if (stopper.getPosition() == stopperUpPos) return new NullCommand();
        return new SetPosition(stopper, stopperUpPos);
    }
    public Command setHoodPos(double pos) {
        if (hood.getPosition() != pos) return new SetPosition(hood, pos);
        return new NullCommand();
    }
}
