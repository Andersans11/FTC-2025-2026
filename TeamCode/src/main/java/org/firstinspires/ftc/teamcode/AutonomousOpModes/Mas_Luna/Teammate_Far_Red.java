package org.firstinspires.ftc.teamcode.AutonomousOpModes.Mas_Luna;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
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

import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Red - Far")
public class Teammate_Far_Red extends RRoboticsOpMode {
    Follower follower;
    PathChain scorePreloads, intakeGate, scoreGate, intakeSpike, scoreSpike;

    SequentialGroupFixed preloads, gates1, gates2, gates3, gates4, gates5, gates6;
    Timer opTimer;

    public int pathState = 0;
    public static double pathPower = 1;
    public static double intakeYFar = 36;
    public static double intakeEndPos2 = 0;

    Pose currentPose;

    boolean isDone = false;

    public Teammate_Far_Red() {
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

        Pose scoring = new Pose(64, 18, Math.toRadians(180));
        Pose intakeStartFar = new Pose(44, intakeYFar, Math.toRadians(180));
        Pose intakeEndFar = new Pose(intakeEndPos2, intakeYFar, Math.toRadians(180));
        Pose gateIntake3 = new Pose(12, 12, Math.toRadians(270));


        Selene.INSTANCE.initFollower(hardwareMap, scoring);

        scoreSpike = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndFar, scoring))
                .setLinearHeadingInterpolation(intakeEndFar.getHeading(), scoring.getHeading())
                .setGlobalDeceleration()
                .build();

        intakeSpike = follower.pathBuilder()
                .addPath(new BezierLine(intakeStartFar, intakeEndFar))
                .setTimeoutConstraint(100)
                .build();

        intakeGate = follower.pathBuilder()
                .addPath(new BezierLine(scoring, gateIntake3))
                .setConstantHeadingInterpolation(scoring.getHeading())
                .addParametricCallback(0.5, () -> follower.setMaxPower(0.75))
                .addParametricCallback(1, () -> follower.setMaxPower(1))
                .build();

        scoreGate = follower.pathBuilder()
                .addPath(new BezierLine(gateIntake3, scoring))
                .setLinearHeadingInterpolation(gateIntake3.getHeading(), scoring.getHeading())
                .build();

        follower.setStartingPose(new Pose(60, 84, Math.toRadians(180)));

        follower.setMaxPower(pathPower);

        preloads = new SequentialGroupFixed(
                Shooter.INSTANCE.setGoal(2000),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setPosition(-64),
                Selene.INSTANCE.shootMotif(),
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeSpike)),
                new WaitUntil(() -> follower.getPose().getX() <= 12 && follower.getPose().getX() >= 1),
                Selene.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(scoreSpike)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates1 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates2 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates3 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates4 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates5 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
                Selene.INSTANCE.shootMotif(),
                new InstantCommand(() -> isDone = true)
        );

        gates6 = new SequentialGroupFixed(
                Selene.INSTANCE.intake(),
                new InstantCommand(() -> follower.followPath(intakeGate)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> follower.followPath(scoreGate)),
                new WaitUntil(() -> Turret.INSTANCE.isInZone(follower.getPose())),
                Selene.INSTANCE.stopIntake(),
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
                    gates2.schedule();
                    pathState = 3;
                }
            case 3:
                if (isDone) {
                    isDone = false;
                    gates3.schedule();
                    pathState = 4;
                }
            case 4:
                if (isDone) {
                    isDone = false;
                    gates4.schedule();
                    pathState = 5;
                }
            case 5:
                if (isDone) {
                    isDone = false;
                    gates5.schedule();
                    pathState = 6;
                }
            case 6:
                if (isDone) {
                    isDone = false;
                    gates6.schedule();
                    pathState = 7;
                }
            case 7:
                // Idle
                break;
        }
    }

    @Override
    public void onStop() {
    }
}
