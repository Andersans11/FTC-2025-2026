package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;

import java.util.function.Supplier;

import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.HolonomicMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


public class FieldCentricDrive extends DriveMotors {

    GoBildaPinpointDriver pinpoint;

    Supplier<Double> forwardBackward = () -> (forwardSupp.get() * Sensitivities.getForwardModifier());
    Supplier<Double> strafe = () -> (strafeSupp.get() * Sensitivities.getStrafeModifier());
    Supplier<Double> yaw = () -> (turnSupp.get() * Sensitivities.getTurnModifier());
    Supplier<Angle> pinpointHeading = () -> (Angle.fromDeg(pinpoint.getHeading(AngleUnit.DEGREES))) ;
    MecanumDriverControlled vroom;

    public FieldCentricDrive(NextFTCOpMode opMode) { // idk the name could be better
        super(opMode);
        pinpoint = opMode.hardwareMap.get(GoBildaPinpointDriver.class, "sensor");
        this.vroom = new MecanumDriverControlled(FL, FR, BL, BR, forwardBackward, strafe, yaw, new FieldCentric(pinpointHeading));
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
