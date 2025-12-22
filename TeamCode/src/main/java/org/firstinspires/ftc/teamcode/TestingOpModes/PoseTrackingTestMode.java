package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.PoseTrackingTurret;

@TeleOp(name = "Pose Tracking", group = Utils.TESTING)
public class PoseTrackingTestMode extends RoyallyFuckedUpMode {

    public PoseTrackingTestMode() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(PoseTrackingTurret.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        PoseTrackingTurret.INSTANCE.mode = PoseTrackingTurret.TurretMode.POSE_TRACKING;

        P1.dpadUp().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setRedAlliance());
        P1.square().whenBecomesTrue(PoseTrackingTurret.INSTANCE.resetPose());
        P1.circle().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setIdle());
        P1.cross().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setTracking());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

        PoseTrackingTurret.INSTANCE.initPoseUpdater(this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("mode", PoseTrackingTurret.INSTANCE.mode);
        addData("pose", PoseTrackingTurret.INSTANCE.poseUpdater.getPose());
        addData("targetYaw", PoseTrackingTurret.INSTANCE.targetYaw);
        addData("targetPitch", PoseTrackingTurret.INSTANCE.targetPitch);
    }
}
