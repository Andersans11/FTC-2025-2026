package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.ReadyPlayerOne;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.ReadyPlayerTwo;

@Config
public class RobotConfig {

    public final ReadyPlayerOne playerOne;
    public final ReadyPlayerTwo playerTwo;
    public final Sensitivities sensitivities = new Sensitivities();

    OpMode opMode;

    public RobotConfig(OpMode opMode) {
        this.opMode = opMode;
        playerOne = new ReadyPlayerOne(opMode.gamepad1);
        playerTwo = new ReadyPlayerTwo(opMode.gamepad2);
    }

}
