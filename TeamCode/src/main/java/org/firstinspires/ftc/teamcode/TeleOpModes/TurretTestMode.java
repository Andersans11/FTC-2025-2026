package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

@TeleOp(name = "Turret Test")
public class TurretTestMode extends RoyallyFuckedUpMode {

    public TurretTestMode() {
        super();
        addComponents(
                new BetterSubsystemComponent(Turret.INSTANCE),
                new BetterSubsystemComponent(Shooter.INSTANCE),
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        Turret.INSTANCE.setManualControl(true);
        driveTrainBinds();

        RobotConfig.bind(P1.triangle(), "SHOOT");
        RobotConfig.bind(P1.square(), "STOP_SHOOT");

        RobotConfig.bind(P2.leftStickX(), "TURRET_ROT");
    }

    @Override
    public void onStartButtonPressed() {

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
