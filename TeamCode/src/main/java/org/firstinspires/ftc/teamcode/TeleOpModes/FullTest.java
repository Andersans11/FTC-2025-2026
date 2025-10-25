package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

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
        RobotConfig.bind(P2.circle().and(P2.start().not()), "SHOOT_PURPLE");
        RobotConfig.bind(P2.cross().and(P2.start().not()), "SHOOT_GREEN");
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
        super.telemetry.addData("Target Pos", Magazine.INSTANCE.getTargetPos());
        super.telemetry.addData("Actual Pos", Magazine.INSTANCE.getActualPos());
        super.telemetry.addData("at target: ", Magazine.INSTANCE.servos[0].isAtTarget());
        super.telemetry.addData("Mode: ", Magazine.INSTANCE.getModeString());
        super.telemetry.addData("Active Slot: ", Magazine.INSTANCE.activeSlot);
        super.telemetry.addData("Magazine Turret Pos: ", Magazine.INSTANCE.turretPos);
        super.telemetry.addData("Turret Pos: ", Turret.INSTANCE.getTurretPos());
        super.telemetry.addData("Slot 0: ", Magazine.INSTANCE.getContent(0));
        super.telemetry.addData("Slot 1: ", Magazine.INSTANCE.getContent(1));
        super.telemetry.addData("Slot 2: ", Magazine.INSTANCE.getContent(2));
        super.telemetry.addData("Updating: ", Magazine.INSTANCE.yej);
        super.telemetry.addData("tre", Magazine.INSTANCE.tre);
        super.telemetry.addData("fal", Magazine.INSTANCE.fal);
    }
}
