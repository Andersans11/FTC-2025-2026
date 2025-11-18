package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.groups.ParallelGroup;

@TeleOp(name = "TwoAndOnlyOpMode", group = Utils.PRIORITY_PRIORITY)
public class Comp_OpMode extends RoyallyFuckedUpMode {

    public Comp_OpMode() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        Turret.INSTANCE.initPoseUpdater(this);

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(false));
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(true));

        P1.rightBumper().whenBecomesTrue(Perseus.INSTANCE.outtake());
        P1.rightBumper().whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P2.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P1.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        P2.dpadUp().whenBecomesTrue(Shooter.INSTANCE.resetKicker());

        P2.leftBumper().whenBecomesTrue(Magazine.INSTANCE.incShotsFired());

        P2.dpadDown().whenBecomesTrue(new ParallelGroup(Turret.INSTANCE.autoControl(), Turret.INSTANCE.zero()));
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Perseus.INSTANCE.start().schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        P2.leftStickX().update();

        if (Math.abs(P2.leftStickX().get()) >= 0.1) {
            Turret.INSTANCE.changePosition(P2.leftStickX().get() * 10);
        }

        telemetryManager.addData("0", Magazine.INSTANCE.getSlotColor(0));
        telemetryManager.addData("1", Magazine.INSTANCE.getSlotColor(1));
        telemetryManager.addData("2", Magazine.INSTANCE.getSlotColor(2));
        telemetryManager.addData("Active", Magazine.INSTANCE.activeSlot);
        telemetryManager.addData("Mode", Magazine.INSTANCE.mode);
        telemetryManager.addData("desiredColor", Magazine.INSTANCE.desiredColor);
        telemetryManager.addData("shotsFired", Magazine.INSTANCE.shotsFired);
    }
}
