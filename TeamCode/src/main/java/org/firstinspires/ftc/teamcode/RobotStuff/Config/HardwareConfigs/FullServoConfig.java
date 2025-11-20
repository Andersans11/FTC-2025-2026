package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;

public class FullServoConfig {

    public String name;
    public Servo.Direction direction;
    public ServoExFullRange servo;

    public FullServoConfig(HardwareMap hardwareMap, String name, Servo.Direction direction) {
        this.name = name;
        this.direction = direction;

        this.servo = new ServoExFullRange(() -> {
            ServoImplEx temp = hardwareMap.get(ServoImplEx.class, name);
            temp.setDirection(direction);

            return temp;
        });
    }

    public String getName() { return name; }
    public ServoExFullRange getServo() { return servo; }
    public Servo.Direction getDirection() { return direction; }

}
