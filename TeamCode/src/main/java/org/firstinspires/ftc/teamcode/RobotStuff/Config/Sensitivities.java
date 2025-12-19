package org.firstinspires.ftc.teamcode.RobotStuff.Config;


import com.bylazar.configurables.annotations.Configurable;

import java.util.function.Supplier;

@Configurable
public class Sensitivities {

    public static float forwardSensitivity = 1f;
    public static float turningSensitivity = 1f;
    public static float strafeCompSensitivity = 0.25f;
    public static float PIDturningSensitivity = 1f;
    public static float strafingSensitivity = 1f;
    public static float driveModifier = 1f;
    public static float turretTurnSpeed = 1f;
    public static float turretPitchSpeed = 0.1f;
    public static float magazineTurnSpeed = 1f;
    public static float slowmodeModifier = 0.4f;
    public static float defaultDriveModifier = 1f;

    public static Supplier<Float> forwardModifier = () -> driveModifier * forwardSensitivity;
    public static Supplier<Float> strafeModifier = () -> driveModifier * strafingSensitivity;
    public static Supplier<Float> turnModifier = () -> driveModifier * turningSensitivity;
    public static Supplier<Float> strafeCompModifier = () -> driveModifier * strafeCompSensitivity;
    public static Supplier<Float> turnPIDModifier = () -> driveModifier * PIDturningSensitivity;
    public static float p1LTThreshold = 0.1f;
    public static float p1RTThreshold = 0.1f;
    public static float p2LTThreshold = 0.1f;
    public static float p2RTThreshold = 0.1f;
}


