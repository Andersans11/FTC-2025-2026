package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

@TeleOp(name = "Test: Turret", group = Utils.TESTING)
public class TestTurret extends RoyallyFuckedUpMode {

    public TestTurret() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Turret.INSTANCE),
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        //P1.dpadDown().whenBecomesTrue(NewTurret.INSTANCE.setPosition(0));
        //P1.dpadRight().whenBecomesTrue(NewTurret.INSTANCE.setPosition(90));
        //P1.dpadLeft().whenBecomesTrue(NewTurret.INSTANCE.setPosition(-90));
        //P1.dpadUp().whenBecomesTrue(NewTurret.INSTANCE.resetPID());
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("targetAngle", Turret.INSTANCE.targetAngle);
        addData("goal", Turret.INSTANCE.controller.getGoal());
        addData("length", Turret.INSTANCE.camera.blocks().length);
        addData("waugh", Turret.INSTANCE.waugh());
        addData("timer", Turret.INSTANCE.timer.getElapsedTimeSeconds());
    }
}
