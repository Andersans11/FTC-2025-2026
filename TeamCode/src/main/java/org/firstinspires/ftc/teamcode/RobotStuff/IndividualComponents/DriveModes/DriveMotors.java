package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

/* A Class to store motors in the config. pull motors from this class when making drives or subsystems. */

public abstract class DriveMotors {

    OpMode opMode;
    RobotConfig config;

    public DriveMotors(OpMode opMode, RobotConfig config) {
        this.opMode = opMode;
        this.config = config;

        driveMotors = new Controllable[] {config.FLDrive.motor, config.FRDrive.motor, config.BLDrive.motor, config.BRDrive.motor};
    }
    public Controllable[] driveMotors;
    //Drive motors
    // oh no way i thought it would be the lift motors

    public abstract void updateDrive(long deltaTimeNano);

    public abstract void Start();
}
