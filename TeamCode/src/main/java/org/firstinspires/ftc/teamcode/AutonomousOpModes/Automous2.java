package org.firstinspires.ftc.teamcode.AutonomousOpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.callbacks.ParametricCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.pedrojson.Callbacks;

import PedroJSON.main.PathLoader;

@Autonomous(name = "Automous2")
public class Automous2 extends RoyallyFuckedUpMode {
    Follower follower;

    int outcomeState = 0;

    PathChain path;

    Pose startingPose;
    Pose scoringPose;

    public Automous2() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        telemetry.addLine("1");
        follower = Constants.createFollower(hardwareMap);
        telemetry.addLine("1");
        Turret.INSTANCE.setPosition(-90).schedule();
        telemetry.addLine("1");
        Turret.INSTANCE.initPoseUpdater(this);

        startingPose = new Pose(0, 0);
        scoringPose = new Pose(-50, 0);

        Magazine.INSTANCE.setSlotContent(0, Utils.ArtifactTypes.PURPLE);
        Magazine.INSTANCE.setSlotContent(1, Utils.ArtifactTypes.PURPLE);
        Magazine.INSTANCE.setSlotContent(2, Utils.ArtifactTypes.GREEN);

        path = follower.pathBuilder()
                .addPath(new Path(new BezierLine(startingPose, scoringPose)))
                .setConstantHeadingInterpolation(90)
                .addParametricCallback(0.2, Turret.INSTANCE.AutoControl())
                .addParametricCallback(1, Perseus.INSTANCE.shootMotif())
                .build();
        follower.setStartingPose(startingPose);
    }

    @Override
    public void onWaitForStart() {
        telemetry.update();
        Turret.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        follower.followPath(path);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();
        telemetry.addData("0", Magazine.INSTANCE.getSlotColor(0));
        telemetry.addData("1", Magazine.INSTANCE.getSlotColor(1));
        telemetry.addData("2", Magazine.INSTANCE.getSlotColor(2));
        telemetry.addData("Active", Magazine.INSTANCE.activeSlot);
        telemetry.addData("Mode", Magazine.INSTANCE.mode);
        telemetry.addData("desiredColor", Magazine.INSTANCE.desiredColor);
    }
}
