package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.hardware.impl.ServoEx;

public class ServoConfig {

    public String name;
    public Servo.Direction direction;
    public ServoEx servo;
    HardwareMap hardwareMap;

    public ServoConfig(HardwareMap hardwareMap, String name, Servo.Direction direction) {
        this.hardwareMap = hardwareMap;
        this.name = name;
        this.direction = direction;

        this.servo = new ServoEx(() -> {
            Servo temp = hardwareMap.get(Servo.class, name);
            temp.setDirection(direction);

            return temp;
        });
    }



}
