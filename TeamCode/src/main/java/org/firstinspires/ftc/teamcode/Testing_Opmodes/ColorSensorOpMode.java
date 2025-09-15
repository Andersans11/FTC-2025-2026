package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Basic Bitch OpMode", group = Utils.WORKING)
//@Disabled
public class ColorSensorOpMode extends NextFTCOpMode {
    public ColorSensorOpMode() {super();}

    RobotConfig robotConfig;

    int sensorargb = 0;
    @Override public void onInit() {
        robotConfig = new RobotConfig(this);
    }
    @Override public void onStartButtonPressed() {
    }
    @Override public void onUpdate() {
        sensorargb = robotConfig.Slot1CS.argb();
        telemetry.addData("argb", sensorargb);

        robotConfig.gamepadUpdates();
    }
}
