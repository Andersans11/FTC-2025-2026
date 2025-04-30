package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

public class FullPreset {
    public double HLiftExt;
    public double VLiftExt;
    public double PickupClawPivot;
    public double PickupClawRotation;
    public double PickupClawServo;
    public double DepositArmPivot;
    public double DepositClawPivot;
    public double DepositClawServo;

    public FullPreset(HorizontalSystemPreset horizontalSystemPreset, VerticalSystemPreset verticalSystemPreset) {
        this.HLiftExt = horizontalSystemPreset.LiftExtension;
        this.VLiftExt = verticalSystemPreset.LiftExtension;

        this.PickupClawPivot = horizontalSystemPreset.ClawPivot;
        this.PickupClawRotation = horizontalSystemPreset.ClawRotation;
        this.PickupClawServo = horizontalSystemPreset.ClawServo;

        this.DepositArmPivot = verticalSystemPreset.ArmPivot;
        this.DepositClawPivot = verticalSystemPreset.ClawPivot;
        this.DepositClawServo = verticalSystemPreset.ClawServo;
    }
}

