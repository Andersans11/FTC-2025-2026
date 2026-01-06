package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;

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
