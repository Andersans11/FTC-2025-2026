package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;

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
        this.vroom = new MecanumDriverControlled(FL, FR, BL, BR, forwardBackward, strafe, yaw);
    }

    @Override
    public void update(long deltaTimeNano) {
        vroom.update();
    }

    @Override
    public void invoke() {
        vroom.invoke();
    }


}
