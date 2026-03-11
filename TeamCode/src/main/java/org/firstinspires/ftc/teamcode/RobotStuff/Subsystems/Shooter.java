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

    public static double powerMod = 0;

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

    double[][] table = {
            {36.4, 1275, 0.0},
            {53.9, 1300, 0.0},
            {64.9, 1325, 0.1},
            {85.6, 1425, 0.1},
            {100.4, 1550, 0.1},
            {115.0, 1700, 0.2},
            {123.5, 1775, 0.25},
            {140.2, 1850, 0.3},
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
