package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

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

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.gate());
        P1.rightBumper().whenBecomesFalse(Selene.INSTANCE.stopIntake());

        P1.leftBumper().whenBecomesTrue(Selene.INSTANCE.shoot());
        P1.leftBumper().whenBecomesFalse(Selene.INSTANCE.stopShoot());

        P2.triangle().whenBecomesTrue(() -> Shooter.powerMod = Shooter.powerMod + 50);
        P2.cross().whenBecomesTrue(() -> Shooter.powerMod = Shooter.powerMod - 50);

        P2.square().whenBecomesTrue(() -> Turret.turretOff = Turret.turretOff + 5);
        P2.circle().whenBecomesTrue(() -> Turret.turretOff = Turret.turretOff - 5);

        P2.dpadDown().whenBecomesTrue(Selene.INSTANCE.resetFollower());

        P2.leftBumper().whenBecomesTrue(Turret.INSTANCE::switchTargets);

        P1.square().whenBecomesTrue(Turret.INSTANCE.autoControl(true));

        Selene.INSTANCE.indMode = Selene.IndicatorMode.INIT_DONE;
        Selene.INSTANCE.getOpMode(this);
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
        Turret.INSTANCE.autoControl(true).schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("poseX", Turret.INSTANCE.getTurretPose(Selene.currentPose).getX());
        addData("poseY", Turret.INSTANCE.getTurretPose(Selene.currentPose).getY());
        addData("poseHeading", Turret.INSTANCE.getTurretPose(Selene.currentPose).getHeading());
        addData("targX", Turret.INSTANCE.targetPose.getX());
        addData("targY", Turret.INSTANCE.targetPose.getY());
        addData("targHeading", Turret.INSTANCE.targetPose.getHeading());
        addData("speed", Shooter.INSTANCE.shooters.getVelocity());
        addData("Power Mod", Shooter.powerMod);
        addData("stopperPos", Shooter.INSTANCE.stopper.getPosition());
        addData("turretPos", Turret.INSTANCE.targetYaw);
        addData("targetDist", Turret.INSTANCE.getrobotToGoalVector(Turret.INSTANCE.getTurretPose(Selene.currentPose)));
    }
}
