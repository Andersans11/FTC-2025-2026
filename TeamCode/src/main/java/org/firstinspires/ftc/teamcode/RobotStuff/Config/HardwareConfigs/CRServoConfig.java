package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.hardware.impl.CRServoEx;

public class CRServoConfig {

    public String name;
    public DcMotorSimple.Direction direction;
    public CRServoEx servo;
    public AnalogInput encoder;
    HardwareMap hardwareMap;

    public CRServoConfig(HardwareMap hardwareMap, String name, DcMotorSimple.Direction direction, AnalogInput encoder) {
        this.hardwareMap = hardwareMap;
        this.name = name;
        this.direction = direction;
        this.encoder = encoder;
        this.servo = new CRServoEx(
            () -> {
                CRServo temp = hardwareMap.get(CRServo.class, name);
                temp.setDirection(direction);
                return temp;
            }
        );
    }



}
