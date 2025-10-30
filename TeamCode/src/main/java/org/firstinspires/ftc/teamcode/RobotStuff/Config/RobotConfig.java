package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import androidx.annotation.StringDef;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.CRServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.MotorConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.ServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import java.lang.reflect.Field;

public class RobotConfig {

    public static ColorSensor IntakeCS;
    public static DistanceSensor IntakeDS;
    public static MotorConfig FLDrive;
    public static MotorConfig BLDrive;
    public static MotorConfig FRDrive;
    public static MotorConfig BRDrive;
    public static MotorConfig ShootMotor1;
    public static MotorConfig ShootMotor2;
    public static MotorConfig TurretRotation;
    public static MotorConfig IntakeMotor;

    public static ServoConfig CarouselCR1;
    public static ServoConfig CarouselCR2;
    public static ServoConfig CarouselCR3;
    public static ServoConfig HoodServo;

    public static ServoConfig Kicker;

    public static HuskyLens camera;

    public static GoBildaPinpointDriver Pinpoint;

    static HardwareMap hardwareMap;
    static RoyallyFuckedUpMode opMode;
    static DeltaTimer deltaTimer;
    static GamepadEx p1;
    static GamepadEx p2;

    public static boolean isRedAlliance = false;


    public static void initConfig(RoyallyFuckedUpMode opMode, DeltaTimer deltaTimer) {
        RobotConfig.opMode = opMode;
        RobotConfig.hardwareMap = opMode.hardwareMap;
        RobotConfig.deltaTimer = deltaTimer;
        RobotConfig.p1 = opMode.P1;
        RobotConfig.p2 = opMode.P2;

        initHardware(hardwareMap);
    }

    public static RoyallyFuckedUpMode getOpMode() {
        return opMode;
    }

    public static Telemetry getTelemetry() {
        return opMode.telemetry;
    }
    public static long getDelta() {
        return deltaTimer.getDelta();
    }
    public static GamepadEx player1() {
        return p1;
    }
    public static GamepadEx player2() {
        return p2;
    }
    public static HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    private static void initHardware (HardwareMap hardwareMap) {

        Pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "PINPOINT");

        IntakeDS = hardwareMap.get(DistanceSensor.class, "Dis");

        camera = hardwareMap.get(HuskyLens.class, "HUSKYLENS");

        FLDrive = new MotorConfig(
                hardwareMap,
                "FLDRIVE",
                DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        BLDrive = new MotorConfig(
                hardwareMap,
                "BLDRIVE",
                DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        FRDrive = new MotorConfig(
                hardwareMap,
                "FRDRIVE",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        BRDrive = new MotorConfig(
                hardwareMap,
                "BRDRIVE",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        ShootMotor1 = new MotorConfig(
                hardwareMap,
                "SHOOTER1",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.FLOAT
        );
        ShootMotor2 = new MotorConfig(
                hardwareMap,
                "SHOOTER2",
                DcMotorSimple.Direction.REVERSE, // motors are facing opposite directions
                DcMotor.ZeroPowerBehavior.FLOAT
        );
        TurretRotation = new MotorConfig(
                hardwareMap,
                "TURRET",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        IntakeMotor = new MotorConfig(
                hardwareMap,
                "INTAKE",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        Kicker = new ServoConfig(
                hardwareMap,
                "KICKER",
                Servo.Direction.FORWARD
        );
        CarouselCR1 = new ServoConfig(
                hardwareMap,
                "CAROUSEL1",
                Servo.Direction.FORWARD
        );
        CarouselCR2 = new ServoConfig(
                hardwareMap,
                "CAROUSEL2",
                Servo.Direction.FORWARD
        );
        CarouselCR3 = new ServoConfig(
                hardwareMap,
                "CAROUSEL3",
                Servo.Direction.FORWARD
        );
        HoodServo = new ServoConfig(
                hardwareMap,
                "HOOD",
                Servo.Direction.FORWARD
        );
    }
}
