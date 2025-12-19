package org.firstinspires.ftc.teamcode.AutonomousOpModes.Old;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;

@Autonomous(name = "Drive Tuning")
public class DriveTuning extends RoyallyFuckedUpMode {
    Follower follower;

    int outcomeState = 0;

    PathChain path, path2;

    Pose startingPose;
    Pose scoringPose;

    public DriveTuning() {
        super();
    }

    @Override
    public void onInit() {
        super.onInit();
        follower = Constants.createFollower(hardwareMap);

        startingPose = new Pose(0, 0);
        scoringPose = new Pose(-50, 0);

        path = follower.pathBuilder()
                .addPath(new Path(new BezierLine(startingPose, scoringPose)))
                .setConstantHeadingInterpolation(0)
                .build();
        path2 = follower.pathBuilder()
                .addPath(new Path(new BezierLine(scoringPose, startingPose)))
                .setConstantHeadingInterpolation(0)
                .build();
        follower.setStartingPose(startingPose);
    }

    @Override
    public void onWaitForStart() {
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        follower.followPath(path);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        switch (outcomeState) {
            case 0:
                if (!follower.isBusy()) {
                    follower.followPath(path2);
                    outcomeState = 1;
                }
                break;
            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(path);
                    outcomeState = 0;
                }
        }
        follower.update();
    }
}
