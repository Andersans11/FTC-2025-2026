package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Reset Encoders", group = Utils.UTILITY)
public class Reset_Encoders extends RoyallyFuckedUpMode {

    public Reset_Encoders() {}

    MotorEx rotationMotor;

    @Override
    public void onInit() {
        super.onInit();
        rotationMotor = RobotConfig.TurretRotation.motor;
        rotationMotor.zero();
        stop();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }
}
