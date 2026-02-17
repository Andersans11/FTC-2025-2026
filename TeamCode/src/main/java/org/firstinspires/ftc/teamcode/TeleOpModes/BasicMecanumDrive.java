package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

@TeleOp(name = "Basic Mecanum Drive", group = Utils.WORKING)
public class BasicMecanumDrive extends RRoboticsOpMode { // jurnlgsjtgf

    public BasicMecanumDrive() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricDrive.INSTANCE)
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
