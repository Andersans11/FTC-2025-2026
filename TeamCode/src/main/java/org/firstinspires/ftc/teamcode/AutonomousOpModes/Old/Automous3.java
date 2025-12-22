package org.firstinspires.ftc.teamcode.AutonomousOpModes.Old;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;
import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@Autonomous(name = "Automous Ultra Pro Max Masters Edition Plus - Blue")
public class Automous3 extends RoyallyFuckedUpMode {
    Follower follower;

    int outcomeState = 0;

    PathChain firstCycle, secondCycle1, secondCycle2, secondCycle3, secondCycle4;

    public static double intake1 = 41;
    public static double intake2 = 36;

    Timer pathTimer;

    Pose startingPose, scoringPose, intakePose1, intakePose2, intakePose3, intakePose4;

    public Automous3() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Artemis.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        pathTimer = new Timer();

        Drawing.init();

        Turret.INSTANCE.setRedAlliance(false).schedule();

        telemetry.addLine("1");
        follower = Constants.createFollower(hardwareMap);
        telemetry.addLine("1");
        Turret.INSTANCE.setPosition(-90).schedule();
        telemetry.addLine("1");

        Pose startingPose = new Pose(28, 131, Math.toRadians(144));
        Pose scoringPose = new Pose(60, 84);
        Pose intakePose1 = new Pose(44, 84);
        Pose intakePose2 = new Pose(intake1, 84);
        Pose intakePose3 = new Pose(intake2, 84);
        Pose intakePose4 = new Pose(16, 84);

        firstCycle = follower.pathBuilder()
                .addPath(new BezierLine(startingPose, scoringPose))
                .setConstantHeadingInterpolation(Math.toRadians(132.5))
                .build();
        secondCycle1 = follower.pathBuilder()
                .addPath(new BezierLine(scoringPose, intakePose1))
                .setLinearHeadingInterpolation(Math.toRadians(132.5), Math.toRadians(180))
                .addPath(new BezierLine(intakePose1, intakePose2))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addParametricCallback(0, () -> {
                    Artemis.INSTANCE.intake().schedule();
                    follower.setMaxPower(0.25);
                })
                .build();
        secondCycle2 = follower.pathBuilder()
                .addPath(new BezierLine(intakePose2, intakePose3))
                .build();
        secondCycle3 = follower.pathBuilder()
                .addPath(new BezierLine(intakePose3, intakePose4))
                .build();
        secondCycle4 = follower.pathBuilder()
                .addPath(new BezierLine(intakePose2, scoringPose))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(132.5))
                .build();

        follower.setStartingPose(startingPose);
        follower.setMaxPower(0.5);
        Shooter.INSTANCE.resetKicker().schedule();
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
                Artemis.INSTANCE.start(),
                Shooter.INSTANCE.idle(),
                new InstantCommand(() -> follower.followPath(firstCycle)),
                Magazine.INSTANCE.setMode(1),
                new WaitUntil(() -> follower.getPathCompletion() >= 0.5),
                new InstantCommand(() -> Turret.INSTANCE.hasGotMotif = false),
                new WaitUntil(() -> Turret.INSTANCE.hasGotMotif),
                Turret.INSTANCE.setPosition(0),
                Turret.INSTANCE.autoControl(),
                Magazine.INSTANCE.setMode(0),
                new WaitUntil(() -> !follower.isBusy()),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 1),
                Artemis.INSTANCE.shootMotif(),
                new WaitUntil(() -> Magazine.INSTANCE.mode == 0),
                new InstantCommand(() -> {
                    follower.followPath(secondCycle1);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 1 || pathTimer.getElapsedTimeSeconds() >= 5),
                new InstantCommand(() -> {
                    follower.followPath(secondCycle2);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 2 || pathTimer.getElapsedTimeSeconds() >= 3.5),
                new InstantCommand(() -> {
                    follower.followPath(secondCycle3);
                    pathTimer.resetTimer();
                }),
                new WaitUntil(() -> Magazine.INSTANCE.getSlotsFilled() == 3 || pathTimer.getElapsedTimeSeconds() >= 3.5),
                new InstantCommand(() -> {
                    Intake.INSTANCE.idle().schedule();
                    follower.setMaxPower(0.5);
                    follower.followPath(secondCycle4);
                }),
                new WaitUntil(() -> !follower.isBusy()),
                //Intake.INSTANCE.idle(),
                new InstantCommand(() -> pathTimer.resetTimer()),
                new WaitUntil(() -> pathTimer.getElapsedTimeSeconds() >= 1),
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
