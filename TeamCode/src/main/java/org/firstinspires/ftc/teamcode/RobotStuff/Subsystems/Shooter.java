package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements IBetterSubsystem {

    public static final Shooter INSTANCE = new Shooter();

    MotorEx[] shooterMotors;
    MotorGroup shooters;
    ServoEx hood;
    ServoEx kicker;

    public Command spinUp;
    public Command spinDown;
    public Command idle;
    public Command shoot;

    double shootingSpeed = 0.25;

    @Override
    public void initialize() {

    }

    @Override
    public void hardware() {
        shooterMotors = new MotorEx[] {
                RobotConfig.ShootMotor1.motor,
                RobotConfig.ShootMotor2.motor
        };
        shooters = new MotorGroup(
                shooterMotors[0],
                shooterMotors[1]
        );
        hood = RobotConfig.HoodServo.servo;
        //TODO: Set Hood PID
        kicker = RobotConfig.Kicker.servo;
    }

    @Override
    public void commands() {
        this.spinUp = new SetPower(shooters, 1);
        this.spinDown = new SetPower(shooters, 0);
        this.idle = new SetPower(shooters, 0.5);

        this.shoot = new SequentialGroup(
            new SetPower(shooters, 1),
            new SetPosition(kicker, 1),
            new Delay(shootingSpeed),
            new ParallelGroup(
                new SetPower(shooters, 0.5),
                new SetPosition(kicker, 0)
            )
        );
    }

    public Command setShooterPitch (double angleDegrees) {
        angleDegrees = Math.min(40, Math.max(angleDegrees, 0));
        hood.setPosition(angleDegrees / 40);
        return new NullCommand();
    }



    @Override
    public void binds() {
        RobotConfig.ButtonControls.SHOOT.whenTrue(this.shoot);
    }

    @Override
    public void periodic() {
    }


}
