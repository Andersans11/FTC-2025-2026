package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;

import dev.nextftc.hardware.impl.ServoEx;

@Configurable
@Autonomous(name = "When I Need A Plumber")
public class Locktite extends RoyallyFuckedUpMode {

    public Locktite() {
        super();
    }

    ServoEx servo;
    public static double power = 0.0;
    double oldPower = 0.0;

    @Override
    public void onInit() {
        super.onInit();
        servo = RobotConfig.Kicker.getServo();
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
