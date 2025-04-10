package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MotorConfig {

    public String name;
    public DcMotorSimple.Direction direction;

    public MotorConfig (String motorName, DcMotorSimple.Direction direction) {
        name = motorName;
        this.direction = direction;
    }
}
