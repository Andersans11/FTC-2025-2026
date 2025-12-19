package org.firstinspires.ftc.teamcode.AutonomousOpModes;

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
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Luna - 9")
public class Luna_9 extends RoyallyFuckedUpMode {
    Follower follower;
    PathChain score1, interrim1, intake1, score2, interrim2, intake2, score3;
    Timer pathTimer;
    public static double pathPower = 0.75;
    public static double intakePower = 0.375;

    public Luna_9() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Artemis.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        Artemis.INSTANCE.initFollower(hardwareMap);

        follower = Constants.createFollower(hardwareMap);

        pathTimer = new Timer();

        Drawing.init();

        Pose starting = new Pose(28, 131, Math.toRadians(54));
        Pose scoring = new Pose(60, 84, Math.toRadians(135));
        Pose intakeStart1 = new Pose(44, 84, Math.toRadians(180));
        Pose intakeEnd1 = new Pose(18, 84, Math.toRadians(180));
        Pose intakeStart2 = new Pose(44, 60, Math.toRadians(180));
        Pose intakeEnd2 = new Pose(11, 60, Math.toRadians(180));

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(starting, scoring))
                .setLinearHeadingInterpolation(starting.getHeading(), scoring.getHeading())
                .build();
        score2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd1, scoring))
                .setLinearHeadingInterpolation(intakeEnd1.getHeading(), scoring.getHeading())
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd2, scoring))
                .setLinearHeadingInterpolation(intakeEnd2.getHeading(), scoring.getHeading())
                .build();

        interrim1 = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStart1))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStart1.getHeading())
                .build();
        interrim1 = follower.pathBuilder()
                .addPath(new BezierLine(scoring, intakeStart2))
                .setLinearHeadingInterpolation(scoring.getHeading(), intakeStart2.getHeading())
                .build();

        intake1 = follower.pathBuilder()
                .addPath(new BezierLine(intakeStart1, intakeEnd1))
                .build();
        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeStart2, intakeEnd2))
                .build();

        follower.setStartingPose(starting);

        follower.setMaxPower(pathPower);

    }

    @Override
    public void onWaitForStart() {
        telemetry.update();
        Turret.INSTANCE.periodic();
        Magazine.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        new SequentialGroup(
                new InstantCommand(()-> follower.followPath(score1)),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.mode == 0),
                new InstantCommand(() -> follower.followPath(interrim1)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake1);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 2.5 || Magazine.INSTANCE.mode == 1),
                Magazine.INSTANCE.fillSlots(),
                new InstantCommand(() -> {
                    follower.setMaxPower(pathPower);
                    follower.followPath(score2);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.mode == 0),
                new InstantCommand(() -> follower.followPath(interrim2)),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> {
                    follower.setMaxPower(intakePower);
                    follower.followPath(intake2);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 2.5 || Magazine.INSTANCE.mode == 1),
                Magazine.INSTANCE.fillSlots(),
                new InstantCommand(() -> {
                    follower.setMaxPower(pathPower);
                    follower.followPath(score3);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                Artemis.INSTANCE.shootMotif()
        ).schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();

        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("Is Follower Busy", follower.isBusy());


        Drawing.drawDebug(follower);
    }
}
