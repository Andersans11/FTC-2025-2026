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

    public static MotorConfig FLDrive = new MotorConfig(
            "Front Left Drive",
            DcMotorSimple.Direction.REVERSE
    );
    public static MotorConfig BLDrive = new MotorConfig(
            "Back Left Drive",
            DcMotorSimple.Direction.REVERSE
    );
    public static MotorConfig FRDrive = new MotorConfig(
            "Front Right Drive",
            DcMotorSimple.Direction.FORWARD
    );
    public static MotorConfig BRDrive = new MotorConfig(
            "Back Right Drive",
            DcMotorSimple.Direction.FORWARD
    );


}
