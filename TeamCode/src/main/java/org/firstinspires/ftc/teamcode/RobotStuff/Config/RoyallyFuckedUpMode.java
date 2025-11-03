package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.NextFTCOpMode;

import com.bylazar.gamepad.GamepadManager;
import com.bylazar.gamepad.PanelsGamepad;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.Gamepad;

public class RoyallyFuckedUpMode extends NextFTCOpMode {

    private final Set<BetterSubsystemComponent> subsystems = new HashSet<>();
    public void addSubsystemComponents(BetterSubsystemComponent... subsystemComponents) {
        subsystems.addAll(Arrays.asList(subsystemComponents));
        addComponents(subsystemComponents);
    }

    protected long deltaTime;

    protected GamepadEx P1 = new GamepadEx(() -> this.gamepad1);
    protected GamepadEx P2 = new GamepadEx(() -> this.gamepad2);
    protected GamepadManager GM1 = PanelsGamepad.INSTANCE.getFirstManager();
    protected GamepadManager GM2 = PanelsGamepad.INSTANCE.getSecondManager();
    protected TelemetryManager TM = PanelsTelemetry.INSTANCE.getTelemetry();

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
        Gamepad gp1 = GM1.asCombinedFTCGamepad(gamepad1);
        Gamepad gp2 = GM2.asCombinedFTCGamepad(gamepad2);

        deltaTime = RobotConfig.getDelta();

        telemetry.addData("Deltatime", deltaTime / 1000000);

        TM.update(telemetry);
    }


}
