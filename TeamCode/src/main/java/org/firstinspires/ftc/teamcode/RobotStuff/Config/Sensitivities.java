package org.firstinspires.ftc.teamcode.RobotStuff.Config;


import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class Sensitivities {

    public static float forwardSensitivity = 1;
    public static float turningSensitivity = 1;
    public static float PIDturningSensitivity = 1;
    public static float strafingSensitivity = 1;
    public static float driveModifier = 1f;
    public static float turretTurnSpeed = 1f;
    public static float turretPitchSpeed = 0.1f;
    public static float magazineTurnSpeed = 1f;

    public static float getForwardModifier() {
        return driveModifier * forwardSensitivity;
    }

    public static float getStrafeModifier() {
        return driveModifier * strafingSensitivity;
    }

    public static float getTurnModifier() {
        return driveModifier * turningSensitivity;
    }

    public static float getPIDTurnModifier() {
        return driveModifier * PIDturningSensitivity;
    }

    public static float p1LTThreshold = 0.1f;
    public static float p1RTThreshold = 0.1f;
    public static float p2LTThreshold = 0.1f;
    public static float p2RTThreshold = 0.1f;
}


