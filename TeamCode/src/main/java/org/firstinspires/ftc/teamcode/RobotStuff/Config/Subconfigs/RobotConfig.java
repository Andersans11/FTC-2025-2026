package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.CRServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.MotorConfig;

@Config
public class RobotConfig {

    public static ReadyPlayerOne playerOne;
    public static ReadyPlayerTwo playerTwo;

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

    public static CRServoConfig CarouselCR1;
    public static CRServoConfig CarouselCR2;
    public static CRServoConfig CarouselCR3;
    public static CRServoConfig HoodServo;

    static HardwareMap hardwareMap;
    static NextFTCOpMode opMode;


    public static void initConfig(NextFTCOpMode opMode) {
        RobotConfig.opMode = opMode;
        RobotConfig.hardwareMap = opMode.hardwareMap;

        playerOne = new ReadyPlayerOne(opMode.gamepad1);
        playerTwo = new ReadyPlayerTwo(opMode.gamepad2);

        initHardware(hardwareMap);
    }


    public static void gamepadUpdates() {
        playerOne.update_all();
        playerTwo.update_all();
    }
    private static void initHardware (HardwareMap hardwareMap) {

        CarouselENC1 = hardwareMap.get(AnalogInput.class, "CarENC1");
        CarouselENC2 = hardwareMap.get(AnalogInput.class, "CarENC2");
        CarouselENC3 = hardwareMap.get(AnalogInput.class, "CarENC3");
        HoodENC = hardwareMap.get(AnalogInput.class, "HoodENC");

        IntakeCS = hardwareMap.get(ColorSensor.class, "IntakeCS");

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
