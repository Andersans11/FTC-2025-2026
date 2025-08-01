package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.CommandManager;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets.HorizontalLiftPresets;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.VerticalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Full Test", group = OpModeGroups.UNUSABLE_DUETOHARDWARE)
@Disabled
public class EverythingMode extends NextFTCOpMode {

    public EverythingMode() {
        super(VerticalLift.INSTANCE, HorizontalLift.INSTANCE);
    }
    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
        VerticalLift.INSTANCE.initSystem(robotConfig, this);
        HorizontalLift.INSTANCE.initSystem(robotConfig, this);
    }

    @Override
    public void onStartButtonPressed() {
        robotCentricDrive.Start();

        // more presets here

        robotConfig.playerTwo.DpadUp.setPressedCommand(HorizontalLiftPresets.INSTANCE::maximum);
        robotConfig.playerTwo.DpadDown.setPressedCommand(HorizontalLiftPresets.INSTANCE::minimum);
        robotConfig.playerTwo.DpadRight.setPressedCommand(HorizontalLiftPresets.INSTANCE::mid);
    }

    @Override
    public void onUpdate() {

        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));
        robotCentricDrive.updateDrive(deltaTimeNano);

        telemetry.addData("Lift current pos:", VerticalLift.INSTANCE.ticksToInches(VerticalLift.INSTANCE.motors.getLeader().getCurrentPosition(), 336));

        telemetry.update();

        robotConfig.gamepadUpdates();
    }
}
