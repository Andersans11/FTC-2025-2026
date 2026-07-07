package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.IRRoboticsSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

@Configurable
public class Intake implements IRRoboticsSubsystem {

    public static final Intake INSTANCE = new Intake();
    public MotorEx intake1;
    public MotorEx intake2;
    public ServoExFullRange winch1;
    public ServoExFullRange winch2;

    // ---------------------------- CONFIG -------------------------- //
    public static double intakeSpeed = 1;
    public static double intakeSpeedSlow = 0.5;
    public static double reverseSpeed = -0.75;

    public static double active1 = 0.475;
    public static double active2 = 0.475;
    public static double off1 = 0.525;
    public static double off2 = 0.525;
    public static double gate1 = 0.45;
    public static double gate2 = 0.45;


    // ------------------------- OPMODE --------------------------- //

    @Override
    public void initSystem() {
        intake1 = RobotConfig.IntakeMotor1.getMotor();
        intake2 = RobotConfig.IntakeMotor2.getMotor();
        winch1 = RobotConfig.Intake1.getServo();
        winch2 = RobotConfig.Intake2.getServo();
    }

    @Override
    public void preStart() {}

    // ------------------------------ COMMANDS ------------------------- //

    public Command start() {
        return new ParallelGroup(
                new SetPower(intake1, intakeSpeed),
                new SetPower(intake2, intakeSpeed)
        );
    }

    public Command startSlow() {
        return new ParallelGroup(
                new SetPower(intake1, intakeSpeedSlow),
                new SetPower(intake2, intakeSpeedSlow)
        );
    }


    public Command active() {
        return new ParallelGroup(
                new SetPosition(winch1, active1),
                new SetPosition(winch2, active2)
        );
    }
    public Command off() {
        return new ParallelGroup(
                new SetPosition(winch1, off1),
                new SetPosition(winch2, off2)
        );
    }
    public Command gate() {
        return new ParallelGroup(
                new SetPosition(winch1, gate1),
                new SetPosition(winch2, gate2),
                stop()
        );
    }
    public Command stop() {
        return new ParallelGroup(
                new SetPower(intake1, 0),
                new SetPower(intake2, 0)
        );
    }
    public Command reverse() {
        return new ParallelGroup(
                new SetPower(intake1, reverseSpeed),
                new SetPower(intake2, reverseSpeed)
        );
    }
}
