package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine.Magazine;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Shooter implements Subsystem {

    MotorEx[] shooters;
    RTPAxon hood;

    public void init() {
        shooters = new MotorEx[] {
                RobotConfig.ShootMotor1.motor,
                RobotConfig.ShootMotor2.motor
        };
        hood = new RTPAxon(RobotConfig.HoodServo.servo, RobotConfig.HoodENC);
        //TODO: Set Hood PID
    }

    public Command setShooterPitch (double angleDegrees) {
        hood.setTargetRotation(angleDegrees);
        return new NullCommand();
    }

    public void update() {
        hood.update();
    }
}
