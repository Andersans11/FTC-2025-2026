package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.ServoEx;


@Configurable
@TeleOp(name = "When I Need A Plumber")
public class Locktite extends NextFTCOpMode {

    public Locktite() {
        super();
    }

    ServoExFullRange servo;
    public static double power = 0.0;
    double oldPower = 0.0;
    public static String servoName;

    @Override
    public void onInit() {
        super.onInit();
        servo = new ServoExFullRange(() -> {
            return hardwareMap.get(ServoImplEx.class, servoName);
        });
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
    }

    @Override
    public void onUpdate() {
        if (power != oldPower) {
            servo.setPosition(power);
            oldPower = power;
        }
    }
}
