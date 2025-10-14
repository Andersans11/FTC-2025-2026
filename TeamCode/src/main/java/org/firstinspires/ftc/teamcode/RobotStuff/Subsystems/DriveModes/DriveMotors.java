package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import dev.nextftc.bindings.Range;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;


public abstract class DriveMotors {

    NextFTCOpMode opMode;
    Range forwardSupp;
    Range strafeSupp;
    Range turnSupp;

    MotorEx FL;
    MotorEx FR;
    MotorEx BL;
    MotorEx BR;

    public DriveMotors(NextFTCOpMode opMode) {
        this.opMode = opMode;

        this.forwardSupp = RobotConfig.playerOne.ForwardAxis;
        this.strafeSupp = RobotConfig.playerOne.StrafeAxis;
        this.turnSupp = RobotConfig.playerOne.TurnAxis;

        this.FL = RobotConfig.FLDrive.motor;
        this.FR = RobotConfig.FRDrive.motor;
        this.BL = RobotConfig.BLDrive.motor;
        this.BR = RobotConfig.BRDrive.motor;

    }

    public abstract void update(long deltaTimeNano);

    public abstract void schedule();
}
