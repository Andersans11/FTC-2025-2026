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
@Autonomous(name = "Red - Far")
public class Far_Red extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeClose, scoreClose, intakeMedium, scoreMedium, intakeGate, scoreGate, hitGate, intakeFar, scoreFar, intakeGate1, intakeGate2, scoreGate1;
    SequentialGroupFixed preloads, closes, middles, fars, gates, hpZone, hpZone2;
    ArrayList<SequentialGroupFixed> commands;
    Timer pathTimer, opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeTime = 1.5;
    public static double intakeYClose = 84;
    public static double intakeYMedium = 60;
    public static double intakeYFar = 36;
    public static double intakeEndPos1 = 144 - 24;
    public static double intakeEndPos2 = 144 - 26;
    public static double intakeEndPos3 = 10.5;
    public static double shootTime = 0.925;

    public static double gateX = 14.5;
    public static double gateY = 58.75;
    public static double gateHeading = 30;

    boolean doingMiddles = false;

    public static double startDelay = 1;
    public static double shootPower = 1800;
    public static double turretAngle = -70;
    SequentialGroupFixed routines;

    Pose currentPose;

    boolean isRunning = false;

    public Far_Red() {
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

        Pose scoring = new Pose(64, 18, Math.toRadians(0)).mirror();
        Pose intakeStartClose = new Pose(44, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeEndClose = new Pose(8, intakeYClose, Math.toRadians(0)).mirror();
        Pose intakeStartMedium = new Pose(44, intakeYMedium, Math.toRadians(0)).mirror();
        Pose intakeEndMedium = new Pose(20, 68, Math.toRadians(0)).mirror();
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(0)).mirror();
        Pose intakeEndFar = new Pose(0, intakeYFar, Math.toRadians(0)).mirror();
        Pose gate = new Pose(gateX, gateY, Math.toRadians(gateHeading)).mirror();
        Pose gateIntake3 = new Pose(11, 9, Math.toRadians(0)).mirror();

        Selene.INSTANCE.initFollower(hardwareMap, new Pose(65, 15, Math.toRadians(0)).mirror());

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

        intakeGate1 = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(36, 9).mirror()))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .addPath(new BezierLine(new Pose(36, 9).mirror(), gateIntake3))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .addParametricCallback(0.5, () -> follower.setMaxPower(0.75))
                .build();

        intakeGate2 = follower.pathBuilder()
                .addPath(new BezierLine(scoring, new Pose(36, 27).mirror()))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .addPath(new BezierLine(new Pose(36, 27).mirror(), gateIntake3))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .addParametricCallback(0.5, () -> follower.setMaxPower(0.75))
                .build();

        scoreGate1 = follower.pathBuilder()
                .addPath(new BezierLine(gateIntake3, scoring))
                .setLinearHeadingInterpolation(gateIntake3.getHeading(), scoring.getHeading())
                .build();

        follower.setStartingPose(new Pose(65, 15, Math.toRadians(0)).mirror());

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                new InstantCommand(() -> follower.followPath(scorePreloads)),
                Turret.INSTANCE.setHoodPosition(0.25),
                Shooter.INSTANCE.setGoal(shootPower),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setPosition(turretAngle),
                new Delay(startDelay),
                Selene.INSTANCE.shootMotif()
        );

        preloads.setName("preloads");

        middles = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeMedium)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreMedium)),
                new WaitUntil(() -> follower.getCurrentTValue() >= shootTime),
                Selene.INSTANCE.shootMotif()
        );

        hpZone = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate1)),
                new Delay(1.75),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new InstantCommand(() -> follower.followPath(scoreGate1)),
                new Delay(1),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> follower.getCurrentTValue() >= shootTime),
                Selene.INSTANCE.shootMotif()
        );

        hpZone2 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate2)),
                new Delay(2.25),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new InstantCommand(() -> follower.followPath(scoreGate1)),
                new Delay(1),
                Selene.INSTANCE.stopIntake(),
                new WaitUntil(() -> follower.getCurrentTValue() >= shootTime),
                Selene.INSTANCE.shootMotif()
        );

        hpZone.setName("hpZone");
        hpZone2.setName("hpZone 2");

        middles.setName("middle");

        gates = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new Delay(intakeTime),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> follower.getCurrentTValue() >= shootTime),
                Selene.INSTANCE.shootMotif()
        );

        gates.setName("gate");

        closes = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeClose)),
                new WaitUntil(() -> follower.getPose().getX() <= intakeEndPos2 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreClose)),
                new WaitUntil(() -> follower.getCurrentTValue() >= shootTime),
                Selene.INSTANCE.shootMotif()
        );

        closes.setName("close");

        fars = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeFar)),
                new WaitUntil(() -> follower.getPose().getX() <= intakeEndPos1 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreFar)),
                new WaitUntil(() -> follower.getCurrentTValue() >= shootTime),
                Selene.INSTANCE.shootMotif()
        );

        fars.setName("far");

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
        P1.dpadUp().whenBecomesTrue(() -> commands.add(hpZone));
        P1.dpadDown().whenBecomesTrue(() -> commands.add(hpZone2));

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            Turret.INSTANCE.setBlueAlliance().schedule();
            scorePreloads = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .setGlobalDeceleration()
                    .build();
        }));
        Turret.INSTANCE.setHoodPosition(0.25).schedule();
        Selene.INSTANCE.stopIntake().schedule();
        Shooter.INSTANCE.StopperClose().schedule();
        Turret.INSTANCE.setPosition(turretAngle).schedule();

        routines = new SequentialGroupFixed(preloads);
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
