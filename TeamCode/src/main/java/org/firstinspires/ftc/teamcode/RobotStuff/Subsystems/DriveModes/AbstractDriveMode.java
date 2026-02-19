package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.hardware.impl.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.IRRoboticsSubsystem;


public abstract class AbstractDriveMode implements IRRoboticsSubsystem {

    RRoboticsOpMode opMode;
    Range forwardSupp;
    Range strafeSupp;
    Range turnSupp;
    Button slowmodeSupp;

    MotorEx FL;
    MotorEx FR;
    MotorEx BL;
    MotorEx BR;

    @Override
    public void initialize() {}

    @Override
    public void periodic() {}


    @Override
    public void initSystem() {
        this.opMode = RobotConfig.getOpMode();

        this.FL = RobotConfig.FLDrive.getMotor();
        this.FR = RobotConfig.FRDrive.getMotor();
        this.BL = RobotConfig.BLDrive.getMotor();
        this.BR = RobotConfig.BRDrive.getMotor();

        this.forwardSupp = RobotConfig.player1().leftStickY().negate();
        this.strafeSupp = RobotConfig.player1().leftStickX();
        this.turnSupp = RobotConfig.player1().rightStickX().negate();
        this.slowmodeSupp = RobotConfig.player1().leftTrigger().atLeast(Sensitivities.p1LTThreshold);

        this.slowmodeSupp.whenTrue(() -> {Sensitivities.driveModifier = Sensitivities.slowmodeModifier; Sensitivities.turningSensitivity = 1.25f;})
                .whenFalse(() -> {Sensitivities.driveModifier = Sensitivities.defaultDriveModifier; Sensitivities.turningSensitivity = 1f;});
    }


}
