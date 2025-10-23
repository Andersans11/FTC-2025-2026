package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.FieldCentricDrive;

import dev.nextftc.core.components.SubsystemComponent;

@TeleOp(name = "Basic FC Drive")
public class FieldCentric extends RoyallyFuckedUpMode {


    public FieldCentric() {
        super();
        addComponents(
                new SubsystemComponent(FieldCentricDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        driveTrainBinds();
    }

    @Override
    public void onStartButtonPressed() {}

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
