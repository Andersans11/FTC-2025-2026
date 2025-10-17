package org.firstinspires.ftc.teamcode.Testing_Opmodes;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.ftc.NextFTCOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
@TeleOp(name = "Color Sensor", group = Utils.TESTING)
public class ColorSensorOpMode extends NextFTCOpMode {

    @Override public void onInit() {
        RobotConfig.initConfig(this);
    }
    @Override public void onStartButtonPressed() {}
    @Override public void onUpdate() {

        telemetry.addData("argb", RobotConfig.IntakeCS.argb());

    }
}