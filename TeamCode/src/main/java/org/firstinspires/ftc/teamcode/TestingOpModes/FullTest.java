package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.lynx.commands.standard.LynxSetNewModuleAddressCommand;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
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
        Turret.INSTANCE.initPoseUpdater(this);

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(() -> Turret.INSTANCE.isRedAlliance = false);
        P1.dpadDown().whenBecomesTrue(() -> Turret.INSTANCE.isRedAlliance = true);

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

        P2.rightBumper().whenTrue(Turret.INSTANCE.ChangePosition(0.5));
        P2.leftBumper().whenTrue(Turret.INSTANCE.ChangePosition(-0.5));

        P2.dpadDown().whenBecomesTrue(Turret.INSTANCE.AutoControl());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Perseus.INSTANCE.start();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        telemetry.addData("0", Magazine.INSTANCE.getSlotColor(0));
        telemetry.addData("1", Magazine.INSTANCE.getSlotColor(1));
        telemetry.addData("2", Magazine.INSTANCE.getSlotColor(2));
        telemetry.addData("Active", Magazine.INSTANCE.activeSlot);
        telemetry.addData("Mode", Magazine.INSTANCE.mode);
        //telemetry.addData("targetPos", Magazine.INSTANCE.targetPos);
        //telemetry.addData("oldTargetPos", Magazine.INSTANCE.oldTargetPos);
        //telemetry.addData("i", Magazine.INSTANCE.i);
        telemetry.addData("desiredColor", Magazine.INSTANCE.desiredColor);
        telemetry.addData("range", Magazine.INSTANCE.color.getDistance(DistanceUnit.MM));

        //telemetry.addData("targetAngle", NewTurret.INSTANCE.targetAngle);
        //telemetry.addData("motorPower", NewTurret.INSTANCE.controller.calculate(NewTurret.INSTANCE.rotationMotor.getState()));
        //telemetry.addData("goal", NewTurret.INSTANCE.controller.getGoal());
        //telemetry.addData("length", NewTurret.INSTANCE.camera.blocks().length);

        telemetry.addData("motif1", Magazine.INSTANCE.motif[0]);
        telemetry.addData("motif2", Magazine.INSTANCE.motif[1]);
        telemetry.addData("motif3", Magazine.INSTANCE.motif[2]);

    }
}
