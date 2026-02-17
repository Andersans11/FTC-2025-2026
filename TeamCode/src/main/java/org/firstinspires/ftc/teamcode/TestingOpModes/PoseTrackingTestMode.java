package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

@Disabled
@TeleOp(name = "Pose Tracking", group = Utils.TESTING)
public class PoseTrackingTestMode extends RRoboticsOpMode {

    public PoseTrackingTestMode() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricDrive.INSTANCE),
                new RRoboticsSubsystemComponent(Turret.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        Turret.INSTANCE.mode = Turret.TurretMode.POSE_TRACKING;

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance());
        //P1.square().whenBecomesTrue(PoseTrackingTurret.INSTANCE.resetPose());
        P1.circle().whenBecomesTrue(Turret.INSTANCE.setIdle());
        P1.cross().whenBecomesTrue(Turret.INSTANCE.setTracking());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("mode", Turret.INSTANCE.mode);
        addData("targetYaw", Turret.INSTANCE.targetYaw);
        addData("targetPitch", Turret.INSTANCE.targetPitch);
    }
}
