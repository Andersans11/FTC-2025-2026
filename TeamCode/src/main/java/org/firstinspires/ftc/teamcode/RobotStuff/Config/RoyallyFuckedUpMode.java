package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    @Deprecated(since = "Panels telemetry was added, use telemetryManager instead.")
    protected Telemetry telemetry;

    protected TelemetryManager telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

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
        if (isUpdating) {
            telemetryManager.update(super.telemetry);
            isUpdating = false;
        } else isUpdating = true;

        deltaTime = RobotConfig.getDelta();

        telemetryManager.addData("deltatime (ms)", TimeUnit.MILLISECONDS.convert(deltaTime, TimeUnit.NANOSECONDS));
    }
}