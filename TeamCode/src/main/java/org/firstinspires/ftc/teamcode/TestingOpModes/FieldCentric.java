package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.FieldCentricDrive;

@Disabled
@TeleOp(name = "Basic FC Drive", group = Utils.TESTING)
public class FieldCentric extends RRoboticsOpMode {


    public FieldCentric() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(FieldCentricDrive.INSTANCE)
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
