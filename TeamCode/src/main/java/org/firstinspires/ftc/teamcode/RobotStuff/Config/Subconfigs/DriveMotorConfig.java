package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveMotorConfig {

    public String name;
    public DcMotorSimple.Direction direction;

    public DriveMotorConfig(String motorName, DcMotorSimple.Direction direction) {
        this.name = motorName;
        this.direction = direction;
    }
}
