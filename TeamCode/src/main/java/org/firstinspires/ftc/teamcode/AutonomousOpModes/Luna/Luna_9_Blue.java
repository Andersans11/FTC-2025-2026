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
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.IfElseCommand;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Luna Blue - Close")
public class Luna_9_Blue extends RoyallyFuckedUpMode {
    Follower follower;
    PathChain score1, interrim1, intake1, score2, interrim2, intake2, score3, endIntake1, endIntake2;
    Timer pathTimer;
    public static double pathPower = 0.75;
    public static double intakePower = 0.35;
    public static double moreIntakePower = 0.6;

    public static double intakeStartPos1 = 84;
    public static double intakeStartPos2 = 60;
    public static double intakeEndPos1 = 17;
    public static double intakeEndPos2 = 9;
    public static double intakeMidPos1 = 34;
    public static double intakeMidPos2 = 34;

    public static double shootPower = 1650;

    Pose currentPose;

    public Luna_9_Blue() {
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

        Pose scoring = new Pose(60, 84, Math.toRadians(135));
        Pose intakeStart1 = new Pose(44, intakeStartPos1, Math.toRadians(180));
        Pose intakeMid1 = new Pose(intakeMidPos1, intakeStartPos1, Math.toRadians(180));
        Pose intakeEnd1 = new Pose(intakeEndPos1, intakeStartPos1, Math.toRadians(180));
        Pose intakeStart2 = new Pose(44, intakeStartPos2, Math.toRadians(180));
        Pose intakeMid2 = new Pose(intakeMidPos2, intakeStartPos2, Math.toRadians(180));
        Pose intakeEnd2 = new Pose(intakeEndPos2, intakeStartPos2, Math.toRadians(180));
        Pose gate = new Pose(36, 72, Math.toRadians(0));

        Artemis.INSTANCE.initFollower(hardwareMap, scoring);

        score2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeMid1, scoring))
                .setLinearHeadingInterpolation(intakeMid1.getHeading(), scoring.getHeading())
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierLine(intakeMid2, intakeStart2))
                .setLinearHeadingInterpolation(intakeMid2.getHeading(), intakeStart2.getHeading())
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
                .addPath(new BezierLine(intakeStart1, intakeMid1))
                .build();
        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeStart2, intakeMid2))
                .build();

        endIntake1 = follower.pathBuilder()
                .addPath(new BezierLine(intakeMid1, intakeEnd1))
                .build();
        endIntake2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeMid2, intakeEnd2))
                .build();

        follower.setStartingPose(scoring);

        follower.setMaxPower(pathPower);


        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.stop());
        P1.leftBumper().whenBecomesTrue(new InstantCommand(() -> {
            score1 = follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), scoring))
                    .setLinearHeadingInterpolation(follower.getPose().getHeading(), scoring.getHeading())
                    .build();
        }));
        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.dpadUp().whenBecomesTrue(Turret.INSTANCE.setBlueAlliance());
        Artemis.INSTANCE.stopIntake().schedule();
    }

    public Command OuttakeThingy() {
        return new InstantCommand(() -> {
            if (Magazine.INSTANCE.mode == 1) Artemis.INSTANCE.outtake().schedule();
        });
    }

    @Override
    public void onWaitForStart() {
        currentPose = follower.getPose();
        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("Is Follower Busy", follower.isBusy());
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        addData("speed", Shooter.INSTANCE.shooters.getVelocity());
        super.telemetryManager.update(telemetry);
        Magazine.INSTANCE.periodic();
        follower.updatePose();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Shooter.INSTANCE.isAuto = true;
        new SequentialGroup(
                Turret.INSTANCE.setPosition(-45),
                Artemis.INSTANCE.start(),
                new InstantCommand(()-> follower.followPath(score1)),
                new WaitUntil(() -> !follower.isBusy()),
                new WaitUntil(() -> Turret.INSTANCE.hasGotMotif),
                Turret.INSTANCE.setTracking(),
                new InstantCommand(() -> pathTimer.resetTimer()),
                Magazine.INSTANCE.setMode(0),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 0.5),
                Magazine.INSTANCE.setMode(1),
                Magazine.INSTANCE.fillSlots(),
                Artemis.INSTANCE.shootMotif(shootPower),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 0),
                Turret.INSTANCE.setPosition(0),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.followPath(interrim1);
                    follower.setMaxPower(moreIntakePower);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.intake(),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake1);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() >= 1),
                new InstantCommand(() -> follower.followPath(endIntake1)),
                new WaitUntil(() -> !follower.isBusy() || pathTimer.getElapsedTimeSeconds() >= 7.5 || Magazine.INSTANCE.getSlotsFilled() >= 2),
                OuttakeThingy(),
                Turret.INSTANCE.setTracking(),
                new InstantCommand(() -> {
                    follower.setMaxPower(pathPower);
                    follower.followPath(score2);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.stopIntake(),
                Magazine.INSTANCE.fillSlots(),
                Artemis.INSTANCE.shootMotif(shootPower),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 0),
                Turret.INSTANCE.setPosition(0),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.followPath(interrim2);
                    follower.setMaxPower(moreIntakePower);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.intake(),
                Magazine.INSTANCE.setMode(0),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake2);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() >= 1),
                new InstantCommand(() -> follower.followPath(endIntake2)),
                new WaitUntil(() -> !follower.isBusy() || pathTimer.getElapsedTimeSeconds() >= 7.5 || Magazine.INSTANCE.getSlotsFilled() >= 2),
                OuttakeThingy(),
                Turret.INSTANCE.setTracking(),
                new InstantCommand(() -> {
                    follower.setMaxPower(pathPower);
                    follower.followPath(score2);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.stopIntake(),
                Magazine.INSTANCE.fillSlots(),
                Artemis.INSTANCE.shootMotif()
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
        addData("Is Follower Busy", follower.isBusy());
        addData("x", currentPose.getX());
        addData("y", currentPose.getY());
        addData("heading", Math.toDegrees(currentPose.getHeading()));
        addData("speed", Shooter.INSTANCE.shooters.getVelocity());
        addData("motif", Magazine.INSTANCE.motif[0]);
        addData("motif", Magazine.INSTANCE.motif[1]);
        addData("motif", Magazine.INSTANCE.motif[2]);


        Drawing.drawDebug(follower);
    }
}
