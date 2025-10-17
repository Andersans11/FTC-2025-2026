package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import androidx.annotation.StringDef;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.CRServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.MotorConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.ServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;

import java.lang.reflect.Field;

@Config
public class RobotConfig {

    public static AnalogInput CarouselENC1;
    public static AnalogInput CarouselENC2;
    public static AnalogInput CarouselENC3;
    public static AnalogInput HoodENC;

    public static ColorSensor IntakeCS;

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
    public static CRServoConfig HoodServo;

    public static ServoConfig Kicker;

    public static HuskyLens camera;

    public static GoBildaPinpointDriver Pinpoint;

    static HardwareMap hardwareMap;
    static NextFTCOpMode opMode;
    static DeltaTimer deltaTimer;

    public static boolean isRedAlliance = false;


    public static void initConfig(NextFTCOpMode opMode, DeltaTimer deltaTimer) {
        RobotConfig.opMode = opMode;
        RobotConfig.hardwareMap = opMode.hardwareMap;
        RobotConfig.deltaTimer = deltaTimer;

        initHardware(hardwareMap);
    }

    public static NextFTCOpMode getOpMode() {
        return opMode;
    }

    public static Telemetry getTelemetry() {
        return opMode.telemetry;
    }

    public static long getDelta() {
        return deltaTimer.getDelta();
    }

    public static class ButtonControls {
        public static Button MAGAZINE_SLOT1 = null;
        public static Button MAGAZINE_SLOT2 = null;
        public static Button MAGAZINE_SLOT3 = null;
        public static Button INTAKE = null;
        public static Button SHOOT = null;
        public static Button SHOOT_MOTIF = null;
        public static Button SLOWMODE = null;

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
            "SHOOT",
            "SHOOT_MOTIF",
            "SLOWMODE"
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

        Pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "sensor");

        CarouselENC1 = hardwareMap.get(AnalogInput.class, "CarENC1");
        CarouselENC2 = hardwareMap.get(AnalogInput.class, "CarENC2");
        CarouselENC3 = hardwareMap.get(AnalogInput.class, "CarENC3");

        HoodENC = hardwareMap.get(AnalogInput.class, "HoodENC");

        IntakeCS = hardwareMap.get(ColorSensor.class, "IntakeCS");

        camera = hardwareMap.get(HuskyLens.class, "Camera");

        FLDrive = new MotorConfig(
                hardwareMap,
                "Front Left Drive",
                DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        BLDrive = new MotorConfig(
                hardwareMap,
                "Back Left Drive",
                DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        FRDrive = new MotorConfig(
                hardwareMap,
                "Front Right Drive",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        BRDrive = new MotorConfig(
                hardwareMap,
                "Back Right Drive",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        ShootMotor1 = new MotorConfig(
                hardwareMap,
                "Shooter 1",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.FLOAT
        );
        ShootMotor2 = new MotorConfig(
                hardwareMap,
                "Shooter 2",
                DcMotorSimple.Direction.REVERSE, // motors are facing opposite directions
                DcMotor.ZeroPowerBehavior.FLOAT
        );
        TurretRotation = new MotorConfig(
                hardwareMap,
                "Turret Rotation",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        IntakeMotor = new MotorConfig(
                hardwareMap,
                "Intake Motor",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        Kicker = new ServoConfig(
                hardwareMap,
                "Kicker Servo",
                Servo.Direction.FORWARD
        );
        CarouselCR1 = new CRServoConfig(
                hardwareMap,
                "Carousel 1",
                DcMotorSimple.Direction.FORWARD,
                CarouselENC1
        );
        CarouselCR2 = new CRServoConfig(
                hardwareMap,
                "Carousel 2",
                DcMotorSimple.Direction.FORWARD,
                CarouselENC2
        );
        CarouselCR3 = new CRServoConfig(
                hardwareMap,
                "Carousel 3",
                DcMotorSimple.Direction.FORWARD,
                CarouselENC3
        );
        HoodServo = new CRServoConfig(
                hardwareMap,
                "Turret Pitch",
                DcMotorSimple.Direction.FORWARD,
                HoodENC
        );
    }
}
