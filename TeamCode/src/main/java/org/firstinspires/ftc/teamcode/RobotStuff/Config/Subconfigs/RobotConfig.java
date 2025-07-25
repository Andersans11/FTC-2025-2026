package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.MotorConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.ServoConfig;

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
    public static MotorConfig LeftVertical;
    public static MotorConfig RightVertical;
    public static ServoConfig LeftHorizontal;
    public static ServoConfig RightHorizontal;
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
        LeftVertical = new MotorConfig(
                hardwareMap,
                "Left Vertical Lift",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE,
                336 // rev 6000 rpm with 12:1 gearbox
        );
        RightVertical = new MotorConfig( // one lift motor will always be the inverted direction of its counterpart
                hardwareMap,
                "Right Vertical Lift",
                DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE,
                336 // rev 6000 rpm with 12:1 gearbox
        );

        LeftHorizontal = new ServoConfig(
                hardwareMap,
                "Left Horizontal Lift",
                Servo.Direction.FORWARD
        );

        RightHorizontal = new ServoConfig(
                hardwareMap,
                "Right Horizontal Lift",
                Servo.Direction.REVERSE
        );
    }
}
