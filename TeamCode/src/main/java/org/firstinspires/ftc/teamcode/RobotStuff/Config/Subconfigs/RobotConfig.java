package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

@Config
public class RobotConfig {

    public final ReadyPlayerOne playerOne;
    public final ReadyPlayerTwo playerTwo;
    public final Sensitivities sensitivities = new Sensitivities();

    NextFTCOpMode opMode;

    public RobotConfig(NextFTCOpMode opMode) {
        this.opMode = opMode;
        playerOne = new ReadyPlayerOne(opMode.gamepad1);
        playerTwo = new ReadyPlayerTwo(opMode.gamepad2);
    }

    public static MotorConfig FLDrive = new MotorConfig("Front Left Drive", DcMotorSimple.Direction.REVERSE);
    public static MotorConfig BLDrive = new MotorConfig("Back Left Drive", DcMotorSimple.Direction.REVERSE);
    public static MotorConfig FRDrive = new MotorConfig("Front Right Drive", DcMotorSimple.Direction.FORWARD);
    public static MotorConfig BRDrive = new MotorConfig("Back Right Drive", DcMotorSimple.Direction.FORWARD);


}
