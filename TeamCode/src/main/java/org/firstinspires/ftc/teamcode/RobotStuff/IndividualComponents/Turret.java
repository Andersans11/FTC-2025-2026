package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;


import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.powerable.SetPower;
import dev.nextftc.hardware.positionable.SetPosition;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;

public class Turret implements Subsystem {

    NextFTCOpMode opMode;
    RobotConfig config;
    RTPAxon pitchServo;
    MotorGroup shootMotors;
    MotorEx rotationMotor;


    public void init(RobotConfig config, NextFTCOpMode opMode) {
        this.config = config;
        this.opMode = opMode;

        this.pitchServo = new RTPAxon(config.TurretPitch);
        this.rotationMotor = config.TurretRotation.motor;

        this.shootMotors = new MotorGroup(config.ShootMotor1.motor, config.ShootMotor2.motor);

        config.playerOne.RightTrigger
                .whenTrue(() -> new SetPower(shootMotors,1))
                .whenFalse(() -> new SetPower(shootMotors,0));

        config.playerTwo.LeftX.greaterThan(0.01).or(() -> config.playerTwo.LeftX.lessThan(-0.01).get())
                .whenTrue(() -> new SetPower(rotationMotor, Sensitivities.turretTurnSpeed * config.playerTwo.LeftX.get()))
                .whenFalse(() -> new SetPower(rotationMotor, 0));

        config.playerTwo.RightY.greaterThan(0.01).or(() -> config.playerTwo.RightY.lessThan(-0.01).get())
                .whenTrue(() -> pitchServo.setPower(Sensitivities.turretPitchSpeed * config.playerTwo.RightY.get()))
                .whenFalse(() -> pitchServo.setPower(0));
    }

}
