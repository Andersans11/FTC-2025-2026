package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Config.Subconfigs.HardwareConfigs;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoConfig {

    public String name;
    public Servo.Direction direction;
    public Servo servo;
    HardwareMap hardwareMap;

    public ServoConfig(HardwareMap hardwareMap, String name, Servo.Direction direction) {
        this.hardwareMap = hardwareMap;
        this.name = name;
        this.direction = direction;
        initServo();
    }

    public void initServo() {
        servo = hardwareMap.get(Servo.class, name);

        servo.setDirection(direction);
    }

}
