package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.NewMagazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;

import dev.nextftc.core.commands.groups.SequentialGroup;

@TeleOp(name = "Test Magazine", group = Utils.TESTING)
public class TestMagazine extends RoyallyFuckedUpMode {

    int i = 0;

    public TestMagazine() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(NewMagazine.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));
        P1.circle().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.cross().whenBecomesTrue(NewMagazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE));
        P1.square().whenBecomesTrue(new SequentialGroup(
                NewMagazine.INSTANCE.incShotsFired(),
                NewMagazine.INSTANCE.setDesiredColor()
        ));

        P2.circle().whenBecomesTrue(() -> i++);

        P1.rightBumper().whenBecomesTrue(NewMagazine.INSTANCE.setMode(0));
        P1.leftBumper().whenBecomesTrue(NewMagazine.INSTANCE.setMode(1));
    }
    
    @Override
    public void onUpdate() {
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

        super.onUpdate();
    }
}
