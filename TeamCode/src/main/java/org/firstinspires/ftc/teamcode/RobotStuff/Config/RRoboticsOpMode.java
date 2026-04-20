package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

public class RRoboticsOpMode extends NextFTCOpMode {

    private final Set<RRoboticsSubsystemComponent> subsystems = new HashSet<>();
    public void addSubsystemComponents(RRoboticsSubsystemComponent... subsystemComponents) {
        subsystems.addAll(Arrays.asList(subsystemComponents));
        addComponents(subsystemComponents);
    }

    protected long deltaTime;

    public TelemetryManager telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

    protected GamepadEx P1 = new GamepadEx(() -> this.gamepad1);
    protected GamepadEx P2 = new GamepadEx(() -> this.gamepad2);
    boolean isUpdating = true;

    /**
     * just a class with basic stuff we use in every opmode
     */
    protected RRoboticsOpMode() {
        addComponents(
                BindingsComponent.INSTANCE
        );
    }


    @Override
    public void onInit() {
        RobotConfig.initConfig(this, new DeltaTimer());
        Turret.INSTANCE.hasGotMotif = false;
        Turret.INSTANCE.hasSetAlliance = false;

        for (RRoboticsSubsystemComponent sys : this.subsystems) {
            sys.initSubsystem();
        }
    }

    public void addData(String key, Object value) {
        if (isUpdating) {
            telemetryManager.addData(key, value == null ? "NULLVALUE" : value);
        }
    }

    public void addLine(String line) {
        telemetryManager.addLine(line);
    }

    @Override
    public void onUpdate() {
        deltaTime = RobotConfig.getDelta();

        addData("deltatime (ms)", TimeUnit.MILLISECONDS.convert(deltaTime, TimeUnit.NANOSECONDS));

        if (isUpdating) {
            telemetryManager.update(telemetry);
            isUpdating = false;
        } else isUpdating = true;
    }
}