package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.CRServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.MotorConfig;

@Config
public class RobotConfig {

    public final ReadyPlayerOne playerOne;
    public final ReadyPlayerTwo playerTwo;

    public final Sensitivities sensitivities;
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;

    public static MotorConfig FLDrive;
    public static MotorConfig BLDrive;
    public static MotorConfig FRDrive;
    public static MotorConfig BRDrive;
    public static CRServoConfig CarouselCR1;
    public static CRServoConfig CarouselCR2;
    public static CRServoConfig CarouselCR3;
    public static AnalogInput CarouselENC1;
    public static AnalogInput CarouselENC2;
    public static AnalogInput CarouselENC3;
    HardwareMap hardwareMap;

    NextFTCOpMode opMode;

    public RobotConfig(NextFTCOpMode opMode) {
        this.opMode = opMode;
        this.hardwareMap = opMode.hardwareMap;
        playerOne = new ReadyPlayerOne(opMode.gamepadManager.getGamepad1());
        playerTwo = new ReadyPlayerTwo(opMode.gamepadManager.getGamepad2());
        sensitivities = new Sensitivities();

        initHardware(hardwareMap);
    }

    public void gamepadUpdates() {
        playerOne.update_all();
        playerTwo.update_all();
    }
    public static void initHardware (HardwareMap hardwareMap) {
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

        CarouselENC1 = hardwareMap.get(AnalogInput.class, "CarENC1");
        CarouselENC2 = hardwareMap.get(AnalogInput.class, "CarENC2");
        CarouselENC3 = hardwareMap.get(AnalogInput.class, "CarENC3");

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
    }
}
