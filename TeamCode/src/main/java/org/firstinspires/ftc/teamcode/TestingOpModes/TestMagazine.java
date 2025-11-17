package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

@TeleOp(name = "Test Magazine", group = Utils.OpModeGroups.TESTING)
public class TestMagazine extends RoyallyFuckedUpMode {

    int i = 0;

    public TestMagazine() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Magazine.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));
        P1.circle().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.cross().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE));

        P2.circle().whenBecomesTrue(() -> i++);

        P1.rightBumper().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P1.leftBumper().whenBecomesTrue(Magazine.INSTANCE.setMode(1));
    }
    
    @Override
    public void onUpdate() {
        telemetry.addData("0", Magazine.INSTANCE.getSlotColor(0));
        telemetry.addData("1", Magazine.INSTANCE.getSlotColor(1));
        telemetry.addData("2", Magazine.INSTANCE.getSlotColor(2));
        telemetry.addData("Active", Magazine.INSTANCE.activeSlot);
        telemetry.addData("Mode", Magazine.INSTANCE.mode);
        telemetry.addData("targetPos", Magazine.INSTANCE.targetPos);
        telemetry.addData("oldTargetPos", Magazine.INSTANCE.oldTargetPos);
        telemetry.addData("i", Magazine.INSTANCE.it);
        telemetry.addData("desiredColor", Magazine.INSTANCE.desiredColor);

        super.onUpdate();
    }
}
