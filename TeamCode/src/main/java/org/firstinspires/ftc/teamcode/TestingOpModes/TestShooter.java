package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.VPIDShooter;

@TeleOp(name = "Test Shooter", group = Utils.TESTING)
public class TestShooter extends RoyallyFuckedUpMode {

    public TestShooter() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(VPIDShooter.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(VPIDShooter.INSTANCE.spinUp());
        P1.square().whenBecomesTrue(VPIDShooter.INSTANCE.idle());
        P1.circle().whenBecomesTrue(VPIDShooter.INSTANCE.resetPID());
        P1.cross().whenBecomesTrue(VPIDShooter.INSTANCE.spinDown());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("speed", VPIDShooter.INSTANCE.shooters.getState().getVelocity());
        addData("power", VPIDShooter.INSTANCE.shooters.getPower());
    }
}
