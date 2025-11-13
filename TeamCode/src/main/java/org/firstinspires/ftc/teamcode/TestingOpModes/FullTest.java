package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.NewMagazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@TeleOp(name = "Test: Full", group = Utils.PRIORITY)
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
        NewTurret.INSTANCE.initPoseUpdater(this);

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(() -> NewTurret.INSTANCE.isRedAlliance = false);
        P1.dpadDown().whenBecomesTrue(() -> NewTurret.INSTANCE.isRedAlliance = true);

        P1.rightBumper().whenBecomesTrue(Perseus.INSTANCE.outtake());
        P1.rightBumper().whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P2.dpadLeft().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P1.dpadLeft().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(NewMagazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(NewMagazine.INSTANCE.setMode(1));

        P2.dpadUp().whenBecomesTrue(Shooter.INSTANCE.resetKicker());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Perseus.INSTANCE.start();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        telemetry.addData("0", NewMagazine.INSTANCE.getSlotColor(0));
        telemetry.addData("1", NewMagazine.INSTANCE.getSlotColor(1));
        telemetry.addData("2", NewMagazine.INSTANCE.getSlotColor(2));
        telemetry.addData("Active", NewMagazine.INSTANCE.activeSlot);
        telemetry.addData("Mode", NewMagazine.INSTANCE.mode);
        //telemetry.addData("targetPos", NewMagazine.INSTANCE.targetPos);
        //telemetry.addData("oldTargetPos", NewMagazine.INSTANCE.oldTargetPos);
        //telemetry.addData("i", NewMagazine.INSTANCE.i);
        telemetry.addData("desiredColor", NewMagazine.INSTANCE.desiredColor);
        telemetry.addData("range", NewMagazine.INSTANCE.color.getDistance(DistanceUnit.MM));

        //telemetry.addData("targetAngle", NewTurret.INSTANCE.targetAngle);
        //telemetry.addData("motorPower", NewTurret.INSTANCE.controller.calculate(NewTurret.INSTANCE.rotationMotor.getState()));
        //telemetry.addData("goal", NewTurret.INSTANCE.controller.getGoal());
        //telemetry.addData("length", NewTurret.INSTANCE.camera.blocks().length);

        telemetry.addData("red", NewMagazine.INSTANCE.color.red());
        telemetry.addData("green", NewMagazine.INSTANCE.color.green());
        telemetry.addData("blue", NewMagazine.INSTANCE.color.blue());

        telemetry.addData("motif1", Perseus.INSTANCE.motif[0]);
        telemetry.addData("motif2", Perseus.INSTANCE.motif[1]);
        telemetry.addData("motif3", Perseus.INSTANCE.motif[2]);

    }
}
