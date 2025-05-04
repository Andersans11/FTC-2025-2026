package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.MotorConfig;

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
    public static MotorConfig TurretRotationMotor;

    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;


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
        TurretRotationMotor = new MotorConfig(
                hardwareMap,
                "Turret",
                Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE
        );
    }
}
