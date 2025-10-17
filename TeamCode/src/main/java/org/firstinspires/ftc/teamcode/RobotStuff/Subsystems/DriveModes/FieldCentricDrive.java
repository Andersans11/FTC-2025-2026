package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;

import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


public class FieldCentricDrive extends DriveMotors {

    GoBildaPinpointDriver pinpoint;
    MecanumDriverControlled vroom;

    public FieldCentricDrive(NextFTCOpMode opMode) { // idk the name could be better
        super(opMode);
        pinpoint = opMode.hardwareMap.get(GoBildaPinpointDriver.class, "sensor");

        this.vroom = new MecanumDriverControlled(
            FL, FR, BL, BR,
            () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
            () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
            () -> (turnSupp.get() * Sensitivities.getTurnModifier()),
            new FieldCentric(
                    () -> (Angle.fromRad(pinpoint.getHeading(AngleUnit.RADIANS)))
            )
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
