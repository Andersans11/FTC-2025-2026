package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import dev.nextftc.core.subsystems.Subsystem;

public interface IAmBetterSubsystem extends Subsystem {

    /**
     * Code to be run on the initialization of a subsystem.
     * This should be used as an alternative to initialize(), as that
     * runs before the HardwareMap has initialize and thus causes problems
     */
    void initSystem();
    void preStart();

}
