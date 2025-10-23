package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import dev.nextftc.core.subsystems.Subsystem;

public interface IBetterSubsystem extends Subsystem {

    void binds();
    void periodic();
    void initialize();
    void hardware();
    void commands();

}
