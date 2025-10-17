package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.FieldCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Basic FC Drive")
public class FieldCentric extends NextFTCOpMode {

    long deltaTime;
    GamepadEx P1 = new GamepadEx(() -> this.gamepad1);
    GamepadEx P2 = new GamepadEx(() -> this.gamepad2);

    public FieldCentric() {
        addComponents(
                new SubsystemComponent(FieldCentricDrive.INSTANCE),
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        RobotConfig.initConfig(this, new DeltaTimer());
        RobotConfig.bind(P1.leftTrigger().atLeast(Sensitivities.playerOneLeftTriggerThreshold), "SLOWMODE");

        RobotConfig.bind(P1.leftStickY(), "FB");
        RobotConfig.bind(P1.leftStickX(), "STRAFE");
        RobotConfig.bind(P1.rightStickX(), "YAW");
    }

    @Override
    public void onStartButtonPressed() {}

    @Override
    public void onUpdate() {
        deltaTime = RobotConfig.getDelta();

        telemetry.addData("Deltatime", deltaTime);
    }
}
