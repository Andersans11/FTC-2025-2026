package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.hardware.impl.MotorEx;

@Configurable
@TeleOp(name = "Motor Directions")
public class Locktite_3 extends RRoboticsOpMode {

    public Locktite_3() {
        super();
    }

    DcMotorEx servo;
    DcMotorEx servo2;
    DcMotorEx servo3;
    DcMotorEx servo4;

    @Override
    public void onInit() {
        super.onInit();
        servo2 = hardwareMap.get(DcMotorEx.class, "FR");
        servo = hardwareMap.get(DcMotorEx.class, "BR");
        servo4 = hardwareMap.get(DcMotorEx.class, "FL");
        servo3 = hardwareMap.get(DcMotorEx.class, "BL");

        servo.setDirection(DcMotorSimple.Direction.FORWARD);
        servo2.setDirection(DcMotorSimple.Direction.FORWARD);
        servo3.setDirection(DcMotorSimple.Direction.REVERSE);
        servo4.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

        P1.square().whenBecomesTrue(() -> servo.setPower(1));
        P1.triangle().whenBecomesTrue(() -> servo2.setPower(1));
        P1.cross().whenBecomesTrue(() -> servo3.setPower(1));
        P1.circle().whenBecomesTrue(() -> servo4.setPower(1));

        P1.square().whenBecomesFalse(() -> servo.setPower(0));
        P1.triangle().whenBecomesFalse(() -> servo2.setPower(0));
        P1.cross().whenBecomesFalse(() -> servo3.setPower(0));
        P1.circle().whenBecomesFalse(() -> servo4.setPower(0));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addLine("square = Front Right");
        addLine("triangle = Back Right");
        addLine("cross = Front Left");
        addLine("circle = Back Left");
    }
}
