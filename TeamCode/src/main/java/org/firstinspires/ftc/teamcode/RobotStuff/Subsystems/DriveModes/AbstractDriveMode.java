package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import com.bylazar.telemetry.TelemetryManager;

import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.IAmBetterSubsystem;


public abstract class AbstractDriveMode implements IAmBetterSubsystem {

    RoyallyFuckedUpMode opMode;
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

        this.forwardSupp = RobotConfig.player1().leftStickY();
        this.strafeSupp = RobotConfig.player1().leftStickX();
        this.turnSupp = RobotConfig.player1().rightStickX().negate();
        this.slowmodeSupp = RobotConfig.player1().leftTrigger().atLeast(Sensitivities.p1LTThreshold);

        this.slowmodeSupp.whenTrue(() -> Sensitivities.driveModifier = Sensitivities.slowmodeModifier)
                .whenFalse(() -> Sensitivities.driveModifier = Sensitivities.defaultDriveModifier);
    }


}
