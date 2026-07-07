package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.math.MathFunctions;

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

    public static double powerMod;

    // ------------------------ CONFIG ------------------------ //
    public static double shootPower = 1950;
    public static double shootPowerLess = 1750;
    public static double stopperDownPos = 0.4;
    public static double stopperUpPos = 0.25;
    public static double kP = 0.05;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kS = 0.0;
    public static double kV = 0.001;
    public static double kA = 0.0;

    public boolean isFar = false;

    // --------------------- OPMODE -------------------------- //
    @Override
    public void initSystem() {
        shooters = new MotorGroup(
                RobotConfig.ShootMotor2.getMotor(),
                RobotConfig.ShootMotor1.getMotor()
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
        shooters.setPower(-Math.max(controller.calculate(shooters.getLeader().getState()), 0));
    }

    double[][] table = {
            {30.6, 600, 62.5},
            {47.5, 650, 57.5},
            {65.2, 750, 57.5},
            {83.2, 800, 50},
            {101.5, 925, 55},
    };

    public double calcShooterPower(double dist) {
        if (dist <= table[0][0]) return table[0][1];

        for (int i = 0; i < table.length - 1; i++) {

            double d1 = table[i][0];
            double t1 = table[i][1];
            double d2 = table[i+1][0];
            double t2 = table[i+1][1];

            if (dist <= d2) {
                return t1 + (dist - d1) * (t2 - t1) / (d2 - d1) + powerMod;
            }
        }

        return table[table.length - 1][1];
    }

    public double calcHoodPower(double dist) {
        if (dist <= table[0][0]) return table[0][2];

        for (int i = 0; i < table.length - 1; i++) {

            double d1 = table[i][0];
            double t1 = table[i][2];
            double d2 = table[i+1][0];
            double t2 = table[i+1][2];

            if (dist <= d2) {
                return t1 + (dist - d1) * (t2 - t1) / (d2 - d1);
            }
        }

        return table[table.length - 1][2];
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
        return new InstantCommand(() -> stopper.setPosition(stopperDownPos));
    }
    public Command StopperOpen() {
        if (stopper.getPosition() == stopperUpPos) return new NullCommand();
        return new InstantCommand(() -> stopper.setPosition(stopperUpPos));
    }
    public Command setHoodPos(double pos) {
        if (hood.getPosition() != pos) return new SetPosition(hood, MathFunctions.clamp(pos, Turret.INSTANCE.angleToServoPower(40), 1));
        return new NullCommand();
    }
}
