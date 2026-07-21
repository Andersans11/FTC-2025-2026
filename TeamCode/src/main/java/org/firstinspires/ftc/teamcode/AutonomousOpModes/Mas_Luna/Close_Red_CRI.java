package org.firstinspires.ftc.teamcode.AutonomousOpModes.Mas_Luna;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import java.util.ArrayList;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Red - Close CRI")
public class Close_Red_CRI extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeClose, scoreClose, intakeMedium, scoreMedium, intakeGate, scoreGate, hitGate, intakeFar, scoreFar;

    SequentialGroupFixed preloads, closes, middles, fars, gates, hitGates;
    ArrayList<SequentialGroupFixed> commands;
    SequentialGroupFixed routines;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 1.75;
    public static double intakeYClose = 108;
    public static double intakeYMedium = 84;
    public static double intakeYFar = 60;
    public static double intakeEndPos = 12;

    public static double gateX = 13;
    public static double gateY = 108;
    public static double gateHeading = 45;

    public static double distCheck = 2;

    public static double closeTurretPos = 90;
    public static double middleTurretPos = 90;
    public static double farTurretPos = 90;
    public static double gateTurretPos = 90;

    boolean doingMiddles = false;

    public static double preloadPower = 175;
    public static double preloadHood = -7.5;

    public double otherPower = 100;
    public double otherHood = 0;

    Pose currentPose;

    boolean isRunning = false;
    double endTime = 0;

    public static double startDelay = 0.5;

    public Close_Red_CRI() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        commands = new ArrayList<>();

        follower = Constants.createFollower(hardwareMap);

        opTimer = new Timer();

        Drawing.init();

        Pose scoring = new Pose(192 - 67, 117, Math.toRadians(315));
        Pose intakeStartClose = new Pose(192 - 36, intakeYClose, Math.toRadians(180 - 180));
        Pose intakeEndClose = new Pose(192 - 16, 114, Math.toRadians(180 - 180));
        Pose intakeStartMedium = new Pose(192 - 36, intakeYMedium, Math.toRadians(180 - 180));
        Pose intakeEndMedium = new Pose(192 - intakeEndPos, intakeYMedium, Math.toRadians(180 - 180));
        Pose intakeStartFar = new Pose(192 - 36, intakeYFar, Math.toRadians(180 - 180));
        Pose intakeEndFar = new Pose(192 - intakeEndPos, intakeYFar, Math.toRadians(180 - 180));
        Pose gate = new Pose(192 - gateX, gateY, Math.toRadians(gateHeading));

        Selene.INSTANCE.initFollower(hardwareMap, scoring);

        scoreClose = follower.pathBuilder()
                .addPath(Utils.reverseTangentFromHeadings(new BezierLine(intakeEndClose, scoring), 0, 315))
                .setReversed()
                .setGlobalDeceleration()
                .build();
        scoreMedium = follower.pathBuilder()
                .addPath(Utils.reverseTangentFromHeadings(new BezierLine(intakeEndMedium, scoring), 0, 315))
                .setReversed()
                .setGlobalDeceleration()
                .build();
        scoreFar = follower.pathBuilder()
                .addPath(Utils.reverseTangentFromHeadings(new BezierLine(intakeEndFar, scoring), 0, 315))
                .setReversed()
                .setGlobalDeceleration()
                .build();

        intakeClose = follower.pathBuilder()
                .addPath(Utils.tangentFromHeadings(new BezierLine(scoring, intakeStartClose), 315, 0))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartClose.getHeading())
                .addPath(new BezierCurve(intakeStartClose, new Pose(192 - 22, 108), new Pose(192 - 26, 114), intakeEndClose))
                .setConstantHeadingInterpolation(intakeStartMedium.getHeading())
                .setTimeoutConstraint(100)
                .build();
        intakeMedium = follower.pathBuilder()
                .addPath(Utils.tangentFromHeadings(new BezierLine(scoring, intakeStartMedium), 315, 0))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartMedium.getHeading())
                .addPath(new BezierLine(intakeStartMedium, intakeEndMedium))
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(Utils.tangentFromHeadings(new BezierLine(scoring, intakeStartFar), 315, 0, 12, 24))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStartFar.getHeading())
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        hitGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(192 - 36, 120)))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .build();

        intakeGate = follower.pathBuilder()
                .addPath(Utils.tangentFromHeadings(new BezierLine(scoring, gate), 315, 30))
                .build();

        scoreGate = follower.pathBuilder()
                .addPath(Utils.reverseTangentFromHeadings(new BezierLine(gate, scoring), 30, 315))
                .setReversed()
                .setGlobalDeceleration()
                .build();

        follower.setStartingPose(new Pose(192 - 60, 132, Math.toRadians(180 - 180)));

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                new InstantCommand(() -> Shooter.powerMod = preloadPower),
                new InstantCommand(() -> Shooter.hoodMod = preloadHood),
                Turret.INSTANCE.setPosition(0),
                Selene.INSTANCE.stopIntake(),
                new Delay(startDelay),
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.75),
                new InstantCommand(() -> follower.setMaxPower(0.5)),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new InstantCommand(() -> Shooter.powerMod = otherPower),
                new InstantCommand(() -> Shooter.hoodMod = otherHood)
        );

        preloads.setName("preloads");

        middles = new SequentialGroupFixed(
                Turret.INSTANCE.setPosition(middleTurretPos),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> isDonePathing()),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new Delay(0.5),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        middles.setName("middles");

        gates = new SequentialGroupFixed(
                Turret.INSTANCE.setPosition(gateTurretPos),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> isDonePathing()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new Delay(0.5),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        gates.setName("gates");

        closes = new SequentialGroupFixed(
                Turret.INSTANCE.setPosition(closeTurretPos),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> isDonePathing()),
                new Delay(0.25),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new Delay(0.5),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        closes.setName("closes");

        fars = new SequentialGroupFixed(
                Turret.INSTANCE.setPosition(farTurretPos),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> follower.getPose().distanceFrom(intakeFar.endPoint()) <= distCheck),
                new WaitUntil(() -> isDonePathing()),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new Delay(0.5),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );



        fars.setName("fars");

        hitGates = new SequentialGroupFixed(
                new InstantCommand(() -> endTime = opTimer.getElapsedTimeSeconds()),
                new InstantCommand(() -> follower.followPath(hitGate)),
                new WaitUntil(() -> isDonePathing())
        );

        hitGates.setName("end");

        routines = new SequentialGroupFixed(preloads);

        P1.triangle().whenBecomesTrue(() -> {
            commands.add(closes);
            doingMiddles = true;
        });
        P1.square().whenBecomesTrue(() -> {
            if (!doingMiddles) {
                commands.add(closes);
                doingMiddles = true;
            }

            commands.add(gates);
        });
        P1.circle().whenBecomesTrue(() -> commands.add(middles));
        P1.cross().whenBecomesTrue(() -> commands.add(fars));

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            scorePreloads = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setHeadingInterpolation(HeadingInterpolator.piecewise(
                            new HeadingInterpolator.PiecewiseNode(0, 0.75,
                                    HeadingInterpolator.constant(Math.toRadians(45))),
                            new HeadingInterpolator.PiecewiseNode(0.75, 1,
                                    HeadingInterpolator.linear(Math.toRadians(45), scoring.getHeading()))
                    ))
                    .setGlobalDeceleration()
                    .build();
        }));
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
        for (int i = 0; i < commands.size(); i++) {
            addData("path " + (i + 1), commands.get(i).name());
        }
        super.telemetryManager.update(telemetry);
        follower.updatePose();
        Turret.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        opTimer.resetTimer();
        commands.add(hitGates);
        for (int i = 0; i < commands.size(); i++) {
            routines.add(commands.get(i));
        }
        routines.schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();

        currentPose = follower.getPose();
        addData("x", String.format("%.6s", currentPose.getX()));
        addData("y", String.format("%.6s", currentPose.getY()));
        addData("heading", String.format("%.6s", Math.toDegrees(currentPose.getHeading())));
        addData("opTimer", opTimer.getElapsedTimeSeconds());
        addData("velocity", follower.getVelocity().getMagnitude());
        addData("t", follower.getCurrentTValue());

        if (endTime != 0) {
            addData("done", endTime);
        }


        Drawing.drawDebug(follower);
    }

    public boolean isDonePathing() {
        return follower.getPose().distanceFrom(follower.getCurrentPathChain().endPoint()) <= distCheck || follower.atParametricEnd();
    }

    @Override
    public void onStop() {
    }
}
