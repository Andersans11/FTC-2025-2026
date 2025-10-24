package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import java.util.function.Function;
import java.util.function.Supplier;

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
    public Supplier<Command> shoot;
    public Function<Double, Command> setHoodPos;

    double shootingSpeed = 0.75;

    @Override
    public void initialize() {}

    @Override
    public void binds() {
        RobotConfig.ButtonControls.SHOOT.whenBecomesTrue(this.shoot.get());
        RobotConfig.ButtonControls.STOP_SHOOT.whenBecomesTrue(this.spinDown);
    }


    @Override
    public void periodic() {}

    @Override
    public void commands() {
        this.spinUp = new SetPower(shooters, -1);
        this.spinDown = new SetPower(shooters, 0);
        this.idle = new SetPower(shooters, -0.7);

        this.shoot = () -> new SequentialGroup(
                this.spinUp,
                new SetPosition(kicker, 0.2),
                new Delay(shootingSpeed),
                new SetPosition(kicker, 1),
                this.idle
        );

        this.setHoodPos = (pos) -> new SetPosition(hood, pos);
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
        kicker = RobotConfig.Kicker.servo;
    }
}
