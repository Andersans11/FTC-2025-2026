package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Reset Encoders", group = Utils.UTILITY)
public class Reset_Encoders extends RRoboticsOpMode {

    public Reset_Encoders() {}

    MotorEx rotationMotor;

    @Override
    public void onInit() {
        super.onInit();
        for (DcMotor motor: hardwareMap.dcMotor){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        stop();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }
}
