package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;

import java.util.function.Supplier;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

public class WheelTestMode extends DriveMotors {

    Supplier<Double> forwardBackward = () -> (forwardSupp.get() * Sensitivities.getForwardModifier());
    Supplier<Double> strafe = () -> 0.0;
    Supplier<Double> yaw = () -> 0.0;

    MecanumDriverControlled vroom;

    public WheelTestMode(NextFTCOpMode opMode) { // idk the name could be better
        super(opMode);
        this.vroom = new MecanumDriverControlled(FL, FR, BL, BR, forwardBackward, strafe, yaw);
    }

    @Override
    public void update(long deltaTimeNano) {
        vroom.update();
    } // only actually needed for holdHeading because of pid stuff, doesn't need to be called here

    @Override
    public void invoke() {
        vroom.invoke();
    }
}