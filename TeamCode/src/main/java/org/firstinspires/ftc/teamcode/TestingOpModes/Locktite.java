package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;

import dev.nextftc.hardware.impl.ServoEx;

@Configurable
@TeleOp(name = "When I Need A Plumber")
public class Locktite extends RRoboticsOpMode {

    public Locktite() {
        super();
    }

    ServoExFullRange servo;
    public static double power = 0.0;
    double oldPower = 0.0;

    @Override
    public void onInit() {
        super.onInit();
        servo = RobotConfig.HoodServo.getServo();
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (power != oldPower) {
            servo.setPosition(power);
            oldPower = power;
        }
    }
}
