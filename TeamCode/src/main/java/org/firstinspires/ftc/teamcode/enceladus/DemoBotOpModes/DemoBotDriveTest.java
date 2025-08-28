package org.firstinspires.ftc.teamcode.enceladus.DemoBotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.DemoRobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DemoBot.DemoBotDrive;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Demo Bot Drive", group = OpModeGroups.TESTING)
//@Disabled
public class DemoBotDriveTest extends NextFTCOpMode {
    public DemoBotDriveTest() {super();}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    DemoRobotConfig demoRobotConfig;
    DemoBotDrive demoBotDrive;

    @Override public void onInit() {
        demoRobotConfig = new DemoRobotConfig(this);
        demoBotDrive = new DemoBotDrive(this, demoRobotConfig);
    }
    @Override public void onStartButtonPressed() {
        demoBotDrive.Start();
    }
    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        demoBotDrive.updateDrive(deltaTimeNano);
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        demoRobotConfig.gamepadUpdates();
    }
}