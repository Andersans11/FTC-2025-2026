package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Test Shooter", group = Utils.TESTING)
public class TestShooter extends RoyallyFuckedUpMode {

    public TestShooter() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Shooter.INSTANCE)
        );
    }

    DcMotorEx motor;

    @Override
    public void onInit() {
        super.onInit();

        motor = RobotConfig.BRDrive.getMotor().getMotor();

        P1.triangle().whenBecomesTrue(Shooter.INSTANCE.spinUp());
        P1.square().whenBecomesTrue(Shooter.INSTANCE.idle());
        P1.circle().whenBecomesTrue(Shooter.INSTANCE.resetPID());
        P1.cross().whenBecomesTrue(Shooter.INSTANCE.spinDown());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("speed", Shooter.INSTANCE.shooters.getLeader().getVelocity());
        addData("speed", Shooter.INSTANCE.shooters.getFollowers()[0].getVelocity());
        addData("power", Shooter.INSTANCE.shooters.getPower());
        addData("enc", (Shooter.INSTANCE.shooters.getLeader().getVelocity() + Shooter.INSTANCE.shooters.getFollowers()[0].getVelocity()) / 2);
        addData("factor", Shooter.INSTANCE.shooters.getVelocity() / motor.getVelocity());
    }
}
