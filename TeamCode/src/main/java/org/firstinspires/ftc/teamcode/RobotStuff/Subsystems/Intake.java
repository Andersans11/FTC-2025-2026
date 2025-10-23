package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements IBetterSubsystem {

    public static final Intake INSTANCE = new Intake();

    MotorEx intake;

    public Command start;
    public Command stop;
    public Command idle;

    @Override
    public void binds() {
        RobotConfig.ButtonControls.INTAKE.whenTrue(this.start);
        RobotConfig.ButtonControls.INTAKE_STOP.whenTrue(this.stop);
        RobotConfig.ButtonControls.INTAKE.and(() -> !RobotConfig.ButtonControls.INTAKE_STOP.get()).whenTrue(this.idle);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void initialize() {
    }

    @Override
    public void hardware() {
        intake = RobotConfig.IntakeMotor.motor;
    }

    @Override
    public void commands() {
        this.start = new SetPower(intake, 1);
        this.stop = new SetPower(intake, 0);
        this.idle = new SetPower(intake, 0.2);
    }
}
