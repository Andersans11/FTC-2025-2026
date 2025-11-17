package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@TeleOp(name = "Test: Full", group = Utils.OpModeGroups.PRIORITY)
public class FullTest extends RoyallyFuckedUpMode {

    public FullTest() {
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

        P1.rightTrigger().atLeast(Sensitivities.p1RTThreshold)
                .whenBecomesTrue(Perseus.INSTANCE.intake())
                .whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(false));
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(true));

        P1.rightBumper().whenBecomesTrue(Perseus.INSTANCE.outtake())
                .whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P2.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P1.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(Sensitivities.p2RTThreshold).whenBecomesTrue(Perseus.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        //P2.dpadUp().whenBecomesTrue(Shooter.INSTANCE.resetKicker());

        P2.rightBumper().whenTrue(Turret.INSTANCE.changePosition(0.5));
        P2.leftBumper().whenTrue(Turret.INSTANCE.changePosition(-0.5));

        P2.dpadDown().whenBecomesTrue(Turret.INSTANCE.autoControl());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Perseus.INSTANCE.onStart().schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        telemetryManager.addData("Alliance", Turret.INSTANCE.isRedAlliance ? "red" : "blue");

        telemetryManager.addData("Active", Magazine.INSTANCE.activeSlot);

        telemetryManager.addData("Content", Magazine.INSTANCE.getContentStr());
        telemetryManager.addData("motif", Magazine.INSTANCE.getMotifStr());
        telemetryManager.addData("mode", Magazine.INSTANCE.mode == 0 ? "intake" : "shoot");

        telemetryManager.addData("desiredColor", Magazine.INSTANCE.desiredColor);
        telemetryManager.addData("range", Magazine.INSTANCE.color.getDistance(DistanceUnit.MM));
    }
}
