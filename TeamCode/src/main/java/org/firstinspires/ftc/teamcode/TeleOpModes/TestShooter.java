package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

@TeleOp(name = "Test Intake")
public class TestShooter extends RoyallyFuckedUpMode {

    public TestShooter() {
        super();
        addComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Shooter.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        driveTrainBinds();

        RobotConfig.bind(P1.triangle(), "SHOOT");
    }

    @Override
    public void onStartButtonPressed() {
        telemetry.addLine("sdgndsfgjnsdfj");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        telemetry.addLine("osuigsdfoiugbdsoifjnsts");
    }
}
