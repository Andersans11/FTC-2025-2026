package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {

    MotorEx[] shooterMotors;
    MotorGroup shooters;
    RTPAxon hood;

    public void init() {
        shooterMotors = new MotorEx[] {
                RobotConfig.ShootMotor1.motor,
                RobotConfig.ShootMotor2.motor
        };
        shooters = new MotorGroup(
                shooterMotors[0],
                shooterMotors[1]
        );
        hood = new RTPAxon(RobotConfig.HoodServo.servo, RobotConfig.HoodENC);
        //TODO: Set Hood PID
    }

    public Command setShooterPitch (double angleDegrees) {
        angleDegrees = Math.min(40, Math.max(angleDegrees, 0));
        hood.setTargetRotation(angleDegrees);
        return new NullCommand();
    }

    public Command spinUp() {
        return new SetPower(
                shooters,
                1
        );
    }

    public Command idle() {
        return new SetPower(
                shooters,
                0.5
        );
    }

    public Command SpinDown() {
        return new SetPower(
                shooters,
                0
        );
    }

    public void update() {
        hood.update();
    }
}
