package org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.NFTCRobotConfig;

/* A Class to store motors in the config. pull motors from this class when making drives or subsystems. */

public abstract class DriveMotors {

    OpMode opMode;
    RobotConfig config;
    NFTCRobotConfig nftcConfig;

    public DriveMotors(OpMode opMode, RobotConfig config) {
        this.opMode = opMode;
        this.config = config;

        initDrive();
    }

    public DriveMotors(OpMode opMode, NFTCRobotConfig config) {
        this.opMode = opMode;
        this.nftcConfig = config;

        initDrive();
    }


    public DcMotorEx frontLeftDrive;
    public DcMotorEx backLeftDrive;
    public DcMotorEx frontRightDrive;
    public DcMotorEx backRightDrive;

    public Controllable[] driveMotors;

    //Drive motors
    public void initDrive() {
        frontLeftDrive = opMode.hardwareMap.get(DcMotorEx.class, "Front Left Drive");
        backLeftDrive = opMode.hardwareMap.get(DcMotorEx.class, "Back Left Drive");
        frontRightDrive = opMode.hardwareMap.get(DcMotorEx.class, "Front Right Drive");
        backRightDrive = opMode.hardwareMap.get(DcMotorEx.class, "Back Right Drive");

        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveMotors = new Controllable[] {new MotorEx(frontLeftDrive), new MotorEx(frontRightDrive), new MotorEx(backLeftDrive), new MotorEx(backRightDrive)};
    }

    public abstract void updateDrive(double deltaTime);
}
