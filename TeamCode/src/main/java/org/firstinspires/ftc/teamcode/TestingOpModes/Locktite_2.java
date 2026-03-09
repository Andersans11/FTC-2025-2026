package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.hardware.impl.MotorEx;

@Disabled
@Configurable
@TeleOp(name = "When I Need A Different Plumber")
public class Locktite_2 extends RRoboticsOpMode {

    public Locktite_2() {
        super();
    }

    MotorEx servo;
    MotorEx servo2;
    public static double power = 0.0;
    double oldPower = 0.0;

    @Override
    public void onInit() {
        super.onInit();
        servo = RobotConfig.ShootMotor1.getMotor();
        servo2 = RobotConfig.ShootMotor2.getMotor();
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
            servo.setPower(power);
            servo2.setPower(power);
            oldPower = power;
        }

        addData("speed1", servo.getVelocity());
        addData("speed2", servo2.getVelocity());
    }
}
