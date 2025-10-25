package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

@TeleOp(name = "perseus throws an artifact and hits eddie in the face", group = Utils.PRIORITY)
public class FullTest extends RoyallyFuckedUpMode {

    public FullTest() {
        super();
        addComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
        Perseus.INSTANCE.setManualControlTurret(true);
        driveTrainBinds();

        RobotConfig.bind(P2.rightTrigger().atLeast(Sensitivities.playerTwoRightTriggerThreshold), "SHOOT_MOTIF");
        RobotConfig.bind(P2.circle(), "SHOOT_PURPLE");
        RobotConfig.bind(P2.cross(), "SHOOT_GREEN");
        RobotConfig.bind(P1.rightTrigger().atLeast(Sensitivities.playerOneRightTriggerThreshold), "INTAKE");
        RobotConfig.bind(P2.square(), "INTAKE_MODE");
        RobotConfig.bind(P2.triangle(), "OUTTAKE_MODE");

        RobotConfig.bind(P2.leftStickX(), "TURRET_ROT");
    }

    @Override
    public void onStartButtonPressed() {}

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
