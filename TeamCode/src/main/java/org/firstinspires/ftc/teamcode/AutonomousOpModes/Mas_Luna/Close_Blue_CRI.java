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

import java.util.ArrayList;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Blue - Close")
public class Close_Blue_CRI extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeClose, scoreClose, intakeMedium, scoreMedium, intakeGate, scoreGate, hitGate, intakeFar, scoreFar;

    SequentialGroupFixed preloads, closes, middles, fars, gates, hitGates;
    ArrayList<SequentialGroupFixed> commands;
    SequentialGroupFixed routines;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 2.5;
    public static double gateEndTime = 17.5;
    public static double intakeYClose = 108;
    public static double intakeYMedium = 84;
    public static double intakeYFar = 60;
    public static double intakeEndPos = 12;

    public static double gateX = 14.5;
    public static double gateY = 58.75 + 48;
    public static double gateHeading = 150;

    public static double shootPower = 1400;
    public static double preloadDiff = 50;
    public static double gateDiff = 0;
    public static double closeDiff = 0;
    public static double middleDiff = 0;
    public static double farDiff = 0;
    public static double turretPos = -53.5;
    public static double middleTurretPos = -53.5;
    public static double distCheck = 2;
    public static double hoodPosPreloads = 0.0;

    boolean doingMiddles = false;

    Pose currentPose;

    boolean isRunning = false;

    public static double startDelay = 0.5;

    public Close_Blue_CRI() {
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

        Pose scoring = new Pose(60, 132, Math.toRadians(180));
        Pose intakeStartClose = new Pose(43, intakeYClose, Math.toRadians(180));
        Pose intakeEndClose = new Pose(19, 114, Math.toRadians(180));
        Pose intakeStartMedium = new Pose(43, intakeYMedium, Math.toRadians(180));
        Pose intakeEndMedium = new Pose(intakeEndPos, intakeYMedium, Math.toRadians(180));
        Pose intakeStartFar = new Pose(43, intakeYFar, Math.toRadians(180));
        Pose intakeEndFar = new Pose(intakeEndPos, intakeYFar, Math.toRadians(180));
        Pose gate = new Pose(gateX, gateY, Math.toRadians(gateHeading));

        Selene.INSTANCE.initFollower(hardwareMap, scoring);

        scoreClose = follower.pathBuilder()
                .addPath(new BezierCurve(intakeEndClose, intakeStartClose, scoring))
                .setReversed()
                .setGlobalDeceleration()
                .build();
        scoreMedium = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndMedium, scoring))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.9,
                                HeadingInterpolator.tangent.reverse()),
                        new HeadingInterpolator.PiecewiseNode(0.9, 1,
                                HeadingInterpolator.constant(Math.toRadians(180)))
                ))
                .setGlobalDeceleration()
                .build();
        scoreFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndFar, scoring))
                .setHeadingInterpolation(HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(0, 0.9,
                                HeadingInterpolator.tangent.reverse()),
                        new HeadingInterpolator.PiecewiseNode(0.9, 1,
                                HeadingInterpolator.constant(Math.toRadians(180)))
                ))
                .setGlobalDeceleration()
                .build();

        intakeClose = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartClose))
                .addPath(new BezierCurve(intakeStartClose, new Pose(20, 60), new Pose(24, 66), intakeEndClose))
                .setConstantHeadingInterpolation(intakeStartMedium.getHeading())
                .setTimeoutConstraint(100)
                .build();
        intakeMedium = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartMedium))
                .addPath(new BezierLine(intakeStartMedium, intakeEndMedium))
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStartFar))
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        hitGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(36, 96)))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .build();

        intakeGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(24, gate.getY())))
                .addPath(new BezierCurve(scoring, intakeStartMedium, gate))
                .setConstantHeadingInterpolation(gate.getHeading())
                .build();

        scoreGate = follower.pathBuilder()
                .addPath(new BezierCurve(gate, new Pose(scoring.getX(), gate.getY()), scoring))
                .setGlobalDeceleration()
                .build();

        follower.setStartingPose(new Pose(65, 87, Math.toRadians(180)));

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + preloadDiff),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setPosition(turretPos),
                new Delay(startDelay),
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.75),
                Selene.INSTANCE.shootMotif(),
                Turret.INSTANCE.setHoodPosition(0.0)
        );

        preloads.setName("preloads");

        middles = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + middleDiff),
                Selene.INSTANCE.intake(),
                Turret.INSTANCE.setPosition(middleTurretPos),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> isDonePathing()),
                new Delay(1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif(),
                Turret.INSTANCE.setPosition(turretPos)
        );

        middles.setName("middles");

        gates = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + gateDiff),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> isDonePathing()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> follower.getCurrentTValue() >= 0.75),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        gates.setName("gates");

        closes = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + closeDiff),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        closes.setName("closes");

        fars = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + farDiff),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> follower.getPose().distanceFrom(intakeFar.endPoint()) <= distCheck),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );



        fars.setName("fars");

        hitGates = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(hitGate)),
                new WaitUntil(() -> isDonePathing())
        );

        hitGates.setName("end");

        routines = new SequentialGroupFixed(preloads);

        P1.triangle().whenBecomesTrue(() -> commands.add(closes));
        P1.square().whenBecomesTrue(() -> {
            if (!doingMiddles) {
                commands.add(middles);
                doingMiddles = true;
            }

            commands.add(gates);
        });
        P1.circle().whenBecomesTrue(() -> {
            commands.add(middles);
            doingMiddles = true;
        });
        P1.cross().whenBecomesTrue(() -> commands.add(fars));

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            Turret.INSTANCE.setBlueAlliance().schedule();
            scorePreloads = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .setGlobalDeceleration()
                    .build();
        }));
        Turret.INSTANCE.setHoodPosition(hoodPosPreloads).schedule();
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
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        addData("opTimer", opTimer.getElapsedTimeSeconds());
        addData("velocity", follower.getVelocity().getMagnitude());
        addData("t", follower.getCurrentTValue());


        Drawing.drawDebug(follower);
    }

    public boolean isDonePathing() {
        return follower.getPose().distanceFrom(follower.getCurrentPathChain().endPoint()) <= distCheck || follower.atParametricEnd();
    }

    @Override
    public void onStop() {
    }
}
