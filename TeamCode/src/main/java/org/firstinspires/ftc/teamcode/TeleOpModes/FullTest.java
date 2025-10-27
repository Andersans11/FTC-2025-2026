package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.NewMagazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;

@TeleOp(name = "perseus throws an artifact and hits eddie in the face", group = Utils.PRIORITY)
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

        Perseus.INSTANCE.stopIntake().schedule();

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Perseus.INSTANCE.stopIntake());


        P2.cross().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(NewMagazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(NewMagazine.INSTANCE.setMode(1));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        P2.leftStickX().update();
        NewTurret.INSTANCE.ChangePosition(P2.leftStickX().get()).schedule();

        telemetry.addData("0", NewMagazine.INSTANCE.getSlotColor(0));
        telemetry.addData("1", NewMagazine.INSTANCE.getSlotColor(1));
        telemetry.addData("2", NewMagazine.INSTANCE.getSlotColor(2));
        telemetry.addData("Active", NewMagazine.INSTANCE.activeSlot);
        telemetry.addData("Mode", NewMagazine.INSTANCE.mode);
        telemetry.addData("targetPos", NewMagazine.INSTANCE.targetPos);
        telemetry.addData("oldTargetPos", NewMagazine.INSTANCE.oldTargetPos);
        telemetry.addData("servoPos", NewMagazine.INSTANCE.servos[0].getTotalRotation());
        telemetry.addData("servoTargetPos", NewMagazine.INSTANCE.servos[0].getTargetRotation());
        telemetry.addData("i", NewMagazine.INSTANCE.i);
        telemetry.addData("desiredColor", NewMagazine.INSTANCE.desiredColor);
        telemetry.addData("power", NewMagazine.INSTANCE.servos[0].power);
        telemetry.addData("P", NewMagazine.INSTANCE.servos[0].getKD());

        telemetry.addData("targetAngle", NewTurret.INSTANCE.targetAngle);
        telemetry.addData("motorPower", NewTurret.INSTANCE.controller.calculate(NewTurret.INSTANCE.rotationMotor.getState()));
        telemetry.addData("goal", NewTurret.INSTANCE.controller.getGoal());
        telemetry.addData("length", NewTurret.INSTANCE.camera.blocks().length);
    }
}
