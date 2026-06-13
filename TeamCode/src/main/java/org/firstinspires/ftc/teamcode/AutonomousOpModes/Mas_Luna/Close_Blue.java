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
public class Close_Blue extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeClose, scoreClose, intakeMedium, scoreMedium, intakeGate, scoreGate, hitGate, intakeFar, scoreFar;

    SequentialGroupFixed preloads, closes, middles, fars, gates, hitGates;
    ArrayList<SequentialGroupFixed> commands;
    SequentialGroupFixed routines;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 2.0;
    public static double gateEndTime = 17.5;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 56;
    public static double intakeYFar = 36;
    public static double intakeEndPos1 = 14;
    public static double intakeEndPos2 = 22;

    public static double gateX = 14.5;
    public static double gateY = 58.75;
    public static double gateHeading = 150;

    public static double shootPower = 1450;
    public static double preloadDiff = 75;
    public static double gateDiff = 0;
    public static double closeDiff = 0;
    public static double middleDiff = 0;
    public static double farDiff = 0;
    public static double turretPos = -55;
    public static double middleTurretPos = -55;
    public static double distCheck = 2;
    public static double hoodPosPreloads = 0.2;

    boolean doingMiddles = false;

    Pose currentPose;

    boolean isRunning = false;

    public static double startDelay = 0.5;

    public Close_Blue() {
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

        Pose scoring = new Pose(52, 80, Math.toRadians(180));
        Pose intakeStartClose = new Pose(44, intakeYClose, Math.toRadians(180));
        Pose intakeEndClose = new Pose(intakeEndPos2, intakeYClose, Math.toRadians(180));
        Pose intakeStartMedium = new Pose(44, intakeYMedium, Math.toRadians(180));
        Pose intakeEndMedium = new Pose(19, 66, Math.toRadians(180));
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(180));
        Pose intakeEndFar = new Pose(intakeEndPos1, intakeYFar, Math.toRadians(180));
        Pose gate = new Pose(gateX, gateY, Math.toRadians(gateHeading));

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
                .addPath(new BezierCurve(intakeStartMedium, new Pose(20, 60), new Pose(24, 66), intakeEndMedium))
                .setConstantHeadingInterpolation(intakeStartMedium.getHeading())
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        hitGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(36, 72)))
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

        follower.setStartingPose(new Pose(65, 87, Math.toRadians(180)));

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + preloadDiff),
                Selene.INSTANCE.intakeOff(),
                Turret.INSTANCE.setPosition(turretPos),
                new Delay(startDelay),
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                new Delay(0.5),
                Selene.INSTANCE.shootMotif(),
                Turret.INSTANCE.setHoodPosition(0.1)
        );

        preloads.setName("preloads");

        middles = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + middleDiff),
                Selene.INSTANCE.intakeOn(),
                Turret.INSTANCE.setPosition(middleTurretPos),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> follower.atParametricEnd()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif(),
                Turret.INSTANCE.setPosition(turretPos)
        );

        middles.setName("middles");

        gates = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + gateDiff),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> isDonePathing()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> follower.getCurrentTValue() >= 0.75),
                Selene.INSTANCE.intakeOff(),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        gates.setName("gates");

        closes = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + closeDiff),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.shootMotif()
        );

        closes.setName("closes");

        fars = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + farDiff),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> isDonePathing()),
                Selene.INSTANCE.intakeOff(),
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
        Selene.INSTANCE.intakeOff().schedule();
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
