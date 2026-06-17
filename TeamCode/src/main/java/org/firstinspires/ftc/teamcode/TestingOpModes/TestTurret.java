package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

@Configurable
@TeleOp(name = "Test Turret", group = Utils.TESTING)
public class TestTurret extends RRoboticsOpMode {

    public static double turretPos = 0;
    public static double oldPos = 1;

    public TestTurret() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(Turret.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        Turret.INSTANCE.mode = Turret.TurretMode.TESTING;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (turretPos != oldPos) {
            Turret.INSTANCE.targetYaw = turretPos;
            oldPos = turretPos;
        }
        addData("ServoVal", Turret.INSTANCE.servos[0].getPosition());
        addData("TurretVal", Turret.INSTANCE.ticksToDegrees(Turret.INSTANCE.servos[0].getPosition()));
    }
}
