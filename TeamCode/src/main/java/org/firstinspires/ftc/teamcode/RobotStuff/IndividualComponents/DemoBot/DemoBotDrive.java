package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.rowanmcalpin.nextftc.ftc.driving.DifferentialArcadeDriverControlled;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.DemoRobotConfig;

public class DemoBotDrive extends DemoBotInternal {

    DifferentialArcadeDriverControlled vroom;

    public DemoBotDrive(OpMode opMode, DemoRobotConfig config) {
        super(opMode, config);
        this.vroom = new DifferentialArcadeDriverControlled(leftMotor, rightMotor, config.playerOne.DriveStick, config.playerOne.RightStick);
    }

    public void Start() {
        vroom.invoke();
    }
}

abstract class DemoBotInternal {

    OpMode opMode;
    DemoRobotConfig config;

    public DemoBotInternal(OpMode opMode, DemoRobotConfig config) {
        this.opMode = opMode;
        this.config = config;

        initDrive();
    }

    public DcMotorEx leftWheel;
    public DcMotorEx rightWheel;

    public MotorEx leftMotor;
    public MotorEx rightMotor;
    //Drive motors
    // oh no way i thought it would be the lift motors
    public void initDrive() {
        leftWheel = opMode.hardwareMap.get(DcMotorEx.class, config.LeftWheel.name);
        rightWheel = opMode.hardwareMap.get(DcMotorEx.class, config.RightWheel.name);

        leftWheel.setDirection(config.LeftWheel.direction); // no it won't, ignore the error
        rightWheel.setDirection(config.RightWheel.direction);

        leftWheel.setZeroPowerBehavior(config.zeroPowerBehavior);
        rightWheel.setZeroPowerBehavior(config.zeroPowerBehavior);

        leftMotor = new MotorEx(leftWheel);
        rightMotor = new MotorEx(rightWheel);
    }
}
