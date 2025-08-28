package org.firstinspires.ftc.teamcode.enceladus.DemoBotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.DemoRobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DemoBot.DemoBotDrive;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DemoBot.DemoBotLifter;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Lifter Test", group = OpModeGroups.TESTING)
@Disabled
public class LifterTest extends NextFTCOpMode {
    public LifterTest() {super(DemoBotLifter.INSTANCE);}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    DemoRobotConfig demoRobotConfig;
    DemoBotDrive demoBotDrive;

    @Override public void onInit() {
        demoRobotConfig = new DemoRobotConfig(this);
        demoBotDrive = new DemoBotDrive(this, demoRobotConfig);
        DemoBotLifter.INSTANCE.initSystem(demoRobotConfig);
    }
    @Override public void onStartButtonPressed() {
        DemoBotLifter.INSTANCE.map(demoRobotConfig.playerOne.Triangle, DemoBotLifter.Mappings.COLLECT);
        DemoBotLifter.INSTANCE.map(demoRobotConfig.playerOne.Square, DemoBotLifter.Mappings.DEPOSIT);
        DemoBotLifter.INSTANCE.map(demoRobotConfig.playerOne.Circle, DemoBotLifter.Mappings.HOLD);

        demoBotDrive.Start();
    }
    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        demoBotDrive.updateDrive(deltaTimeNano);
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        demoRobotConfig.gamepadUpdates();
    }
}