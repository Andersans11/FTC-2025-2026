package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.hardware.impl.MotorEx;

public class MotorConfig {

    public String name;
    public Direction direction;
    public ZeroPowerBehavior zeroPowerBehavior;
    public MotorEx motor;

    HardwareMap hardwareMap;

    public double encoderPPR;

    public MotorConfig(HardwareMap hardwareMap, String motorName, Direction direction, ZeroPowerBehavior zeroPowerBehavior, double encoderPPR) {
        this.hardwareMap = hardwareMap;
        this.name = motorName;
        this.direction = direction;
        this.zeroPowerBehavior = zeroPowerBehavior;
        this.encoderPPR = encoderPPR;

        this.motor = new MotorEx(() -> {
            DcMotorEx tempMotor = hardwareMap.get(DcMotorEx.class, name);

            tempMotor.setDirection(direction);
            tempMotor.setZeroPowerBehavior(zeroPowerBehavior);
            return tempMotor;
        });
    }

    public MotorConfig(HardwareMap hardwareMap, String motorName, Direction direction, ZeroPowerBehavior zeroPowerBehavior) {
        this.hardwareMap = hardwareMap;
        this.name = motorName;
        this.direction = direction;
        this.zeroPowerBehavior = zeroPowerBehavior;

        this.motor = new MotorEx(() -> {
            DcMotorEx temp = hardwareMap.get(DcMotorEx.class, name);

            temp.setDirection(direction);
            temp.setZeroPowerBehavior(zeroPowerBehavior);
            return temp;
        });
    }
}
