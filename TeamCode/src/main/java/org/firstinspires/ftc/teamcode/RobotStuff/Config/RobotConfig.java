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

    public static AnalogInput CarouselENC1;

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

    public static CRServoConfig CarouselCR1;
    public static CRServoConfig CarouselCR2;
    public static CRServoConfig CarouselCR3;
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

    public static class ButtonControls {
        public static Button MAGAZINE_SLOT1 = null;
        public static Button MAGAZINE_SLOT2 = null;
        public static Button MAGAZINE_SLOT3 = null;
        public static Button INTAKE = null;
        public static Button INTAKE_STOP = null;
        public static Button SHOOT = null;
        public static Button SHOOT_MOTIF = null;
        public static Button SLOWMODE = null;
        public static Button SHOOT_GREEN = null;
        public static Button SHOOT_PURPLE = null;
        public static Button STOP_SHOOT = null;
        public static Button INTAKE_MODE = null;
        public static Button OUTTAKE_MODE = null;
    }

    public static class RangeControls {
        public static Range FB = null;
        public static Range STRAFE = null;
        public static Range YAW = null;
        public static Range MAGAZINE_ROT = null;
        public static Range TURRET_ROT = null;
        public static Range TURRET_PITCH = null;
    }

    @StringDef({
            "MAGAZINE_SLOT1",
            "MAGAZINE_SLOT2",
            "MAGAZINE_SLOT3",
            "INTAKE",
            "INTAKE_STOP",
            "SHOOT",
            "SHOOT_MOTIF",
            "STOP_SHOOT",
            "SLOWMODE",
            "SHOOT_GREEN",
            "SHOOT_PURPLE",
            "INTAKE_MODE",
            "OUTTAKE_MODE"
    })
    @interface ButtonOption {}

    @StringDef({
            "FB",
            "STRAFE",
            "YAW",
            "MAGAZINE_ROT",
            "TURRET_ROT",
            "TURRET_PITCH"
    })
    @interface RangeOption {}

    public static void bind(Button button, @ButtonOption String control) {
        try {
            Field field = ButtonControls.class.getDeclaredField(control);
            field.set(ButtonControls.class, button);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            opMode.telemetry.addLine("Field with name " + control + "could not be found. This error should not be possible.");
        } catch (IllegalAccessException e) {
            opMode.telemetry.addLine("Field object is enforcing Java language access control, and the underlying field is inaccessible");
        }
    }

    public static void bind(Range range, @RangeOption String control) {
        try {
            Field field = RangeControls.class.getDeclaredField(control);
            field.set(RangeControls.class, range);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            opMode.telemetry.addLine("Field with name " + control + "could not be found. This error should not be possible.");
        } catch (IllegalAccessException e) {
            opMode.telemetry.addLine("Field object is enforcing Java language access control, and the underlying field is inaccessible");
        }
    }

    private static void initHardware (HardwareMap hardwareMap) {

        Pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "PINPOINT");

        CarouselENC1 = hardwareMap.get(AnalogInput.class, "CARENC1");

        //IntakeCS = hardwareMap.get(ColorSensor.class, "IntakeCS");
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
        CarouselCR1 = new CRServoConfig(
                hardwareMap,
                "CAROUSEL1",
                DcMotorSimple.Direction.FORWARD,
                CarouselENC1
        );
        CarouselCR2 = new CRServoConfig(
                hardwareMap,
                "CAROUSEL2",
                DcMotorSimple.Direction.FORWARD,
                CarouselENC1
        );
        CarouselCR3 = new CRServoConfig(
                hardwareMap,
                "CAROUSEL3",
                DcMotorSimple.Direction.FORWARD,
                CarouselENC1
        );
        HoodServo = new ServoConfig(
                hardwareMap,
                "HOOD",
                Servo.Direction.FORWARD
        );
    }
}
