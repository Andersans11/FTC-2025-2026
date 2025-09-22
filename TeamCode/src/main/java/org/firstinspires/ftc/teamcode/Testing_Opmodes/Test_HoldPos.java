package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.FConstantsTeleOp;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.LConstants;

@TeleOp(name = "Test: Hold Position")
public class Test_HoldPos extends OpMode {
    Follower follower;

    @Override
    public void init() {
        follower = new Follower(hardwareMap, FConstantsTeleOp.class, LConstants.class);
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
