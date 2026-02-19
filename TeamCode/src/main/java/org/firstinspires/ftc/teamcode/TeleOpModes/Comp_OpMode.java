package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;

@TeleOp(name = "TwoAndOnlyOpMode", group = Utils.PRIORITY_PRIORITY)
public class Comp_OpMode extends RRoboticsOpMode {

    public Comp_OpMode() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricDrive.INSTANCE),
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        Selene.INSTANCE.initFollower(hardwareMap);

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Selene.INSTANCE.intake());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Selene.INSTANCE.stopIntake());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance());

        P1.rightBumper().whenBecomesTrue(Selene.INSTANCE.outtake());
        P1.rightBumper().whenBecomesFalse(Selene.INSTANCE.stopIntake());

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Shooter.INSTANCE.StopperUp());
        P2.rightTrigger().atLeast(0.1).whenBecomesFalse(Shooter.INSTANCE.StopperDown());

        P2.square().whenBecomesTrue(Magazine.INSTANCE.setMode(0));
        P2.triangle().whenBecomesTrue(Magazine.INSTANCE.setMode(1));

        P2.cross().whenBecomesTrue(Shooter.INSTANCE.idle());

        P2.leftBumper().whenBecomesTrue(Selene.INSTANCE.setAutoShooting());

        Selene.INSTANCE.indMode = Selene.IndicatorMode.INIT_DONE;
    }

    @Override
    public void onWaitForStart() {
        super.onWaitForStart();
        Selene.INSTANCE.runIndicator();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        Selene.INSTANCE.start().schedule();
        Selene.INSTANCE.indMode = Selene.IndicatorMode.INTAKE_ACTIVE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("0", Magazine.INSTANCE.getSlotColor(0));
        addData("1", Magazine.INSTANCE.getSlotColor(1));
        addData("2", Magazine.INSTANCE.getSlotColor(2));
        addData("Active", Magazine.INSTANCE.activeSlot);
        addData("Mode", Magazine.INSTANCE.mode);
        addData("desiredColor", Magazine.INSTANCE.desiredColor);
        addData("shotsFired", Magazine.INSTANCE.shotsFired);
        addData("turret", Turret.INSTANCE.rotationMotor.getPower());
        addData("speed", Shooter.INSTANCE.shooters.getVelocity());

    }
}
