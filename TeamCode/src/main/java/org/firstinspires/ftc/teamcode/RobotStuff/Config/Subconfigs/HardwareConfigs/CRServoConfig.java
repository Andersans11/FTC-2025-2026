package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CRServoConfig {

    public String name;
    public DcMotorSimple.Direction direction;
    public CRServo servo;
    public AnalogInput encoder;
    HardwareMap hardwareMap;

    public CRServoConfig(HardwareMap hardwareMap, String name, DcMotorSimple.Direction direction, AnalogInput encoder) {
        this.hardwareMap = hardwareMap;
        this.name = name;
        this.direction = direction;
        this.encoder = encoder;
        initServo();
    }

    public void initServo() {
        servo = hardwareMap.get(CRServo.class, name);

        servo.setDirection(direction);
    }

}
