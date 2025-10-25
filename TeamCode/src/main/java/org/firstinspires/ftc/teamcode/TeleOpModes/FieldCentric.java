package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.FieldCentricDrive;

import dev.nextftc.core.components.SubsystemComponent;

@TeleOp(name = "Basic FC Drive", group = Utils.TESTING)
public class FieldCentric extends RoyallyFuckedUpMode {


    public FieldCentric() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(FieldCentricDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
