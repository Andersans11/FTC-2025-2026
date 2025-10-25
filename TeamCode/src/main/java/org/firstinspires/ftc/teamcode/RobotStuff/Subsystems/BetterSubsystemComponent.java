package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.components.Component;

public class BetterSubsystemComponent implements Component {

    private final IAmBetterSubsystem subsystem;

    public BetterSubsystemComponent(IAmBetterSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void preInit() {
        subsystem.initialize();
    }

    public void initSubsystem() {
        subsystem.initSystem();
    }

    @Override
    public void preStartButtonPressed() {
        subsystem.preStart();
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
