package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;

@Disabled
@TeleOp(name = "Lock Yaw", group = Utils.TESTING)
public class HoldHeadingTest extends RRoboticsOpMode {

    public HoldHeadingTest() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    Follower follower;

    @Override
    public void onInit() {
        super.onInit();
        follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        P1.leftBumper().whenBecomesTrue(() -> follower.holdPoint(new Pose(0, 0, Math.toRadians(0))));
        P1.rightBumper().whenBecomesTrue(() -> follower.holdPoint(new Pose(0, 0, Math.toRadians(180))));
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();
    }
}