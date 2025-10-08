package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;


import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.powerable.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;

public class Turret implements Subsystem {

    NextFTCOpMode opMode;
    RTPAxon pitchServo;
    MotorGroup shootMotors;
    MotorEx rotationMotor;


    public void init(NextFTCOpMode opMode) {
        this.opMode = opMode;

        this.pitchServo = new RTPAxon(RobotConfig.HoodServo);
        this.rotationMotor = RobotConfig.TurretRotation.motor;

        this.shootMotors = new MotorGroup(RobotConfig.ShootMotor1.motor, RobotConfig.ShootMotor2.motor);

        RobotConfig.playerOne.RightTrigger
                .whenTrue(() -> new SetPower(shootMotors,1))
                .whenFalse(() -> new SetPower(shootMotors,0));

        RobotConfig.playerTwo.LeftX.greaterThan(0.01).or(() -> RobotConfig.playerTwo.LeftX.lessThan(-0.01).get())
                .whenTrue(() -> new SetPower(rotationMotor, Sensitivities.turretTurnSpeed * RobotConfig.playerTwo.LeftX.get()))
                .whenFalse(() -> new SetPower(rotationMotor, 0));

        RobotConfig.playerTwo.RightY.greaterThan(0.01).or(() -> RobotConfig.playerTwo.RightY.lessThan(-0.01).get())
                .whenTrue(() -> pitchServo.setPower(Sensitivities.turretPitchSpeed * RobotConfig.playerTwo.RightY.get()))
                .whenFalse(() -> pitchServo.setPower(0));
    }

}
