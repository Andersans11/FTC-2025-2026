package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

public class RoyallyFuckedUpMode extends NextFTCOpMode {

    private final Set<BetterSubsystemComponent> subsystems = new HashSet<>();
    public void addSubsystemComponents(BetterSubsystemComponent... subsystemComponents) {
        subsystems.addAll(Arrays.asList(subsystemComponents));
        addComponents(subsystemComponents);
    }

    protected long deltaTime;

    protected GamepadEx P1 = new GamepadEx(() -> this.gamepad1);
    protected GamepadEx P2 = new GamepadEx(() -> this.gamepad2);
    boolean isUpdating = true;

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

        for (BetterSubsystemComponent sys : this.subsystems) {
            sys.initSubsystem();
        }
    }

    @Override
    public void onUpdate() {
        deltaTime = RobotConfig.getDelta();

        telemetry.addData("deltatime (ms)", TimeUnit.MILLISECONDS.convert(deltaTime, TimeUnit.NANOSECONDS));

        if (isUpdating) {
            telemetry.update();
            isUpdating = false;
        } else isUpdating = true;
    }
}