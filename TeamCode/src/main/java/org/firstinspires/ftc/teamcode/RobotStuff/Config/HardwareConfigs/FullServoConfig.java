package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import dev.nextftc.hardware.impl.ServoEx;

public class FullServoConfig {

    public String name;
    public Servo.Direction direction;
    public ServoExFullRange servo;
    HardwareMap hardwareMap;

    public FullServoConfig(HardwareMap hardwareMap, String name, Servo.Direction direction) {
        this.hardwareMap = hardwareMap;
        this.name = name;
        this.direction = direction;

        this.servo = new ServoExFullRange(() -> {
            ServoImplEx temp = hardwareMap.get(ServoImplEx.class, name);
            temp.setDirection(direction);

            return temp;
        });
    }



}
