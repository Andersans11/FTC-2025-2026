package org.firstinspires.ftc.teamcode.AutonomousOpModes.Luna;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.delays.WaitUntil;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Luna Red - Close")
public class Luna_9_Red extends RRoboticsOpMode {
    Follower follower;
    PathChain score1, interrim1, intake1, score2, interrim2, intake2, score3;
    Timer pathTimer;
    public static double pathPower = 0.75;
    public static double intakePower = 0.2;

    public static double intakeStartPos1 = 84;
    public static double intakeStartPos2 = 60;
    public static double intakeEndPos1 = 127;
    public static double intakeEndPos2 = 135;

    Pose currentPose;

    public Luna_9_Red() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        follower = Constants.createFollower(hardwareMap);

        pathTimer = new Timer();

        Drawing.init();

        Pose scoring = new Pose(84, 84, Math.toRadians(45));
        Pose intakeStart1 = new Pose(100, intakeStartPos1, Math.toRadians(0));
        Pose intakeEnd1 = new Pose(intakeEndPos1, intakeStartPos1, Math.toRadians(0));
        Pose intakeStart2 = new Pose(100, intakeStartPos2, Math.toRadians(0));
        Pose intakeEnd2 = new Pose(intakeEndPos2, intakeStartPos2, Math.toRadians(0));
        Pose gate = new Pose(108, 72, Math.toRadians(0));

        Selene.INSTANCE.initFollower(hardwareMap, scoring);

        score2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd1, scoring))
                .setLinearHeadingInterpolation(intakeEnd1.getHeading(), scoring.getHeading())
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd2, intakeStart2))
                .setLinearHeadingInterpolation(intakeEnd2.getHeading(), intakeStart2.getHeading())
                .build();

        interrim1 = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStart1))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStart1.getHeading())
                .build();
        interrim2 = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStart2))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStart2.getHeading())
                .build();

        intake1 = follower.pathBuilder()
                .addPath(new BezierLine(intakeStart1, intakeEnd1))
                .build();
        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeStart2, intakeEnd2))
                .build();

        follower.setStartingPose(scoring);

        follower.setMaxPower(pathPower);

        Magazine.INSTANCE.setMode(0).schedule();
        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() ->{
            score1 = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .build();
        }));
        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.dpadUp().whenBecomesTrue(Turret.INSTANCE.setRedAlliance());
        Selene.INSTANCE.stopIntake().schedule();
    }

    @Override
    public void onWaitForStart() {
        currentPose = follower.getPose();
        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("Is Follower Busy", follower.isBusy());
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        super.telemetryManager.update(telemetry);
        Magazine.INSTANCE.periodic();
        follower.updatePose();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        new SequentialGroupFixed(
                Selene.INSTANCE.stopIntake(),
                Shooter.INSTANCE.idle(),
                new InstantCommand(()-> follower.followPath(score1)),
                new WaitUntil(() -> !follower.isBusy()),
                Turret.INSTANCE.setPosition(45),
                new WaitUntil(() -> Turret.INSTANCE.hasGotMotif),
                Turret.INSTANCE.setTracking(),
                new InstantCommand(() -> pathTimer.resetTimer()),
                Magazine.INSTANCE.setMode(0),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 0.5),
                Magazine.INSTANCE.setMode(1),
                Selene.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 0),
                Turret.INSTANCE.setPosition(0),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> follower.followPath(interrim1)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intake(),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake1);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> !follower.isBusy() || pathTimer.getElapsedTimeSeconds() >= 10),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 2.5 || Magazine.INSTANCE.mode == 1),
                Magazine.INSTANCE.fillSlots(),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setTracking(),
                new InstantCommand(() -> {
                    follower.setMaxPower(pathPower);
                    follower.followPath(score2);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 0),
                Turret.INSTANCE.setPosition(0),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> follower.followPath(interrim2)),
                new WaitUntil(() -> !follower.isBusy()),
                Selene.INSTANCE.intake(),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake2);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> !follower.isBusy() || pathTimer.getElapsedTimeSeconds() >= 10),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 2.5 || Magazine.INSTANCE.mode == 1),
                Selene.INSTANCE.stopIntake(),
                Turret.INSTANCE.setTracking()
        ).schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();

        currentPose = follower.getPose();

        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("Is Follower Busy", follower.isBusy());
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));


        Drawing.drawDebug(follower);
    }
}
