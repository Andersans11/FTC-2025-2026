package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.MotorConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.ServoConfig;

@Config
public class DemoRobotConfig {

    public final ReadyPlayerOne playerOne;
    public final ReadyPlayerTwo playerTwo;

    public final Sensitivities sensitivities = new Sensitivities();
    NextFTCOpMode opMode;
    HardwareMap hardwareMap;

    public static MotorConfig LeftWheel;
    public static MotorConfig RightWheel;
    public static MotorConfig LauncherMotor;

    public static ServoConfig PivotServo;
    public static ServoConfig LifterServoLeft;
    public static ServoConfig LifterServoRight;

    public DemoRobotConfig(NextFTCOpMode opMode) {
        this.opMode = opMode;
        this.hardwareMap = opMode.hardwareMap;
        playerOne = new ReadyPlayerOne(opMode.gamepadManager.getGamepad1());
        playerTwo = new ReadyPlayerTwo(opMode.gamepadManager.getGamepad2());

        initHardware(hardwareMap);
    }

    public void gamepadUpdates() {
        playerOne.update_all();
        playerTwo.update_all();
    }

    public static void initHardware(HardwareMap hardwareMap) {

        LeftWheel = new MotorConfig(
                hardwareMap,
                "Left Wheel",
                Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        RightWheel = new MotorConfig(
                hardwareMap,
                "Right Wheel",
                Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        LauncherMotor = new MotorConfig(
                hardwareMap,
                "Launcher",
                Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.FLOAT
        );
        PivotServo = new ServoConfig(
                hardwareMap,
                "Pivot",
                Servo.Direction.FORWARD
        );
        LifterServoLeft = new ServoConfig(
                hardwareMap,
                "LifterL",
                Servo.Direction.FORWARD
        );
        LifterServoRight = new ServoConfig(
                hardwareMap,
                "LifterR",
                Servo.Direction.REVERSE
        );
    }
}
