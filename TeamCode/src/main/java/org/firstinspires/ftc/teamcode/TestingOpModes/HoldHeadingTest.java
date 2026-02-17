package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;

@Disabled
@TeleOp(name = "Lock Yaw", group = Utils.TESTING)
public class HoldHeadingTest extends RRoboticsOpMode {

    public HoldHeadingTest() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(HoldHeadingPID.INSTANCE)
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
