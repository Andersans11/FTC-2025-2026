package org.firstinspires.ftc.teamcode.DemoBotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.DemoRobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotLauncher;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot.DemoBotPivot;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Launcher Test", group = OpModeGroups.TESTING)
//@Disabled
public class LauncherTest extends NextFTCOpMode {
    public LauncherTest() {super(DemoBotPivot.INSTANCE, DemoBotLauncher.INSTANCE);}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    DemoRobotConfig demoRobotConfig;
    DemoBotDrive demoBotDrive;

    @Override public void onInit() {
        demoRobotConfig = new DemoRobotConfig(this);
        demoBotDrive = new DemoBotDrive(this, demoRobotConfig);
        DemoBotPivot.INSTANCE.initSystem(demoRobotConfig);
        DemoBotLauncher.INSTANCE.initSystem(demoRobotConfig);
    }
    @Override public void onStartButtonPressed() {
        DemoBotLauncher.INSTANCE.map(demoRobotConfig.playerOne.RightTrigger, DemoBotLauncher.Mappings.SHOOT);
        DemoBotLauncher.INSTANCE.map(demoRobotConfig.playerOne.RightBumper, DemoBotLauncher.Mappings.OUTTAKE);

        DemoBotPivot.INSTANCE.map(demoRobotConfig.playerOne.DpadUp, DemoBotPivot.Mappings.UP);
        DemoBotPivot.INSTANCE.map(demoRobotConfig.playerOne.DpadDown, DemoBotPivot.Mappings.DOWN);
        DemoBotPivot.INSTANCE.map(demoRobotConfig.playerOne.DpadLeft, DemoBotPivot.Mappings.NORMAL);

        demoBotDrive.Start();
    }
    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        demoBotDrive.updateDrive(deltaTimeNano);
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        demoRobotConfig.gamepadUpdates();
    }
}