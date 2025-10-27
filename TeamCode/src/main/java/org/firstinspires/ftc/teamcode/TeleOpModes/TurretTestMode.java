package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.HoldHeadingPID;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.utility.InstantCommand;

@TeleOp(name = "fuck you", group = Utils.TESTING)
public class TurretTestMode extends RoyallyFuckedUpMode {

    public TurretTestMode() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(NewTurret.INSTANCE),
                new BetterSubsystemComponent(HoldHeadingPID.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        //P1.dpadDown().whenBecomesTrue(NewTurret.INSTANCE.setPosition(0));
        //P1.dpadRight().whenBecomesTrue(NewTurret.INSTANCE.setPosition(90));
        //P1.dpadLeft().whenBecomesTrue(NewTurret.INSTANCE.setPosition(-90));
        //P1.dpadUp().whenBecomesTrue(NewTurret.INSTANCE.resetPID());
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        super.telemetry.addData("targetAngle", NewTurret.INSTANCE.targetAngle);
        super.telemetry.addData("motorPower", NewTurret.INSTANCE.controller.calculate(NewTurret.INSTANCE.rotationMotor.getState()));
        super.telemetry.addData("goal", NewTurret.INSTANCE.controller.getGoal());
        super.telemetry.addData("length", NewTurret.INSTANCE.camera.blocks().length);
        super.telemetry.addData("waugh", NewTurret.INSTANCE.waugh());
    }
}
