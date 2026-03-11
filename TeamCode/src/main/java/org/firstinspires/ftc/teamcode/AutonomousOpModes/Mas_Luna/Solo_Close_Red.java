package org.firstinspires.ftc.teamcode.AutonomousOpModes.Mas_Luna;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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

@Configurable
@Autonomous(name = "Solo Red - Close")
public class Solo_Close_Red extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeClose, scoreClose, intakeMedium, scoreMedium, intakeGate, scoreGate, hitGate, intakeFar, scoreFar;

    SequentialGroupFixed preloads, gates1, gates2, gates3, spikes;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 1.5;
    public static double gateEndTime = 17.5;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 60;
    public static double intakeYFar = 36;
    public static double intakeEndPos1 = 26;
    public static double intakeEndPos2 = 26;

    public static double gateX = 14.5;
    public static double gateY = 58.75;
    public static double gateHeading = 30;

    Pose currentPose;

    boolean isDone = false;

    public Solo_Close_Red() {
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

        Pose scoring = new Pose(64, 88, Math.toRadians(0)).mirror();
        Pose intakeStartClose = new Pose(44, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeEndClose = new Pose(8, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeStartMedium = new Pose(44, intakeYMedium, Math.toRadians(0)).mirror();
        Pose intakeEndMedium = new Pose(20, 68, Math.toRadians(0)).mirror();
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(0)).mirror();
        Pose intakeEndFar = new Pose(0, intakeYFar, Math.toRadians(0)).mirror();
        Pose gate = new Pose(gateX, gateY, Math.toRadians(gateHeading)).mirror();

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

        intakeClose = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartClose, intakeEndClose))
                .setTimeoutConstraint(100)
                .build();
        intakeMedium = follower.pathBuilder()
                .addPath(new BezierCurve(intakeStartMedium, new Pose(24, 60).mirror(), intakeEndMedium))
                .setConstantHeadingInterpolation(intakeStartMedium.getHeading())
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        hitGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(30, 72).mirror()))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .build();

        intakeGate = follower.pathBuilder()
                .addPath(new BezierCurve(scoring, intakeStartMedium, gate))
                .setLinearHeadingInterpolation(scoring.getHeading(), gate.getHeading())
                .setGlobalDeceleration()
                .build();

        scoreGate = follower.pathBuilder()
                .addPath(new BezierCurve(gate, new Pose(scoring.getX(), gate.getY()), scoring))
                .setHeadingInterpolation(
                    HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(
                            0,
                            .5,
                            HeadingInterpolator.linear(gate.getHeading(), scoring.getHeading())
                        ),
                        new HeadingInterpolator.PiecewiseNode(
                            .5,
                            1,
                            HeadingInterpolator.constant(scoring.getHeading())
                        )
                    )
                )
                .setGlobalDeceleration()
                .build();

        follower.setStartingPose(new Pose(65, 87, Math.toRadians(0)).mirror());

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(1500),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setPosition(42.5),
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.5),
                Selene.INSTANCE.shootMotif(),
                Selene.INSTANCE.intake(),
                Shooter.INSTANCE.setHoodPos(0.1),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates1 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates2 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates3 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        spikes = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> follower.getPose().getX() <= intakeEndPos2 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> follower.getPose().getX() <= intakeEndPos1 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> follower.followPath(hitGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> isDone = true)
        );

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            Turret.INSTANCE.setBlueAlliance().schedule();
            scorePreloads = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .setGlobalDeceleration()
                    .build();
        }));
        Shooter.INSTANCE.setHoodPos(0.0).schedule();
        Selene.INSTANCE.stopIntake().schedule();
        Shooter.INSTANCE.StopperClose().schedule();
        Turret.INSTANCE.setPosition(0).schedule();
    }

    @Override
    public void onWaitForStart() {
        currentPose = follower.getPose();
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        super.telemetryManager.update(telemetry);
        follower.updatePose();
        Turret.INSTANCE.periodic();
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
                        pathState = 6;
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
                        pathState = 6;
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
