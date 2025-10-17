package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

public class WheelTestMode extends DriveMotors {
    MecanumDriverControlled vroom;

    public WheelTestMode(NextFTCOpMode opMode) {
        super(opMode);
        this.vroom = new MecanumDriverControlled(
            FL, FR, BL, BR,
            () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
            () -> 0.0,
            () -> 0.0
        );
    }

    @Override
    public void update(long deltaTimeNano) {
        vroom.update();
    } // only actually needed for holdHeading because of pid stuff, doesn't need to be called here

    @Override
    public void schedule() {
        vroom.schedule();
    }
}