package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

public class RoyallyFuckedUpMode extends NextFTCOpMode {

    protected long deltaTime;

    protected GamepadEx P1 = new GamepadEx(() -> this.gamepad1);
    protected GamepadEx P2 = new GamepadEx(() -> this.gamepad2);

    /**
     * just a class with basic stuff we use in every opmode
     */
    protected RoyallyFuckedUpMode() {
        addComponents(
                BindingsComponent.INSTANCE
        );
    }


    @Override
    public void onInit() {
        RobotConfig.initConfig(this, new DeltaTimer());
    }

    protected void driveTrainBinds() {
        RobotConfig.bind(P1.leftTrigger().atLeast(Sensitivities.playerOneLeftTriggerThreshold), "SLOWMODE");

        RobotConfig.bind(P1.leftStickY(), "FB");
        RobotConfig.bind(P1.leftStickX().negate(), "STRAFE");
        RobotConfig.bind(P1.rightStickX().negate(), "YAW");
    }

    @Override
    public void onUpdate() {
        deltaTime = RobotConfig.getDelta();

        telemetry.addData("Deltatime", deltaTime);
    }


}
