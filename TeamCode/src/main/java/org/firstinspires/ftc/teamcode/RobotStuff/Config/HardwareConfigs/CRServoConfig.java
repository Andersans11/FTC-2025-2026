package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.hardware.impl.CRServoEx;

public class CRServoConfig {

    public String name;
    public DcMotorSimple.Direction direction;
    public CRServoEx servo;
    public AnalogInput encoder;

    /**
     * a way to easily create and use a CRServoEx
     * @param hardwareMap the HardwareMap to get the servo from
     * @param name the name of the servo
     * @param direction the direction for the servo to rotate in
     * @param encoder the encoder for the servo
     */
    public CRServoConfig(HardwareMap hardwareMap, String name, DcMotorSimple.Direction direction, AnalogInput encoder) {
        this.name = name;
        this.direction = direction;
        this.encoder = encoder;
        this.servo = new CRServoEx(() -> {
                CRServo temp = hardwareMap.get(CRServo.class, name);
                temp.setDirection(direction);
                return temp;
            }
        );
    }

    public String getName() { return name; }
    public CRServoEx getServo() { return servo; }
    public DcMotorSimple.Direction getDirection() { return direction; }
    public AnalogInput getEncoder() { return encoder; }



}
