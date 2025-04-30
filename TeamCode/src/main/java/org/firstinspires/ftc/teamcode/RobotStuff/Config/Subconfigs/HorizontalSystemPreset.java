package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class HorizontalSystemPreset {
    public double LiftExtension;
    public double ClawPivot;
    public double ClawRotation;
    public double ClawServo;

    public HorizontalSystemPreset(double HLiftExtension, PickupClawPreset pickupClawPreset) {
        this.LiftExtension = DistanceUnit.INCH.fromMm(HLiftExtension);

        this.ClawPivot = pickupClawPreset.PickupClawPivot;
        this.ClawRotation = pickupClawPreset.PickupClawRotation;
        this.ClawServo = pickupClawPreset.PickupClawServo;
    }
}
