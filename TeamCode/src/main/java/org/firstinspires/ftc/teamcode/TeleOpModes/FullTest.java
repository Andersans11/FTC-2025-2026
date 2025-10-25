package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "perseus throws an artifact and hits eddie in the face", group = Utils.PRIORITY)
public class FullTest extends NextFTCOpMode {

    public FullTest() {
        super();
        addComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new SubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        RobotConfig.initConfig(this, new DeltaTimer());
        Perseus.INSTANCE.setManualControl(true);
        driveTrainBinds();
    }

    @Override
    public void onStartButtonPressed() {
        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.intake());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
