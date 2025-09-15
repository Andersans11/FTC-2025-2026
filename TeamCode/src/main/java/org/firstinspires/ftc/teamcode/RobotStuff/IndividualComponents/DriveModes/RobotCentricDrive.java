package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

import java.util.function.Supplier;


public class RobotCentricDrive extends DriveMotors {


    Supplier<Double> forwardBackward = () -> (forwardSupp.get() * config.sensitivities.getForwardModifier());
    Supplier<Double> strafe = () -> (strafeSupp.get() * config.sensitivities.getStrafeModifier());
    Supplier<Double> yaw = () -> (turnSupp.get() * config.sensitivities.getTurnModifier());
    MecanumDriverControlled vroom;

    public RobotCentricDrive(NextFTCOpMode opMode, RobotConfig config) { // idk the name could be better
        super(opMode, config);
        this.vroom = new MecanumDriverControlled(FL, FR, BL, BR, forwardBackward, strafe, yaw);
    }

    @Override
    public void updateDrive(long deltaTimeNano) {
        vroom.update();
    }

    @Override
    public void Start() {
        vroom.invoke();
    }
}
