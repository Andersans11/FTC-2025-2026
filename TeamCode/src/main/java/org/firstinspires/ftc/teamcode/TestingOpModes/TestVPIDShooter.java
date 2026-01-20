package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@TeleOp(name = "Test VIPD Shooter", group = Utils.TESTING)
public class TestVPIDShooter extends RoyallyFuckedUpMode {

    public TestVPIDShooter() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Shooter.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(Shooter.INSTANCE.spinUp());
        P1.cross().whenBecomesTrue(Shooter.INSTANCE.spinDown());
        P1.circle().whenBecomesTrue(Shooter.INSTANCE.idle());
        P1.square().whenBecomesTrue(Shooter.INSTANCE.resetPID());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("goal", Shooter.INSTANCE.controller.getGoal());
        addData("vel", Shooter.INSTANCE.shooters.getVelocity());
    }
}
