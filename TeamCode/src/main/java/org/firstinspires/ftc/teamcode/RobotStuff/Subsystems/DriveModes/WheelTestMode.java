package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

public class WheelTestMode extends DriveMotors {

    public static final WheelTestMode INSTANCE = new WheelTestMode();
    MecanumDriverControlled vroom;

    @Override
    public void initialize() {
        this.vroom = new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> 0.0,
                () -> 0.0
        );

        this.vroom.schedule();
    }

    @Override
    public void periodic() {
        super.periodic();
    }
}