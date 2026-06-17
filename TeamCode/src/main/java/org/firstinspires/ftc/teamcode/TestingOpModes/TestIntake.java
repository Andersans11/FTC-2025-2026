package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;

@Configurable
@TeleOp(name = "Test Intake", group = Utils.TESTING)
public class TestIntake extends RRoboticsOpMode {

    public static boolean doIntake = false;
    boolean isIntaking = false;

    public TestIntake() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Intake.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (doIntake != isIntaking) {
            if (doIntake) {
                Intake.INSTANCE.start().schedule();
                Intake.INSTANCE.active().schedule();
            } else {
                Intake.INSTANCE.stop().schedule();
                Intake.INSTANCE.off().schedule();
            }
            isIntaking = doIntake;
        }

    }
}
