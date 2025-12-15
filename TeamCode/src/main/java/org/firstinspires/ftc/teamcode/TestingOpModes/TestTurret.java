package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.PoseTrackingTurret;

@TeleOp(name = "Test: Turret", group = Utils.TESTING)
public class TestTurret extends RoyallyFuckedUpMode {

    public TestTurret() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(PoseTrackingTurret.INSTANCE),
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

        addData("targetYaw", PoseTrackingTurret.INSTANCE.targetYaw);
        addData("targetPitch", PoseTrackingTurret.INSTANCE.targetPitch);
        addData("goal", PoseTrackingTurret.INSTANCE.controller.getGoal());
        addData("length", PoseTrackingTurret.INSTANCE.camera.blocks().length);
    }
}
