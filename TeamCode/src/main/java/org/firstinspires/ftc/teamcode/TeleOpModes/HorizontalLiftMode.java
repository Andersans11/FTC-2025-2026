package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets.HorizontalLiftPresets;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Horizontal", group = OpModeGroups.TESTING)
//@Disabled
public class HorizontalLiftMode extends NextFTCOpMode {

    public HorizontalLiftMode() {
        super(HorizontalLift.INSTANCE, HorizontalLiftPresets.INSTANCE);
    }
    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
        HorizontalLift.INSTANCE.initSystem(robotConfig, this);
    }

    @Override
    public void onStartButtonPressed() {
        robotCentricDrive.Start();

        robotConfig.playerTwo.DpadUp.setPressedCommand(HorizontalLiftPresets.INSTANCE::maximum);
        robotConfig.playerTwo.DpadDown.setPressedCommand(HorizontalLiftPresets.INSTANCE::minimum);
        robotConfig.playerTwo.DpadRight.setPressedCommand(HorizontalLiftPresets.INSTANCE::mid);
    }



    @Override
    public void onUpdate() {

        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));
        robotCentricDrive.updateDrive(deltaTimeNano);

        telemetry.update();

        robotConfig.gamepadUpdates();
    }
}
