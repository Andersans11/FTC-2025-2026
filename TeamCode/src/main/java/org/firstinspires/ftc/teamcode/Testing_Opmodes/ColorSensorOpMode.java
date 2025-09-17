package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils;


@TeleOp(name = "Color Sensor", group = Utils.TESTING)
//@Disabled
public class ColorSensorOpMode extends NextFTCOpMode {

    int sensorargb = 0;
    @Override public void onInit() {
        RobotConfig.initConfig(this);
    }
    @Override public void onStartButtonPressed() {
    }
    @Override public void onUpdate() {
        sensorargb = RobotConfig.Slot1CS.argb();
        telemetry.addData("argb", sensorargb);

        RobotConfig.gamepadUpdates();
    }
}
