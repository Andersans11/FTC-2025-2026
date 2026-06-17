package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;

import dev.nextftc.ftc.NextFTCOpMode;


@Configurable
@TeleOp(name = "When I Need A Different Plumber")
public class Locktite_2 extends NextFTCOpMode {

    public Locktite_2() {
        super();
    }

    ServoExFullRange servo1;
    ServoExFullRange servo2;
    public static double power1 = 0.5;
    public static double power2 = 0.5;
    double oldPower1 = 0.0;
    double oldPower2 = 0.0;
    public static String servoName1;
    public static String servoName2;

    @Override
    public void onInit() {
        servo1 = new ServoExFullRange(() -> {
            return hardwareMap.get(ServoImplEx.class, servoName1);
        });
        servo2 = new ServoExFullRange(() -> {
            return hardwareMap.get(ServoImplEx.class, servoName2);
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
        if (power1 != oldPower1) {
            servo1.setPosition(power1);
            oldPower1 = power1;
        }
        if (power2 != oldPower2) {
            servo2.setPosition(power2);
            oldPower2 = power2;
        }
    }
}
