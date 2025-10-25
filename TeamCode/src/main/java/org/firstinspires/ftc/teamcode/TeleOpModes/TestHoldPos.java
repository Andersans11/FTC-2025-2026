package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

@TeleOp(name = "Test: Hold Position", group = Utils.TESTING)
public class TestHoldPos extends OpMode {
    Follower follower;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
    }

    @Override
    public void start() {
        follower.holdPoint(new Pose(0, 0, 0));
    }

    @Override
    public void loop() {
        follower.update();
    }
}
