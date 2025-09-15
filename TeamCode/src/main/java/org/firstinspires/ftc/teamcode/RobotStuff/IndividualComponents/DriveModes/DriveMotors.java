package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;


import dev.nextftc.bindings.Range;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;


public abstract class DriveMotors {

    NextFTCOpMode opMode;
    RobotConfig config;
    Range forwardSupp;
    Range strafeSupp;
    Range turnSupp;

    MotorEx FL;
    MotorEx FR;
    MotorEx BL;
    MotorEx BR;

    public DriveMotors(NextFTCOpMode opMode, RobotConfig config) {
        this.opMode = opMode;
        this.config = config;

        this.forwardSupp = config.playerOne.ForwardAxis;
        this.strafeSupp = config.playerOne.StrafeAxis;
        this.turnSupp = config.playerOne.TurnAxis;

        this.FL = config.FLDrive.motor;
        this.FR = config.FRDrive.motor;
        this.BL = config.BLDrive.motor;
        this.BR = config.BRDrive.motor;

    }

    public abstract void updateDrive(long deltaTimeNano);

    public abstract void Start();
}
