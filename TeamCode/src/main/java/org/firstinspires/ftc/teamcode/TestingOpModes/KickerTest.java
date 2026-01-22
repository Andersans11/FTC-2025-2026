package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.hardware.impl.ServoEx;

@Configurable
@Autonomous(name = "Test: Kicker")
public class KickerTest extends RoyallyFuckedUpMode {

    public KickerTest() {
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
