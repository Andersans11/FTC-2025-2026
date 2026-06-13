package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.PIDDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Stayputnik;

@Configurable
@TeleOp(name="Test: PID Drive Tuning")
public class PIDDriveTest extends RRoboticsOpMode {
    public PIDDriveTest() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Stayputnik.INSTANCE)
        );
    }

    public static double targetVel = 0;

    @Override
    public void onStartButtonPressed() {
        P1.circle().whenTrue(Stayputnik.INSTANCE.setTarget(targetVel));
        P1.cross().whenTrue(Stayputnik.INSTANCE::resetPID);
    }
}
