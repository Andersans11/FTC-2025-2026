package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricCorrectingDrive;

@Disabled
@TeleOp(name = "correct Drive", group = Utils.WORKING)
public class CorrectionMode extends RRoboticsOpMode { // jurnlgsjtgf

    public CorrectionMode() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricCorrectingDrive.INSTANCE)
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
