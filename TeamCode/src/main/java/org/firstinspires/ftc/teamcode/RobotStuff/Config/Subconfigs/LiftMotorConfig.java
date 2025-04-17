package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LiftMotorConfig {

    public String name;
    public DcMotorSimple.Direction direction;

    public double encoderPPR;

    public LiftMotorConfig(String motorName, DcMotorSimple.Direction direction, double encoderPPR) {
        this.name = motorName;
        this.direction = direction;
        this.encoderPPR = encoderPPR;
    }
}
