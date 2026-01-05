package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.PoseTrackingTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

@TeleOp(name = "Pitch Recording", group = Utils.PRIORITY_PRIORITY)
public class PitchRecording extends RoyallyFuckedUpMode {

    public PitchRecording() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Artemis.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        Artemis.INSTANCE.initFollower(hardwareMap);

        PoseTrackingTurret.INSTANCE.mode = PoseTrackingTurret.TurretMode.POSE_TRACKING;

        P1.square().whenBecomesTrue(PoseTrackingTurret.INSTANCE.resetPose());
        P1.circle().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setIdle());
        P1.cross().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setTracking());

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Artemis.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Artemis.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(PoseTrackingTurret.INSTANCE.setRedAlliance());

        P1.rightBumper().whenBecomesTrue(Artemis.INSTANCE.outtake());
        P1.rightBumper().whenBecomesFalse(Artemis.INSTANCE.stopIntake());

        P1.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Artemis.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Artemis.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Artemis.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        P2.dpadUp().whenBecomesTrue(() -> Shooter.INSTANCE.hood.setPosition(PoseTrackingTurret.hoodToPos));

        P2.leftTrigger().atLeast(0.1).whenBecomesTrue(Magazine.INSTANCE.incShotsFired());

        P2.dpadDown().whenBecomesTrue(Artemis.INSTANCE.resetFollower());

        Artemis.INSTANCE.indMode = Artemis.IndicatorMode.INIT_DONE;
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Artemis.INSTANCE.start().schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("distance", Artemis.INSTANCE.currentPose.distanceFrom(PoseTrackingTurret.INSTANCE.targetPose));

        addData("target", RobotConfig.HoodServo.getServo().getPosition());
        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("shotsFired", Magazine.INSTANCE.shotsFired);
        addData("turret", PoseTrackingTurret.INSTANCE.rotationMotor.getPower());
    }
}
