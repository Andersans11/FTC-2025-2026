package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import dev.nextftc.ftc.GamepadEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.FullServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.MotorConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.ServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

public class RobotConfig {

    public static ColorRangeSensor IntakeCS;
    public static DistanceSensor IntakeDS;
    public static VoltageSensor VoltageSensor;
    public static MotorConfig FLDrive;
    public static MotorConfig BLDrive;
    public static MotorConfig FRDrive;
    public static MotorConfig BRDrive;
    public static MotorConfig ShootMotor1;
    public static MotorConfig ShootMotor2;
    public static MotorConfig TurretRotation;
    public static MotorConfig IntakeMotor;
    public static TouchSensor LimitSwitch;

    public static FullServoConfig CarouselCR1;
    public static FullServoConfig CarouselCR2;
    public static FullServoConfig CarouselCR3;
    public static ServoConfig HoodServo;
    public static ServoConfig HoodServo2;
    public static ServoImplEx Indicator;

    public static ServoConfig Kicker;

    public static HuskyLens camera;

    public static GoBildaPinpointDriver Pinpoint;

    public static Limelight3A Limelight;

    static HardwareMap hardwareMap;
    static RRoboticsOpMode opMode;
    static DeltaTimer deltaTimer;
    static GamepadEx p1;
    static GamepadEx p2;

    public static boolean isRedAlliance = false;


    /**
     * initialize the robot config, initializing hardwareMap and setting variables accessed by other classes
     * @param opMode the opMode for the config to use
     * @param deltaTimer the DeltaTimer for the config to use, used in other subsystems
     */
    public static void initConfig(RRoboticsOpMode opMode, DeltaTimer deltaTimer) {
        RobotConfig.opMode = opMode;
        RobotConfig.hardwareMap = opMode.hardwareMap;
        RobotConfig.deltaTimer = deltaTimer;
        RobotConfig.p1 = opMode.P1;
        RobotConfig.p2 = opMode.P2;

        initHardware();
    }

    public static RRoboticsOpMode getOpMode() {
        return opMode;
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

    /**
     * initialize ALL hardware in the opMode's hardwareMap
     */
    private static void initHardware() {

        VoltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        Indicator = hardwareMap.get(ServoImplEx.class, "INDICATOR");

        Pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "PINPOINT");

        IntakeDS = hardwareMap.get(DistanceSensor.class, "DISTANCE");

        IntakeCS = hardwareMap.get(ColorRangeSensor.class, "COLOR");

        camera = hardwareMap.get(HuskyLens.class, "HUSKYLENS");

        LimitSwitch = hardwareMap.get(TouchSensor.class, "LIMIT");

        Limelight = hardwareMap.get(Limelight3A.class, "LIMELIGHT");

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
        CarouselCR1 = new FullServoConfig(
                hardwareMap,
                "CAROUSEL1",
                Servo.Direction.FORWARD
        );
        CarouselCR2 = new FullServoConfig(
                hardwareMap,
                "CAROUSEL2",
                Servo.Direction.FORWARD
        );
        CarouselCR3 = new FullServoConfig(
                hardwareMap,
                "CAROUSEL3",
                Servo.Direction.FORWARD
        );
        HoodServo = new ServoConfig(
                hardwareMap,
                "HOOD",
                Servo.Direction.FORWARD
        );
        HoodServo2 = new ServoConfig(
                hardwareMap,
                "HOOD2",
                Servo.Direction.REVERSE
        );
    }
}
