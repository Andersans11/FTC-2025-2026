package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

public class BetterSubsystemGroup implements IAmBetterSubsystem {

    IAmBetterSubsystem[] subsystems;

    public BetterSubsystemGroup(IAmBetterSubsystem... subsystems) {
        this.subsystems = subsystems;
    }

    @Override
    public void initialize() {
        for (IAmBetterSubsystem system : this.subsystems) {
            system.initialize();
        }
    }

    @Override
    public void initSystem() {
        for (IAmBetterSubsystem subsystem : this.subsystems) {
            subsystem.initSystem();
        }
    }

    @Override
    public void preStart() {
        for (IAmBetterSubsystem subsystem : this.subsystems) {
            subsystem.preStart();
        }
    }

    @Override
    public void periodic() {
        for (IAmBetterSubsystem subsystem : this.subsystems) {
            subsystem.periodic();
        }
    }

}
