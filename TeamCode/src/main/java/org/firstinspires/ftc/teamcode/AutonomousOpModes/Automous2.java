package org.firstinspires.ftc.teamcode.AutonomousOpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;

import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

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

        Drawing.init();

        telemetry.addLine("1");
        follower = Constants.createFollower(hardwareMap);
        telemetry.addLine("1");
        Turret.INSTANCE.setPosition(-90).schedule();
        telemetry.addLine("1");
        Turret.INSTANCE.initPoseUpdater(this);

        startingPose = new Pose(72, 72);
        scoringPose = new Pose(10, 72);

        Magazine.INSTANCE.setSlotContent(0, Utils.ArtifactTypes.PURPLE).schedule();
        Magazine.INSTANCE.setSlotContent(1, Utils.ArtifactTypes.PURPLE).schedule();
        Magazine.INSTANCE.setSlotContent(2, Utils.ArtifactTypes.GREEN).schedule();

        path = follower.pathBuilder()
                .addPath(new Path(new BezierLine(startingPose, scoringPose)))
                .setConstantHeadingInterpolation(0)
                .build();
        follower.setStartingPose(startingPose);
        follower.setMaxPower(0.5);
        Shooter.INSTANCE.resetKicker().schedule();
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
                Perseus.INSTANCE.start(),
                new InstantCommand(() -> follower.followPath(path)),
                Magazine.INSTANCE.setMode(1),
                new WaitUntil(() -> follower.getCurrentTValue() >= 0.5),
                Turret.INSTANCE.setPosition(0),
                Turret.INSTANCE.autoControl(),
                new WaitUntil(() -> !follower.isBusy()),
                Perseus.INSTANCE.shootMotif()
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
        addData("shotsFired", Magazine.INSTANCE.shotsFired);
        addData("turretMode", Turret.INSTANCE.mode);
        addData("turret", Turret.INSTANCE.rotationMotor.getCurrentPosition());
        addData("target", Turret.INSTANCE.controller.getGoal());

        Drawing.drawDebug(follower);
    }
}
