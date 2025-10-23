package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.components.Component;
import dev.nextftc.core.subsystems.Subsystem;

public class BetterSubsystemComponent implements Component {

    private final IBetterSubsystem subsystem;

    public BetterSubsystemComponent(IBetterSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void preInit() {
        subsystem.initialize();
    }

    @Override
    public void postInit() {
        subsystem.binds();
        subsystem.hardware();
        subsystem.commands();
    }

    @Override
    public void preWaitForStart() {
        this.updateSubsystem();
    }

    @Override
    public void preUpdate() {
        this.updateSubsystem();
    }

    private void updateSubsystem() {
        this.subsystem.periodic();
        if (!CommandManager.INSTANCE.hasCommandsUsing(this.subsystem)) {
            this.subsystem.getDefaultCommand().schedule();
        }
    }
}
