package org.firstinspires.ftc.teamcode.AutonomousOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Automous Ultra Pro Max Masters Edition Plus - Blue")
public class Automous3_Kisen extends RoyallyFuckedUpMode {
    Follower follower;

    PathChain path1;

    Timer pathTimer;

    Pose startingPose;
    Pose pose1, pose2, pose3;

    public Automous3_Kisen() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        follower = Constants.createFollower(hardwareMap);

        pathTimer = new Timer();

        Turret.INSTANCE.initPoseUpdater(follower);

        Drawing.init();


        startingPose = new Pose(0,0, Math.toRadians(0));
        pose1 = new Pose(24,0);
        pose2 = new Pose(24,24);
        pose3 = new Pose(0,24);

        path1 = follower.pathBuilder()
                .addPath(new BezierLine(startingPose, pose1))
                .addPath(new BezierLine(pose1, pose2))
                .addPath(new BezierLine(pose2, pose3))
                .addPath(new BezierLine(pose3, startingPose))
                .build();


        follower.setMaxPower(0.75);

    }

    @Override
    public void onWaitForStart() {
        telemetry.update();
        Turret.INSTANCE.periodic();
        Magazine.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        new SequentialGroup(
                new InstantCommand(()-> {
                    follower.followPath(path1);
                })
        ).schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();

        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("Is Follower Busy", follower.isBusy());


        Drawing.drawDebug(follower);
    }
}
