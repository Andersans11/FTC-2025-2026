package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.VPIDShooter;

@TeleOp(name = "Test VIPD Shooter", group = Utils.TESTING)
public class TestVPIDShooter extends RoyallyFuckedUpMode {

    public TestVPIDShooter() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(VPIDShooter.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(VPIDShooter.INSTANCE.spinUp());
        P1.cross().whenBecomesTrue(VPIDShooter.INSTANCE.spinDown());
        P1.circle().whenBecomesTrue(VPIDShooter.INSTANCE.idle());
        P1.square().whenBecomesTrue(VPIDShooter.INSTANCE.resetPID());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("goal", VPIDShooter.INSTANCE.controller.getGoal());
        addData("vel", VPIDShooter.INSTANCE.shooters.getVelocity());
    }
}
