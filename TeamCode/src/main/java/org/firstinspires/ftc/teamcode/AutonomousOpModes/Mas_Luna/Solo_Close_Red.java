package org.firstinspires.ftc.teamcode.AutonomousOpModes.Mas_Luna;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
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
    PathChain scorePreloads, interrimClose, intakeClose, scoreClose, interrimMedium, intakeMedium, scoreMedium, hitGate, hitGate2, intakeGate1, intakeGate2, scoreGate1, scoreGate2, interrimFar, intakeFar, scoreFar;

    SequentialGroupFixed preloads, gates1, gates2, gates3, spikes;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 1;
    public static double gateEndTime = 20;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 60;
    public static double intakeYFar = 36;
    public static double intakeEndPos1 = 8;
    public static double intakeEndPos2 = 0;

    public static double gateX = 16;

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
        Pose scoring2 = new Pose(64, 18, Math.toRadians(0)).mirror();
        Pose intakeStartClose = new Pose(44, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeEndClose = new Pose(intakeEndPos1, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeStartMedium = new Pose(44, intakeYMedium, Math.toRadians(0)).mirror();
        Pose intakeEndMedium = new Pose(intakeEndPos2, intakeYMedium, Math.toRadians(0)).mirror();
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(0)).mirror();
        Pose intakeEndFar = new Pose(intakeEndPos2, intakeYFar, Math.toRadians(0)).mirror();
        Pose gate = new Pose(gateX, 72, Math.toRadians(0)).mirror(); // TODO: tune this point
        Pose gateIntake1 = new Pose(12, 54, Math.toRadians(270)).mirror();
        Pose gateIntake2 = new Pose(12, 24, Math.toRadians(270)).mirror();
        Pose gateIntake3 = new Pose(24, 12, Math.toRadians(270)).mirror();
        Pose gateInterrim = new Pose(28, 60).mirror();

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
                .setConstantHeadingInterpolation(intakeStartClose.getHeading())
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
                .setTimeoutConstraint(100)
                .build();
        intakeMedium = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartMedium, intakeEndMedium))
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        hitGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(scoring.getX(), gate.getY())))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .addPath(new BezierLine(new Pose(scoring.getX(), gate.getY()), gate))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .build();

        hitGate2 = follower.pathBuilder()
                .addPath(new BezierLine(scoring2, gate))
                .setConstantHeadingInterpolation(scoring2.getHeading())
                .addPath(new BezierLine(gate, new Pose(scoring.getX(), gate.getY())))
                .setConstantHeadingInterpolation(scoring2.getHeading())
                .setGlobalDeceleration()
                .build();

        intakeGate1 = follower.pathBuilder()
                .addPath(new BezierCurve(gate, gateInterrim, gateIntake1))
                .setLinearHeadingInterpolation(gate.getHeading(), gateIntake1.getHeading())
                .addPath(new BezierLine(gateIntake1, gateIntake2))
                .setConstantHeadingInterpolation(gateIntake1.getHeading())
                .addParametricCallback(0.5, () -> follower.setMaxPower(0.75))
                .addParametricCallback(1, () -> follower.setMaxPower(1))
                .build();
        intakeGate2 = follower.pathBuilder()
                .addPath(new BezierLine(scoring2, gateIntake3))
                .setConstantHeadingInterpolation(scoring2.getHeading())
                .addParametricCallback(0.5, () -> follower.setMaxPower(0.75))
                .addParametricCallback(1, () -> follower.setMaxPower(1))
                .build();

        scoreGate1 = follower.pathBuilder()
                .addPath(new BezierLine(gateIntake2, scoring2))
                .setLinearHeadingInterpolation(gateIntake2.getHeading(), scoring2.getHeading())
                .build();
        scoreGate2 = follower.pathBuilder()
                .addPath(new BezierLine(gateIntake3, scoring2))
                .setLinearHeadingInterpolation(gateIntake3.getHeading(), scoring2.getHeading())
                .build();

        follower.setStartingPose(new Pose(60, 84, Math.toRadians(0)).mirror());

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(1500),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setPosition(45),
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.5),
                Selene.INSTANCE.shootMotif(),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> follower.getPose().getX() <= 12 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> follower.getPose().getX() <= 12 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates1 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                Turret.INSTANCE.setPosition(-64),
                Shooter.INSTANCE.setGoal(2000),
                new InstantCommand(() -> follower.followPath(hitGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new Delay(0.5),
                new InstantCommand(() -> follower.followPath(scoreGate1)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates2 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate2)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate2)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates3 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate2)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate2)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        spikes = new SequentialGroupFixed(
                Turret.INSTANCE.setPosition(-45),
                Shooter.INSTANCE.setGoal(1500),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> follower.getPose().getX() <= 20 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
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
        Shooter.INSTANCE.setHoodPos(0.1).schedule();
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
