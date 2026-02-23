package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.IRRoboticsSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

@Configurable
public class Intake implements IRRoboticsSubsystem {

    public static final Intake INSTANCE = new Intake();
    public MotorEx intake;
    public ServoExFullRange winch1;
    public ServoExFullRange winch2;

    // ---------------------------- CONFIG -------------------------- //
    public static double intakeSpeed = 1;
    public static double reverseSpeed = -0.75;

    public static double active1 = 0.35;
    public static double active2 = 0.75;
    public static double off1 = 0.3;
    public static double off2 = 0.85;


    // ------------------------- OPMODE --------------------------- //

    @Override
    public void initSystem() {
        intake = RobotConfig.IntakeMotor.getMotor();
        winch1 = RobotConfig.Intake1.getServo();
        winch2 = RobotConfig.Intake2.getServo();
    }

    @Override
    public void preStart() {}

    // ------------------------------ COMMANDS ------------------------- //

    public Command start() {
        return new SetPower(intake, intakeSpeed);
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
    public Command stop() {
        return new SetPower(intake, 0);
    }
    public Command reverse() {
        return new SetPower(intake, reverseSpeed);
    }
}
