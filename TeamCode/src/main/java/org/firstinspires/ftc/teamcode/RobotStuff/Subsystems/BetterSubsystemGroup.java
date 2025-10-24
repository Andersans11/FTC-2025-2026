package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import dev.nextftc.core.subsystems.Subsystem;

public class BetterSubsystemGroup implements IBetterSubsystem {

    IBetterSubsystem[] subsystems;

    public BetterSubsystemGroup(IBetterSubsystem... subsystems) {
        this.subsystems = subsystems;
    }

    @Override
    public void initialize() {
        for (IBetterSubsystem system : this.subsystems) {
            system.initialize();
        }
    }

    @Override
    public void hardware() {
        for (IBetterSubsystem subsystem : this.subsystems) {
            subsystem.hardware();
        }
    }

    @Override
    public void commands() {
        for (IBetterSubsystem subsystem : this.subsystems) {
            subsystem.commands();
        }
    }

    @Override
    public void binds() {
        for (IBetterSubsystem subsystem : this.subsystems) {
            subsystem.binds();
        }
    }

    @Override
    public void periodic() {
        for (IBetterSubsystem subsystem : this.subsystems) {
            subsystem.periodic();
        }
    }

}
