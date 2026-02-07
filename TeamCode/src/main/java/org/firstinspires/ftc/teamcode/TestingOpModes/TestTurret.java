package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.OldTurret;

@Disabled
@TeleOp(name = "Test: Turret", group = Utils.TESTING)
public class TestTurret extends RoyallyFuckedUpMode {

    public TestTurret() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(OldTurret.INSTANCE),
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        OldTurret.INSTANCE.mode = OldTurret.TurretMode.MANUAL_PID;

        P1.dpadDown().whenBecomesTrue(OldTurret.INSTANCE.setPosition(0));
        P1.dpadRight().whenBecomesTrue(OldTurret.INSTANCE.setPosition(45));
        P1.dpadLeft().whenBecomesTrue(OldTurret.INSTANCE.setPosition(-45));
        //P1.dpadUp().whenBecomesTrue(NewTurret.INSTANCE.resetPID());
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("targetAngle", OldTurret.INSTANCE.targetAngle);
        addData("goal", OldTurret.INSTANCE.controller.getGoal());
        addData("length", OldTurret.INSTANCE.camera.blocks().length);
        addData("waugh", OldTurret.INSTANCE.waugh());
        addData("timer", OldTurret.INSTANCE.timer.getElapsedTimeSeconds());
    }
}
