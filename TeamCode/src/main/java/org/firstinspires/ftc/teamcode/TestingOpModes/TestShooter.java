package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

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
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        addData("speed", Shooter.INSTANCE.shooterMotors[0].getVelocity());
    }
}
