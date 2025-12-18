package org.firstinspires.ftc.teamcode.TestingOpModes;


import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
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
                new BetterSubsystemComponent(Artemis.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        P1.leftBumper().whenBecomesTrue(Artemis.INSTANCE.updateHoodPos());

        Artemis.INSTANCE.initFollower(hardwareMap);

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Artemis.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Artemis.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(false));
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance(true));

        P1.rightBumper().whenBecomesTrue(Artemis.INSTANCE.outtake());
        P1.rightBumper().whenBecomesFalse(Artemis.INSTANCE.stopIntake());

        P2.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P2.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P1.dpadLeft().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.GREEN));
        P1.dpadRight().whenBecomesTrue(Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.PURPLE));

        P2.cross().whenBecomesTrue(Artemis.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.circle().whenBecomesTrue(Artemis.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Artemis.INSTANCE.shootMotif());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        P2.dpadUp().whenBecomesTrue(Shooter.INSTANCE.resetKicker());

        P2.leftTrigger().atLeast(0.1).whenBecomesTrue(Magazine.INSTANCE.incShotsFired());
        P2.leftBumper().whenBecomesTrue(Turret.INSTANCE.zero());

        P2.rightBumper().whenBecomesTrue(Turret.INSTANCE.switchMode());

        P2.dpadDown().whenBecomesTrue(Artemis.INSTANCE.resetFollower());
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Artemis.INSTANCE.start();
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

        addData("distance", Math.sqrt(Math.pow((144 - Artemis.INSTANCE.currentPose.getX()), 2) + Math.pow((144 - Artemis.INSTANCE.currentPose.getY()), 2)));

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
