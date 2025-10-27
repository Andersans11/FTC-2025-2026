package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;

@TeleOp(name = "Test Intake", group = Utils.TESTING)
public class TestIntake extends RoyallyFuckedUpMode {

    public TestIntake() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(HoldHeadingPID.INSTANCE),
                new BetterSubsystemComponent(Intake.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.rightTrigger().atLeast(Sensitivities.p1RTThreshold).whenBecomesTrue(Intake.INSTANCE::start);
        P1.rightTrigger().atLeast(Sensitivities.p1RTThreshold).whenBecomesFalse(Intake.INSTANCE::idle);
        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE::stop);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
