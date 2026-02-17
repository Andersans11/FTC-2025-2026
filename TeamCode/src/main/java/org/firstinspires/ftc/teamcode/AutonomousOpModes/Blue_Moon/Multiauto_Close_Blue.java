package org.firstinspires.ftc.teamcode.AutonomousOpModes.Blue_Moon;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Multiauto Blue - Close")
public class Multiauto_Close_Blue extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, interrimClose, intakeClose, scoreClose, interrimMedium, intakeMedium, scoreMedium, intakeGate, scoreGate, interrimFar, intakeFar, scoreFar;

    SequentialGroupFixed preloads, gates, spikes;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 0.75;
    public static double intakeTime = 2.5;
    public static double gateEndTime = 20;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 60;
    public static double intakeYFar = 48;
    public static double intakeEndPos1 = 17;
    public static double intakeEndPos2 = 9;

    Pose currentPose;

    public Multiauto_Close_Blue() {
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

        Pose scoring = new Pose(60, 84, Math.toRadians(135));
        Pose intakeStartClose = new Pose(44, intakeYClose, Math.toRadians(180));
        Pose intakeEndClose = new Pose(intakeEndPos1, intakeYClose, Math.toRadians(180));
        Pose intakeStartMedium = new Pose(44, intakeYMedium, Math.toRadians(180));
        Pose intakeEndMedium = new Pose(intakeEndPos2, intakeYMedium, Math.toRadians(180));
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(180));
        Pose intakeEndFar = new Pose(intakeEndPos2, intakeYFar, Math.toRadians(180));
        Pose gate = new Pose(36, 72, Math.toRadians(0)); // TODO: tune this point

        Selene.INSTANCE.initFollower(hardwareMap, scoring);

        scoreClose = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndClose, scoring))
                .setLinearHeadingInterpolation(intakeEndClose.getHeading(), scoring.getHeading())
                .build();
        scoreMedium = follower.pathBuilder()
                .addPath(new BezierCurve(intakeEndMedium, intakeStartMedium, scoring))
                .setLinearHeadingInterpolation(intakeEndMedium.getHeading(), intakeStartMedium.getHeading())
                .build();
        scoreFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndFar, scoring))
                .setLinearHeadingInterpolation(intakeEndFar.getHeading(), intakeStartFar.getHeading())
                .build();

        interrimClose = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartClose))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartClose.getHeading())
                .build();
        interrimMedium = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartMedium))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartMedium.getHeading())
                .build();
        interrimFar = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartFar))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartMedium.getHeading())
                .build();

        intakeClose = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartClose, intakeEndClose))
                .build();
        intakeMedium = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartMedium, intakeEndMedium))
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .build();

        intakeGate = follower.pathBuilder()
                .addPath(new BezierCurve(scoring, new Pose(scoring.getX(), gate.getY()), gate))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .build();

        scoreGate = follower.pathBuilder()
                .addPath(new BezierCurve(gate, new Pose(scoring.getX(), gate.getY()), scoring))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .build();

        follower.setStartingPose(scoring);

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.5),
                Selene.INSTANCE.shootMotif(),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(interrimMedium)),
                new WaitUntil(() -> follower.isBusy()),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif()
        );

        gates = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intake(),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif()
        );

        spikes = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(interrimClose)),
                new WaitUntil(() -> follower.isBusy()),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> follower.followPath(interrimFar)),
                new WaitUntil(() -> follower.isBusy()),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif()
        );

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            scorePreloads = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .build();
        }));
        Selene.INSTANCE.stopIntake().schedule();
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
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();

        currentPose = follower.getPose();
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));


        Drawing.drawDebug(follower);

        switch (pathState) {
            case 0:
                preloads.schedule();
                pathState = 1;
                break;
            case 1:
                if (preloads.isDone()) {
                    gates.schedule();
                    pathState = 2;
                }
                break;
            case 2:
                if (gates.isDone()) {
                    if (opTimer.getElapsedTimeSeconds() >= gateEndTime) {
                        preloads.schedule();
                        pathState = 3;
                    } else {
                        gates.schedule();
                    }
                }
                break;
            case 3:
                // Idle
                break;
        }
    }
}
