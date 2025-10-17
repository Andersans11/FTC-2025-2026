package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

import java.util.function.Supplier;


public class RobotCentricDrive extends DriveMotors {


    Supplier<Double> forwardBackward = () -> (forwardSupp.get() * Sensitivities.getForwardModifier());
    Supplier<Double> strafe = () -> (strafeSupp.get() * Sensitivities.getStrafeModifier());
    Supplier<Double> yaw = () -> (turnSupp.get() * Sensitivities.getTurnModifier());
    MecanumDriverControlled vroom;

    public RobotCentricDrive(NextFTCOpMode opMode) { // idk the name could be better
        super(opMode);

        this.vroom = new MecanumDriverControlled(
            FL, FR, BL, BR,
            () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
            () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
            () -> (turnSupp.get() * Sensitivities.getTurnModifier())
        );
    }

    @Override
    public void update(long deltaTimeNano) {
        vroom.update();
    }

    @Override
    public void schedule() {
        vroom.schedule();
    }


}
