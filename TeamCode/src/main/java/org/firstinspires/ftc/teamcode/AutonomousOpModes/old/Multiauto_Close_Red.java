package org.firstinspires.ftc.teamcode.AutonomousOpModes.old;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.utility.InstantCommand;

@Disabled
@Configurable
@Autonomous(name = "Gate Red - Close")
public class Multiauto_Close_Red extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, interrimClose, intakeClose, scoreClose, interrimMedium, intakeMedium, scoreMedium, intakeGate, scoreGate, interrimFar, intakeFar, scoreFar;

    SequentialGroupFixed preloads, gates1, gates2, gates3, spikes;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 1;
    public static double gateEndTime = 17.5;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 60;
    public static double intakeYFar = 48;
    public static double intakeEndPos1 = 20;
    public static double intakeEndPos2 = 12;

    Pose currentPose;

    boolean isDone = false;

    public Multiauto_Close_Red() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        follower = Constants.createFollower(hardwareMap);

        opTimer = new Timer();

        Drawing.init();

        Pose scoring = new Pose(60, 84, Math.toRadians(45)).mirror();
        Pose intakeStartClose = new Pose(44, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeEndClose = new Pose(intakeEndPos1, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeStartMedium = new Pose(44, intakeYMedium, Math.toRadians(0)).mirror();
        Pose intakeEndMedium = new Pose(intakeEndPos2, intakeYMedium, Math.toRadians(0)).mirror();
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(0)).mirror();
        Pose intakeEndFar = new Pose(intakeEndPos2, intakeYFar, Math.toRadians(0)).mirror();
        Pose gate = new Pose(36, 72, Math.toRadians(0)).mirror(); // TODO: tune this point

        Selene.INSTANCE.initFollower(hardwareMap, scoring);

        scoreClose = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndClose, scoring))
                .setLinearHeadingInterpolation(intakeEndClose.getHeading(), scoring.getHeading())
                .setGlobalDeceleration()
                .build();
        scoreMedium = follower.pathBuilder()
                .addPath(new BezierCurve(intakeEndMedium, intakeStartMedium, scoring))
                .setLinearHeadingInterpolation(intakeEndMedium.getHeading(), scoring.getHeading())
                .setGlobalDeceleration()
                .build();
        scoreFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndFar, scoring))
                .setLinearHeadingInterpolation(intakeEndFar.getHeading(), scoring.getHeading())
                .setGlobalDeceleration()
                .build();

        interrimClose = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartClose))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartClose.getHeading())
                .setGlobalDeceleration()
                .build();
        interrimMedium = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartMedium))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartMedium.getHeading())
                .setGlobalDeceleration()
                .build();
        interrimFar = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartFar))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartMedium.getHeading())
                .setGlobalDeceleration()
                .build();

        intakeClose = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartClose, intakeEndClose))
                .setGlobalDeceleration()
                .setTimeoutConstraint(100)
                .build();
        intakeMedium = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartMedium, intakeEndMedium))
                .setGlobalDeceleration()
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setGlobalDeceleration()
                .setTimeoutConstraint(100)
                .build();

        intakeGate = follower.pathBuilder()
                .addPath(new BezierCurve(scoring, new Pose(scoring.getX(), gate.getY()), gate))
                .setLinearHeadingInterpolation(scoring.getHeading(), gate.getHeading())
                .setGlobalDeceleration()
                .build();

        scoreGate = follower.pathBuilder()
                .addPath(new BezierCurve(gate, new Pose(scoring.getX(), gate.getY()), scoring))
                .setLinearHeadingInterpolation(gate.getHeading(), scoring.getHeading())
                .setGlobalDeceleration()
                .build();

        follower.setStartingPose(scoring);

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(1400),
                Shooter.INSTANCE.setHoodPos(0.0),
                Selene.INSTANCE.intakeOff(),
                Turret.INSTANCE.setPosition(0),
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.5),
                Selene.INSTANCE.shootMotif(),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(interrimMedium)),
                new WaitUntil(() -> follower.isBusy()),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> follower.atParametricEnd()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates1 = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intakeOn(),
                new Delay(intakeTime),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates2 = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intakeOn(),
                new Delay(intakeTime),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates3 = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intakeOn(),
                new Delay(intakeTime),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        spikes = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(interrimClose)),
                new WaitUntil(() -> follower.isBusy()),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> follower.atParametricEnd()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> follower.followPath(interrimFar)),
                new WaitUntil(() -> follower.isBusy()),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> follower.atParametricEnd()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            scorePreloads = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .setGlobalDeceleration()
                    .build();
        }));
        Shooter.INSTANCE.setHoodPos(0.0).schedule();
        Selene.INSTANCE.intakeOff().schedule();
        Shooter.INSTANCE.StopperClose().schedule();
    }

    @Override
    public void onWaitForStart() {
        currentPose = follower.getPose();
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        super.telemetryManager.update(telemetry);
        follower.updatePose();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        opTimer.resetTimer();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();

        currentPose = follower.getPose();
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        addData("opTimer", opTimer.getElapsedTimeSeconds());


        Drawing.drawDebug(follower);

        switch (pathState) {
            case 0:
                preloads.schedule();
                pathState = 1;
                break;
            case 1:
                if (isDone) {
                    isDone = false;
                    gates1.schedule();
                    pathState = 2;
                }
                break;
            case 2:
                if (isDone) {
                    isDone = false;
                    if (opTimer.getElapsedTimeSeconds() >= gateEndTime) {
                        spikes.schedule();
                        pathState = 5;
                    } else {
                        gates2.schedule();
                        pathState = 3;
                    }
                }
                break;
            case 3:
                if (isDone) {
                    isDone = false;
                    if (opTimer.getElapsedTimeSeconds() >= gateEndTime) {
                        spikes.schedule();
                        pathState = 5;
                    } else {
                        gates3.schedule();
                        pathState = 4;
                    }
                }
                break;
            case 4:
                if (isDone) {
                    isDone = false;
                    spikes.schedule();
                    pathState = 5;
                }
                break;
            case 5:
                // Idle
                break;
        }
    }

    @Override
    public void onStop() {
    }
}
