package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class VerticalSystemPreset {

    public double LiftExtension;
    public double ArmPivot;
    public double ClawPivot;
    public double ClawServo;

    public VerticalSystemPreset(double VLiftExtension, DepositArmPreset depositArmPreset) {
        this.LiftExtension = DistanceUnit.INCH.fromMm(VLiftExtension);

        this.ArmPivot = depositArmPreset.DepositArmPivot;
        this.ClawPivot = depositArmPreset.DepositClawPivot;
        this.ClawServo = depositArmPreset.DepositClawServo;
    }
}
