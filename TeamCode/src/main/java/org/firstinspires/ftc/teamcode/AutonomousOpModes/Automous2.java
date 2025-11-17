package org.firstinspires.ftc.teamcode.AutonomousOpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.Drawing;

@Autonomous(name = "Automous2")
public class Automous2 extends RoyallyFuckedUpMode {
    Follower follower;

    int outcomeState = 0;

    PathChain path;

    Pose startingPose;
    Pose scoringPose;

    public Automous2() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        Drawing.init();

        telemetryManager.addLine("1");
        follower = Constants.createFollower(hardwareMap);
        telemetryManager.addLine("1");
        Turret.INSTANCE.setPosition(-90).schedule();
        telemetryManager.addLine("1");
        Turret.INSTANCE.initPoseUpdater(this);

        startingPose = new Pose(72, 72);
        scoringPose = new Pose(22, 72);

        Magazine.INSTANCE.setSlotContent(0, Utils.ArtifactTypes.PURPLE).schedule();
        Magazine.INSTANCE.setSlotContent(1, Utils.ArtifactTypes.PURPLE).schedule();
        Magazine.INSTANCE.setSlotContent(2, Utils.ArtifactTypes.GREEN).schedule();

        path = follower.pathBuilder()
                .addPath(new Path(new BezierLine(startingPose, scoringPose)))
                .setConstantHeadingInterpolation(0)
                .addParametricCallback(0, () -> Magazine.INSTANCE.setMode(1).schedule())
                .addParametricCallback(0.25, () -> Turret.INSTANCE.autoControl().schedule())
                .addParametricCallback(1, () -> Perseus.INSTANCE.shootMotif().schedule())
                .build();
        follower.setStartingPose(startingPose);
        follower.setMaxPower(0.75);
        Shooter.INSTANCE.resetKicker().schedule();
        path.resetCallbacks();
    }

    @Override
    public void onWaitForStart() {
        telemetryManager.update();
        Turret.INSTANCE.periodic();
        Magazine.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        follower.followPath(path);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        follower.update();
        telemetryManager.addData("0", Magazine.INSTANCE.getSlotColor(0));
        telemetryManager.addData("1", Magazine.INSTANCE.getSlotColor(1));
        telemetryManager.addData("2", Magazine.INSTANCE.getSlotColor(2));
        telemetryManager.addData("Active", Magazine.INSTANCE.activeSlot);
        telemetryManager.addData("Mode", Magazine.INSTANCE.mode);
        telemetryManager.addData("desiredColor", Magazine.INSTANCE.desiredColor);
        telemetryManager.addData("isBusy", follower.isBusy());
        Drawing.drawDebug(follower);
    }
}
