package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

import java.util.function.Supplier;


public class RobotCentricDrive extends DriveMotors {

    public static final RobotCentricDrive INSTANCE = new RobotCentricDrive();
    MecanumDriverControlled vroom;

    @Override
    public void commands() {
        this.vroom = new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
                () -> (turnSupp.get() * Sensitivities.getTurnModifier())
        );

        this.vroom.schedule();
    }
}
