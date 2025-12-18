package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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

@Autonomous(name = "When I Need A Plumber")
public class Locktite extends RoyallyFuckedUpMode {

    public Locktite() {
        super();
    }

    ServoEx servo;

    @Override
    public void onInit() {
        super.onInit();
        servo = RobotConfig.Kicker.getServo();

        P1.triangle().whenBecomesTrue(new SetPosition(servo, Shooter.kickerPos0));
        P1.triangle().whenBecomesTrue(new SetPosition(servo, Shooter.kickerPos0));
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
    }
}
