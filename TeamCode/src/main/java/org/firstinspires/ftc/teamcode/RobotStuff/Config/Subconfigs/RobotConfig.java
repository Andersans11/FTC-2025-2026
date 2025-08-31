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

    public static MotorConfig SwFLDrive;
    public static MotorConfig SwBLDrive;
    public static MotorConfig SwFRDrive;
    public static MotorConfig SwBRDrive;

    public static CRServoConfig SwFLPivot;
    public static CRServoConfig SwBLPivot;
    public static CRServoConfig SwFRPivot;
    public static CRServoConfig SwBRPivot;

    public static AnalogInput SwFLEnc;
    public static AnalogInput SwBLEnc;
    public static AnalogInput SwFREnc;
    public static AnalogInput SwBREnc;

    public static MotorConfig FLDrive;
    public static MotorConfig BLDrive;
    public static MotorConfig FRDrive;
    public static MotorConfig BRDrive;
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
        SwFLDrive = new MotorConfig(
                hardwareMap,
                "SwFLDrive",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        SwBLDrive = new MotorConfig(
                hardwareMap,
                "SwBLDrive",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        SwFRDrive = new MotorConfig(
                hardwareMap,
                "SwFRDrive",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        SwBRDrive = new MotorConfig(
                hardwareMap,
                "SwBRDrive",
                DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
        SwFLPivot = new CRServoConfig(
                hardwareMap,
                "SwFLPivot",
                DcMotorSimple.Direction.FORWARD
        );
        SwBLPivot = new CRServoConfig(
                hardwareMap,
                "SwBLPivot",
                DcMotorSimple.Direction.FORWARD
        );
        SwFRPivot = new CRServoConfig(
                hardwareMap,
                "SwFRPivot",
                DcMotorSimple.Direction.FORWARD
        );
        SwBRPivot = new CRServoConfig(
                hardwareMap,
                "SwBRPivot",
                DcMotorSimple.Direction.FORWARD
        );
        SwFLEnc = hardwareMap.get(AnalogInput.class, "SwFLEnc");
        SwBLEnc = hardwareMap.get(AnalogInput.class, "SwFLEnc");
        SwFREnc = hardwareMap.get(AnalogInput.class, "SwFLEnc");
        SwBREnc = hardwareMap.get(AnalogInput.class, "SwFLEnc");

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
    }
}
