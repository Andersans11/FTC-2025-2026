package org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

/* A Class to store motors in the config. pull motors from this class when making drives or subsystems. */

public abstract class DriveMotors {

    OpMode opMode;
    RobotConfig config;

    public DriveMotors(OpMode opMode, RobotConfig config) {
        this.opMode = opMode;
        this.config = config;

        initDrive();
    }

    public DcMotorEx frontLeftDrive;
    public DcMotorEx backLeftDrive;
    public DcMotorEx frontRightDrive;
    public DcMotorEx backRightDrive;
    public Controllable[] driveMotors;
    //Drive motors
    // oh no way i thought it would be the lift motors
    public void initDrive() {
        frontLeftDrive = opMode.hardwareMap.get(DcMotorEx.class, config.FLDrive.name);
        backLeftDrive = opMode.hardwareMap.get(DcMotorEx.class, config.BLDrive.name);
        frontRightDrive = opMode.hardwareMap.get(DcMotorEx.class, config.FRDrive.name);
        backRightDrive = opMode.hardwareMap.get(DcMotorEx.class, config.BRDrive.name);

        frontLeftDrive.setDirection(config.FLDrive.direction); // no it won't, ignore the error
        backLeftDrive.setDirection(config.BLDrive.direction);
        frontRightDrive.setDirection(config.FRDrive.direction);
        backRightDrive.setDirection(config.BRDrive.direction);

        frontLeftDrive.setZeroPowerBehavior(config.zeroPowerBehavior);
        backLeftDrive.setZeroPowerBehavior(config.zeroPowerBehavior);
        frontRightDrive.setZeroPowerBehavior(config.zeroPowerBehavior);
        backRightDrive.setZeroPowerBehavior(config.zeroPowerBehavior);

        driveMotors = new Controllable[] {new MotorEx(frontLeftDrive), new MotorEx(frontRightDrive), new MotorEx(backLeftDrive), new MotorEx(backRightDrive)};
    }

    public abstract void updateDrive(long deltaTimeNano);

    public abstract void Start();
}
