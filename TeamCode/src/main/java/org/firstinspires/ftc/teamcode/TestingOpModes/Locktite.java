package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

@Configurable
@Autonomous(name = "When I Need A Plumber")
public class Locktite extends RoyallyFuckedUpMode {

    public Locktite() {
        super();
    }

    ServoImplEx servo;
    int PWM = 1650;
    int oldPWM = 0;
    public static int upperPWM = 1600;
    public static int lowerPWM = 500;

    @Override
    public void onInit() {
        super.onInit();
        servo = RobotConfig.Indicator;
        servo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        P1.dpadUp().whenBecomesTrue(() -> PWM = upperPWM);
        P1.dpadDown().whenBecomesTrue(() -> PWM = lowerPWM);
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
        if (PWM != oldPWM) {
            servo.setPosition(PWMToPower(PWM));
            oldPWM = PWM;
        }
        addData("PWM", servo.getPosition());
    }

    public double PWMToPower(double PWM) {
        return (PWM - 500) / 2000;
    }
}
