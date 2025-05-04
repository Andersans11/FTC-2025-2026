package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot;

import com.rowanmcalpin.nextftc.core.Subsystem;

public class DemoBotTurret extends Subsystem {
    public static final DemoBotTurret INSTANCE = new DemoBotTurret();
    private DemoBotTurret() { } // nftc boilerplate

    double turretGearPitchDiameter = 40.2; // idk i just put in a random value
    double motorGearPitchDiameter = 38.2;

    public double angleToTicks(double angle, double encoderPPR) {
        double gearRatio = turretGearPitchDiameter / motorGearPitchDiameter;
        double motorRotations = (angle / 360.0) * gearRatio;
        return motorRotations * encoderPPR;
    }

}
