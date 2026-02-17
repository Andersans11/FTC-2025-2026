package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;

@Disabled
@TeleOp(name = "Test Intake", group = Utils.TESTING)
public class TestIntake extends RRoboticsOpMode {

    public TestIntake() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(HoldHeadingPID.INSTANCE),
                new RRoboticsSubsystemComponent(Intake.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.rightTrigger().atLeast(Sensitivities.p1RTThreshold).whenBecomesTrue(Intake.INSTANCE::start);
        P1.rightBumper().whenBecomesTrue(Intake.INSTANCE::stop);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
