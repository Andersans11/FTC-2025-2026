package org.firstinspires.ftc.teamcode.AutonomousOpModes.Templates;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.utility.InstantCommand;

@Disabled
@Configurable
@Autonomous(name = "Automous - Template")
public class Automous_Template extends RRoboticsOpMode {
    Follower follower;

    PathChain path1;

    Timer pathTimer;

    Pose startingPose;
    Pose pose1, pose2, pose3;

    public Automous_Template() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        Selene.INSTANCE.initFollower(hardwareMap);

        follower = Constants.createFollower(hardwareMap);

        pathTimer = new Timer();

        Drawing.init();

        startingPose = new Pose(0,0, Math.toRadians(0));
        pose1 = new Pose(24,0);
        pose2 = new Pose(24,24);
        pose3 = new Pose(0,24);

        path1 = follower.pathBuilder()
                .addPath(new BezierLine(startingPose, pose1))
                .build();


        follower.setMaxPower(0.75);

    }

    @Override
    public void onWaitForStart() {
        telemetry.update();
        Turret.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        new SequentialGroupFixed(
                new InstantCommand(()-> {
                    follower.followPath(path1);
                })
        ).schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();


        Drawing.drawDebug(follower);
    }
}
