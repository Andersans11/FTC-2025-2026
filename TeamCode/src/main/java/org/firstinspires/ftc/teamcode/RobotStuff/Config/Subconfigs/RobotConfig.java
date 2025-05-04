package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.MotorConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.HardwareConfigs.ServoConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Presets.DepositArmPreset;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Presets.FullPreset;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Presets.HorizontalSystemPreset;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Presets.PickupClawPreset;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Presets.VerticalSystemPreset;

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
        public static FullPreset RestingPos = new FullPreset(
                new HorizontalSystemPreset(0, new PickupClawPreset(0, 0, 0)),
                new VerticalSystemPreset(0, new DepositArmPreset(0, 0, 0))
        );
        public static PickupClawPreset PickupFromSub = new PickupClawPreset(0.5, 0, 0.25);
        public static PickupClawPreset PickupFromWall = new PickupClawPreset(0.25, 0, 0.25);
        // ALWAYS IN MM: MORE EXACT
        public static VerticalSystemPreset DepositToHighBar = new VerticalSystemPreset(107, new DepositArmPreset(0.5, 0.5, 0));
        public static VerticalSystemPreset DepositToLowBar = new VerticalSystemPreset(0, new DepositArmPreset(0.5, 0.55, 0));
//    public static VerticalSystemPreset DepositToHighBasket = new VerticalSystemPreset();
//    public static VerticalSystemPreset DepositToLowBasket = new VerticalSystemPreset();
}
