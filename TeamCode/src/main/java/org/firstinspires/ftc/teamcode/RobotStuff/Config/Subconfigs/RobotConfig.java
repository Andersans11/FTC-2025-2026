package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

@Config
public class RobotConfig {

    public final ReadyPlayerOne playerOne;
    public final ReadyPlayerTwo playerTwo;
    public final Sensitivities sensitivities = new Sensitivities();
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;

    NextFTCOpMode opMode;

    public RobotConfig(NextFTCOpMode opMode) {
        this.opMode = opMode;
        playerOne = new ReadyPlayerOne(opMode.gamepadManager.getGamepad1());
        playerTwo = new ReadyPlayerTwo(opMode.gamepadManager.getGamepad2());
    }

    public void gamepadUpdates() {
        playerOne.update_all();
        playerTwo.update_all();
    }

    public static DriveMotorConfig FLDrive = new DriveMotorConfig(
            "Front Left Drive",
            DcMotorSimple.Direction.REVERSE
    );
    public static DriveMotorConfig BLDrive = new DriveMotorConfig(
            "Back Left Drive",
            DcMotorSimple.Direction.REVERSE
    );
    public static DriveMotorConfig FRDrive = new DriveMotorConfig(
            "Front Right Drive",
            DcMotorSimple.Direction.FORWARD
    );
    public static DriveMotorConfig BRDrive = new DriveMotorConfig(
            "Back Right Drive",
            DcMotorSimple.Direction.FORWARD
    );
    public static LiftMotorConfig LeftVertical = new LiftMotorConfig(
            "Left Vertical Lift",
            DcMotorSimple.Direction.FORWARD,
            336 // rev 6000 rpm with 12:1 gearbox
    );
    public static LiftMotorConfig RightVertical = new LiftMotorConfig( // one lift motor will always be the inverted direction of its counterpart
            "Right Vertical Lift",
            DcMotorSimple.Direction.REVERSE,
            336 // rev 6000 rpm with 12:1 gearbox
    );
    public static LiftMotorConfig LeftHorizontal = new LiftMotorConfig(
            "Left Horizontal Lift",
            DcMotorSimple.Direction.FORWARD,
            336 // rev 6000 rpm with 12:1 gearbox
    );
    public static LiftMotorConfig RightHorizontal = new LiftMotorConfig(
            "Right Horizontal Lift",
            DcMotorSimple.Direction.REVERSE,
            336 // rev 6000 rpm with 12:1 gearbox
    );


}
