package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.acmerobotics.dashboard.config.Config;

@Config
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

    public static float playerOneLeftTriggerThreshold = 0.0f;
    public static float playerOneRightTriggerThreshold = 0.0f;
    public static float playerTwoLeftTriggerThreshold = 0.0f;
    public static float playerTwoRightTriggerThreshold = 0.0f;
}


