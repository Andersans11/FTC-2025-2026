package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.hardware.impl.MotorEx;

public class MotorConfig {

    public String name;
    public Direction direction;
    public MotorEx motor;

    /**
     * a way to easily create and use a MotorEx
     * @param hardwareMap the HardwareMap to get the motor from
     * @param motorName the name to search for in the hardwareMap
     * @param direction the direction for the motor to rotate in
     * @param zeroPowerBehavior the ZeroPowerBehavior for the motor to use
     */
    public MotorConfig(HardwareMap hardwareMap, String motorName, Direction direction, ZeroPowerBehavior zeroPowerBehavior) {
        this.name = motorName;
        this.direction = direction;
        this.motor = new MotorEx(() -> {
            DcMotorEx temp = hardwareMap.get(DcMotorEx.class, name);

            temp.setDirection(direction);
            temp.setZeroPowerBehavior(zeroPowerBehavior);
            return temp;
        });
    }

    public String getName() { return name; }
    public MotorEx getMotor() { return motor; }
    public Direction getDirection() { return direction; }
}
