package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.hardware.impl.ServoEx;

public class ServoConfig {

    String name;
    Servo.Direction direction;
    ServoEx servo;

    /**
     * A way to easily create and use a ServoEx
     * @param hardwareMap the HardwareMap to get the servo from
     * @param name the name of the Servo to look for in
     * @param direction the direction to set the servo's rotation
     */
    public ServoConfig(HardwareMap hardwareMap, String name, Servo.Direction direction) {
        this.name = name;
        this.direction = direction;

        this.servo = new ServoEx(() -> {
            Servo temp = hardwareMap.get(Servo.class, name);
            temp.setDirection(direction);

            return temp;
        });
    }

    public String getName() { return name; }
    public ServoEx getServo() { return servo; }
    public Servo.Direction getDirection() { return direction; }
}
