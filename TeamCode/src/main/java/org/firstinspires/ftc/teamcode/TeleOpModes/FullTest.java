package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Test: Full")
public class FullTest extends NextFTCOpMode {

    long deltaTime;

    GamepadEx P1 = new GamepadEx(() -> this.gamepad1);
    GamepadEx P2 = new GamepadEx(() -> this.gamepad2);

    public FullTest() {
        addComponents(
                new SubsystemComponent(RobotCentricDrive.INSTANCE, Perseus.INSTANCE),
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        RobotConfig.initConfig(this, new DeltaTimer());

    }

    @Override
    public void onStartButtonPressed() {
        RobotConfig.bind(P1.leftTrigger().atLeast(Sensitivities.playerOneLeftTriggerThreshold), "SLOWMODE");

        RobotConfig.bind(P1.leftStickY(), "FB");
        RobotConfig.bind(P1.leftStickX(), "STRAFE");
        RobotConfig.bind(P1.rightStickX(), "YAW");

        P1.rightTrigger().atLeast(Sensitivities.playerOneRightTriggerThreshold).whenBecomesTrue(Perseus.INSTANCE.intake());
        P1.rightTrigger().atLeast(Sensitivities.playerOneRightTriggerThreshold).whenBecomesFalse(Perseus.INSTANCE.stopIntake());

        P2.rightTrigger().atLeast(Sensitivities.playerTwoRightTriggerThreshold).whenBecomesTrue(Perseus.INSTANCE.shootMotif());
        P2.a().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.GREEN));
        P2.b().whenBecomesTrue(Perseus.INSTANCE.shootSingle(Utils.ArtifactTypes.PURPLE));

        Perseus.INSTANCE.manualTurretPassAxis(P2.leftStickX().get());


        Perseus.INSTANCE.stopIntake();
    }

    @Override
    public void onUpdate() {
        deltaTime = RobotConfig.getDelta();

        telemetry.addData("Deltatime", deltaTime);
    }
}
