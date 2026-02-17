package org.firstinspires.ftc.teamcode.RobotStuff.Config;

public class RRoboticsSubsystemGroup implements IRRoboticsSubsystem {

    IRRoboticsSubsystem[] subsystems;

    public RRoboticsSubsystemGroup(IRRoboticsSubsystem... subsystems) {
        this.subsystems = subsystems;
    }

    @Override
    public void initialize() {
        for (IRRoboticsSubsystem system : this.subsystems) {
            system.initialize();
        }
    }

    @Override
    public void initSystem() {
        for (IRRoboticsSubsystem subsystem : this.subsystems) {
            subsystem.initSystem();
        }
    }

    @Override
    public void preStart() {
        for (IRRoboticsSubsystem subsystem : this.subsystems) {
            subsystem.preStart();
        }
    }

    @Override
    public void periodic() {
        for (IRRoboticsSubsystem subsystem : this.subsystems) {
            subsystem.periodic();
        }
    }

}
