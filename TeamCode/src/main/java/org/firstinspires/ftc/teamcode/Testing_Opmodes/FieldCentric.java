package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.FieldCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Basic FC Drive")
public class FieldCentric extends NextFTCOpMode {

    long deltaTime;
    DeltaTimer deltaTimer;
    FieldCentricDrive fieldCentricDrive;

    public FieldCentric() {
        addComponents(
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        RobotConfig.initConfig(this);
        fieldCentricDrive = new FieldCentricDrive(this);
    }

    @Override
    public void onStartButtonPressed() {
        fieldCentricDrive.schedule();
    }

    @Override
    public void onUpdate() {
        deltaTime = deltaTimer.getDelta();

        fieldCentricDrive.update(deltaTime);
    }
}
