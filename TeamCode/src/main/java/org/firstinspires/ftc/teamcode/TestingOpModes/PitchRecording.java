package org.firstinspires.ftc.teamcode.TestingOpModes;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

@Disabled
@Configurable
@TeleOp(name = "Pitch Recording", group = Utils.PRIORITY)
public class PitchRecording extends RoyallyFuckedUpMode {


    boolean outputValues = false;
    public static double hoodPos = 0.45;

    public PitchRecording() {
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

        P1.rightTrigger().atLeast(0.1)
                .whenBecomesTrue(Perseus.INSTANCE.intake())
                .whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(false));
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(true));

        P1.rightBumper()
                .whenBecomesTrue(Perseus.INSTANCE.outtake())
                .whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P1.leftBumper().whenBecomesTrue(Perseus.INSTANCE.updateHoodPos());

        P1.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P1.y().whenBecomesTrue(Shooter.INSTANCE.setHoodPos(hoodPos));


        P2.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Perseus.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        P2.dpadUp().whenBecomesTrue(Shooter.INSTANCE.resetKicker());

        P2.rightBumper().whenTrue(Turret.INSTANCE.changePosition(0.5));
        P2.leftBumper().whenTrue(Turret.INSTANCE.changePosition(-0.5));

        P2.dpadDown().whenBecomesTrue(Turret.INSTANCE.autoControl());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Perseus.INSTANCE.start();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            HuskyLens.Block[] blocks = Turret.INSTANCE.camera.blocks(1);

            addData("tagX", blocks != null ? blocks[0].x : "no blocks found");
            addData("tagY", blocks != null ? blocks[0].y : "no blocks found");

        } catch (RuntimeException e) {
            addData("tagX", "no blocks found");
            addData("tagY", "no blocks found");
        }

        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);

        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("range", Magazine.INSTANCE.color.getDistance(DistanceUnit.MM));

        addData("motif1", Magazine.INSTANCE.motif[0]);
        addData("motif2", Magazine.INSTANCE.motif[1]);
        addData("motif3", Magazine.INSTANCE.motif[2]);

    }
}
