package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@TeleOp(name = "Test: Full", group = Utils.PRIORITY_PRIORITY)
public class FullTest extends RRoboticsOpMode {

    public FullTest() {
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

        P1.rightTrigger().atLeast(0.1).whenBecomesTrue(Selene.INSTANCE.intakeOn());
        P1.rightTrigger().atLeast(0.1).whenBecomesFalse(Selene.INSTANCE.intakeOff());

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance());

        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE.gate());
        P1.rightBumper().whenBecomesFalse(Selene.INSTANCE.intakeOff());

        P2.rightTrigger().atLeast(0.1).whenBecomesTrue(Shooter.INSTANCE.StopperOpen());
        P2.rightTrigger().atLeast(0.1).whenBecomesFalse(Shooter.INSTANCE.StopperClose());

        P1.leftBumper().whenBecomesTrue(Shooter.INSTANCE.StopperOpen());
        P1.leftBumper().whenBecomesFalse(Shooter.INSTANCE.StopperClose());

        P2.dpadDown().whenBecomesTrue(Selene.INSTANCE.resetFollower());

        P2.leftBumper().whenBecomesTrue(Selene.INSTANCE.setAutoShooting());
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
        Turret.INSTANCE.autoControl(true).schedule();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("speed", Shooter.INSTANCE.shooters.getLeader().getVelocity());

        addData("ticks", Shooter.INSTANCE.controller.getGoal().getVelocity());

        addData("turret target yaw", Turret.INSTANCE.targetYaw);

        addData("autoshooting", Selene.INSTANCE.autoShooting);
        addData("is in zone", Turret.INSTANCE.isInZone(Selene.INSTANCE.currentPose));
        addData("is at limit", Turret.INSTANCE.isAtLimit());

        addData("i", Turret.INSTANCE.getrobotToGoalVector(Selene.INSTANCE.currentPose).getMagnitude());
    }
}