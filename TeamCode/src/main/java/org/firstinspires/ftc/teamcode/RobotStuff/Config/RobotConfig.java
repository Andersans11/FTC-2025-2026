package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfig.ReadyPlayerOne;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfig.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfig.ReadyPlayerTwo;

@Config
public class RobotConfig {

    public final ReadyPlayerOne p1;
    public final ReadyPlayerTwo p2;
    public final Sensitivities sensitivities = new Sensitivities();

    OpMode opMode;

    public RobotConfig(OpMode opMode) {
        this.opMode = opMode;
        p1 = new ReadyPlayerOne(opMode.gamepad1);
        p2 = new ReadyPlayerTwo(opMode.gamepad2);
    }

}
