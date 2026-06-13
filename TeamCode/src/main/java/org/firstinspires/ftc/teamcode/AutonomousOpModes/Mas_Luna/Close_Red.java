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
@Autonomous(name = "Red - Close")
public class Close_Red extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeClose, scoreClose, intakeMedium, scoreMedium, intakeGate, scoreGate, hitGate, intakeFar, scoreFar;

    SequentialGroupFixed preloads, closes, middles, fars, gates, hitGates;
    ArrayList<SequentialGroupFixed> commands;
    SequentialGroupFixed routines;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 1.0;
    public static double gateEndTime = 17.5;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 60;
    public static double intakeYFar = 36;
    public static double intakeEndPos1 = 144 - 20;
    public static double intakeEndPos2 = 144 - 26;

    public static double gateX = 129.5;
    public static double gateY = 58.75;
    public static double gateHeading = 30;

    public static double shootPower = 1500;
    public static double preloadDiff = 25;
    public static double gateDiff = 0;
    public static double closeDiff = 0;
    public static double middleDiff = -50;
    public static double farDiff = -25;
    public static double turretPos = 40;
    public static double middleTurretPos = 40;

    boolean doingMiddles = false;

    Pose currentPose;

    boolean isRunning = false;

    public static double startDelay = 0.5;

    public Close_Red() {
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

        Pose scoring = new Pose(144 - 54, 78, Math.toRadians(0));
        Pose intakeStartClose = new Pose(144 - 44, intakeYClose, Math.toRadians(0));
        Pose intakeEndClose = new Pose(144 - 8, intakeYClose, Math.toRadians(0));
        Pose intakeStartMedium = new Pose(144 - 44, intakeYMedium, Math.toRadians(0));
        Pose intakeEndMedium = new Pose(144 - 20, 68, Math.toRadians(0));
        Pose intakeStartFar = new Pose(144 - 44, intakeYFar, Math.toRadians(0));
        Pose intakeEndFar = new Pose(144 - 0, intakeYFar, Math.toRadians(0));
        Pose gate = new Pose(gateX, gateY, Math.toRadians(gateHeading));

        Selene.INSTANCE.initFollower(hardwareMap, new Pose(144 - 65, 87, Math.toRadians(0)));

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
                .addPath(new BezierCurve(intakeStartMedium, new Pose(144 - 22, 60), intakeEndMedium))
                .setConstantHeadingInterpolation(intakeStartMedium.getHeading())
                .setTimeoutConstraint(100)
                .build();
        intakeFar = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        hitGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(144 - 36, 72)))
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

        follower.setStartingPose(new Pose(144 - 65, 87, Math.toRadians(0)));

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
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                Turret.INSTANCE.setPosition(turretPos)
        );

        middles.setName("middles");

        gates = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + gateDiff),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif()
        );

        gates.setName("gates");

        closes = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + closeDiff),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif()
        );

        closes.setName("closes");

        fars = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(shootPower + farDiff),
                Selene.INSTANCE.intakeOn(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intakeOff(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif()
        );

        fars.setName("fars");

        hitGates = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(hitGate)),
                new WaitUntil(() -> !follower.isBusy())
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
        Turret.INSTANCE.setHoodPosition(0.0).schedule();
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


        Drawing.drawDebug(follower);
    }

    @Override
    public void onStop() {
    }
}
