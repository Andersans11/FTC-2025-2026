package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;


public abstract class DriveMotors {

    NextFTCOpMode opMode;
    Range forwardSupp;
    Range strafeSupp;
    Range turnSupp;
    Button slowmodeSupp;

    MotorEx FL;
    MotorEx FR;
    MotorEx BL;
    MotorEx BR;

    public DriveMotors(NextFTCOpMode opMode) {
        this.opMode = opMode;

        this.forwardSupp = RobotConfig.RangeControls.FB;
        this.strafeSupp = RobotConfig.RangeControls.STRAFE;
        this.turnSupp = RobotConfig.RangeControls.YAW;
        this.slowmodeSupp = RobotConfig.ButtonControls.SLOWMODE;

        this.slowmodeSupp.whenTrue(() -> Sensitivities.driveModifier = 0.4f)
                .whenFalse(() -> Sensitivities.driveModifier = 1f);

        this.FL = RobotConfig.FLDrive.motor;
        this.FR = RobotConfig.FRDrive.motor;
        this.BL = RobotConfig.BLDrive.motor;
        this.BR = RobotConfig.BRDrive.motor;

    }

    public abstract void update(long deltaTimeNano);

    public abstract void schedule();
}
