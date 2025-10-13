package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements Subsystem {
    MotorEx intake;

    public void init() {
        intake = RobotConfig.IntakeMotor.motor;
    }

    public Command start() {
        return new SetPower(
                intake,
                1
        );
    }
    public Command idle() {
        return new SetPower(
                intake,
                0.1
        );
    }
    public Command stop() {
        return new SetPower(
                intake,
                0
        );
    }
}
