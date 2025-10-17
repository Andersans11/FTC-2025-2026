package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;

import dev.nextftc.core.units.Angle;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


public class FieldCentricDrive extends DriveMotors {

    public static final FieldCentricDrive INSTANCE = new FieldCentricDrive();

    GoBildaPinpointDriver pinpoint;
    MecanumDriverControlled vroom;

    @Override
    public void periodic() {
        super.periodic();
    }

    @Override
    public void initialize() {
        pinpoint = RobotConfig.Pinpoint;

        this.vroom = new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
                () -> (turnSupp.get() * Sensitivities.getTurnModifier()),
                new FieldCentric(
                        () -> (Angle.fromRad(pinpoint.getHeading(AngleUnit.RADIANS)))
                )
        );

        vroom.schedule();
    }
}
