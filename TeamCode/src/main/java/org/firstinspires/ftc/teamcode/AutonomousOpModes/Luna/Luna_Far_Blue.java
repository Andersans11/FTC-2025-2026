package org.firstinspires.ftc.teamcode.AutonomousOpModes.Luna;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.PoseTrackingTurret;

import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Luna Blue - Close")
public class Luna_Far_Blue extends RoyallyFuckedUpMode {
    Follower follower;
    PathChain obelisk1, score1, interrim1, intake1, score2, interrim2, intake2, score3;
    Timer pathTimer;
    public static double pathPower = 0.75;
    public static double intakePower = 0.2;

    public static double intakeStartPos1 = 36;
    public static double intakeStartPos2 = 60;
    public static double intakeEndPos1 = 17;
    public static double intakeEndPos2 = 10;

    Pose currentPose;

    public Luna_Far_Blue() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Artemis.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        follower = Constants.createFollower(hardwareMap);

        pathTimer = new Timer();

        Drawing.init();

        Pose scoring = new Pose(60, 12, Math.toRadians(90));
        Pose obelisk = new Pose(60, 60, Math.toRadians(90));
        Pose intakeStart1 = new Pose(44, intakeStartPos1, Math.toRadians(180));
        Pose intakeEnd1 = new Pose(intakeEndPos1, intakeStartPos1, Math.toRadians(180));
        Pose intakeStart2 = new Pose(44, intakeStartPos2, Math.toRadians(180));
        Pose intakeEnd2 = new Pose(intakeEndPos2, intakeStartPos2, Math.toRadians(180));

        Artemis.INSTANCE.initFollower(hardwareMap, scoring);

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(obelisk, scoring))
                .setLinearHeadingInterpolation(obelisk.getHeading(), scoring.getHeading())
                .build();
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

        follower.setStartingPose(obelisk);

        follower.setMaxPower(pathPower);

        Magazine.INSTANCE.setMode(0).schedule();
        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            obelisk1 = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), obelisk))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), obelisk.getHeading())
                    .build();
        }));
        Artemis.INSTANCE.stopIntake().schedule();
        PoseTrackingTurret.INSTANCE.setBlueAlliance();
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
        new SequentialGroup(
                Artemis.INSTANCE.stopIntake(),
                new InstantCommand(() -> follower.followPath(obelisk1)),
                new WaitUntil(() -> !follower.isBusy()),
                PoseTrackingTurret.INSTANCE.setPosition(0),
                new WaitUntil(() -> PoseTrackingTurret.INSTANCE.hasGotMotif),
                PoseTrackingTurret.INSTANCE.setTracking(),
                new InstantCommand(()-> follower.followPath(score1)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> pathTimer.resetTimer()),
                Magazine.INSTANCE.setMode(0),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 0.5),
                Magazine.INSTANCE.setMode(1),
                Artemis.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 0),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> follower.followPath(interrim1)),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.intake(),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake1);
                }),
                new WaitUntil(() -> !follower.isBusy() || pathTimer.getElapsedTimeSeconds() >= 10),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 2.5 || Magazine.INSTANCE.mode == 1),
                Magazine.INSTANCE.fillSlots(),
                Artemis.INSTANCE.stopIntake(),
                new InstantCommand(() -> {
                    follower.setMaxPower(pathPower);
                    follower.followPath(score2);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 0),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> follower.followPath(interrim2)),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.intake(),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake2);
                }),
                new WaitUntil(() -> !follower.isBusy() || pathTimer.getElapsedTimeSeconds() >= 10),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 5 || Magazine.INSTANCE.mode == 1),
                Magazine.INSTANCE.fillSlots(),
                Artemis.INSTANCE.stopIntake()
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
