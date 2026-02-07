package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@Disabled
@TeleOp(name = "Test Shooter", group = Utils.TESTING)
public class TestShooter extends RoyallyFuckedUpMode {

    public TestShooter() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Shooter.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(Shooter.INSTANCE.spinUp());
        P1.square().whenBecomesTrue(Shooter.INSTANCE.idle());
        P1.circle().whenBecomesTrue(Shooter.INSTANCE.resetPID());
        P1.cross().whenBecomesTrue(Shooter.INSTANCE.spinDown());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("speed", Shooter.INSTANCE.shooters.getState().getVelocity());
        addData("power", Shooter.INSTANCE.shooters.getPower());
    }
}
