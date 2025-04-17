package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class VerticalLift extends DoubleLiftBase {



    public static final VerticalLift INSTANCE = new VerticalLift();
    private VerticalLift() { } // nftc boilerplate


    @Override
    public void initialize() {
        setLimits(0 ,13.6);
    }

    @Override
    public void configure(RobotConfig robotConfig) {
        this.robotConfig = robotConfig;
        this.leftMotor = new MotorEx(robotConfig.LeftVertical.name);
        this.rightMotor = new MotorEx(robotConfig.RightVertical.name);
        this.motors = new MotorGroup(leftMotor, rightMotor);
    }

    public Command toLow() {
        return setTargetPosition(0);
    }

    public Command toMiddle() {
        return setTargetPosition(6);
    }

    public Command toHigh() {
        return setTargetPosition(12);
    }


}
