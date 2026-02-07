package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@TeleOp(name = "Pitch Recording", group = Utils.PRIORITY_PRIORITY)
public class PitchRecording extends RoyallyFuckedUpMode {

    public PitchRecording() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        Selene.INSTANCE.initFollower(hardwareMap);

        Turret.INSTANCE.mode = Turret.TurretMode.POSE_TRACKING;

        //P1.square().whenBecomesTrue(PoseTrackingTurret.INSTANCE.resetPose());
        P1.circle().whenBecomesTrue(Turret.INSTANCE.setIdle());
        P1.cross().whenBecomesTrue(Turret.INSTANCE.setTracking());

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Selene.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Selene.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance());

        P1.rightBumper().whenBecomesTrue(Selene.INSTANCE.outtake());
        P1.rightBumper().whenBecomesFalse(Selene.INSTANCE.stopIntake());

        P1.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Selene.INSTANCE.shootSingle());

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Selene.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        P2.dpadUp().whenBecomesTrue(() -> Shooter.INSTANCE.hood.setPosition(Turret.hoodToPos));

        P2.leftTrigger().atLeast(0.1).whenBecomesTrue(Magazine.INSTANCE.incShotsFired());

        P2.dpadDown().whenBecomesTrue(Selene.INSTANCE.resetFollower());

        Selene.INSTANCE.indMode = Selene.IndicatorMode.INIT_DONE;
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Selene.INSTANCE.start().schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("distance", Selene.INSTANCE.currentPose.distanceFrom(Turret.INSTANCE.targetPose));

        addData("tagY", Turret.INSTANCE.waugh().component2());

        addData("target", RobotConfig.HoodServo.getServo().getPosition());
        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("shotsFired", Magazine.INSTANCE.shotsFired);
        addData("turret", Turret.INSTANCE.rotationMotor.getPower());
    }
}
