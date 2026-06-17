package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;

import dev.nextftc.hardware.impl.MotorEx;

@Configurable
@TeleOp(name = "Motor Directions")
public class Locktite_3 extends RRoboticsOpMode {

    public Locktite_3() {
        super();
    }

    public static boolean doIntake = false;
    boolean isIntaking = false;

    DcMotorEx servo;
    DcMotorEx servo2;
    @Override
    public void onInit() {
        super.onInit();
        servo2 = RobotConfig.ShootMotor2.getMotor().getMotor();
        servo = RobotConfig.ShootMotor1.getMotor().getMotor();
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
        if (doIntake != isIntaking) {
            if (doIntake) {
                servo.setPower(1);
                servo2.setPower(1);
            } else {
                servo.setPower(0);
                servo2.setPower(0);
            }
            isIntaking = doIntake;
        }
        addData("vel1", servo.getVelocity());
        addData("vel2", servo2.getVelocity());
    }
}
