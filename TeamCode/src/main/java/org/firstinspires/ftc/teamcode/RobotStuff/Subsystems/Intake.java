package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

@Configurable
public class Intake implements IAmBetterSubsystem {

    public static final Intake INSTANCE = new Intake();
    public MotorEx intake;

    // ---------------------------- CONFIG -------------------------- //
    public static double intakeSpeed = 1;
    public static double reverseSpeed = -0.75;
    public static double idleSpeed = 0.35;


    // ------------------------- OPMODE --------------------------- //

    @Override
    public void initSystem() {
        intake = RobotConfig.IntakeMotor.getMotor();
    }

    @Override
    public void preStart() {}

    // ------------------------------ COMMANDS ------------------------- //

    public Command start() {
        return new SetPower(intake, intakeSpeed);
    }
    public Command stop() {
        return new SetPower(intake, 0);
    }
    public Command reverse() {
        return new SetPower(intake, reverseSpeed);
    }
}
