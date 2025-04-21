package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Sensitivities {

    public static float driveSensitivity = 1;

    public float getDriveSensitivity() {
        return driveSensitivity;
    }

    public static float forwardSensitivity = 1; // basic driving sensitivities only relative to each other

    public float getForwardSensitivity() {
        return forwardSensitivity;
    }

    public static float turningSensitivity = .69f;

    public float getTurningSensitivity() {
        return turningSensitivity;
    }

    public static float turningRateDPS = 130;

    public float getTurningRateDPS() {
        return turningRateDPS;
    }

    public static float strafingSensitivity = 1;

    public float getStrafingSensitivity() {
        return strafingSensitivity;
    }

    public static float slowDownModifier = 0.4f;

    public float getSlowDownModifier(){return slowDownModifier;}

    // ----------- PLAYER ONE --------- //
    public static float playerOneLeftXAxisThreshold = 0.0f;
    public static float playerOneLeftYAxisThreshold = 0.0f;
    public static float playerOneRightXAxisThreshold = 0.0f;
    public static float playerOneRightYAxisThreshold = 0.0f;
    public static boolean isPlayerOneLeftXInverted = true;
    public static boolean isPlayerOneLeftYInverted = true;
    public static boolean isPlayerOneRightXInverted = true;
    public static boolean isPlayerOneRightYInverted = true;
    public static float playerOneLeftTriggerThreshold = 0.0f;
    public static float playerOneRightTriggerThreshold = 0.0f;

    // -------- PLAYER TWO --------- //
    public static float playerTwoLeftXAxisThreshold = 0.0f;
    public static float playerTwoLeftYAxisThreshold = 0.0f;
    public static float playerTwoRightXAxisThreshold = 0.0f;
    public static float playerTwoRightYAxisThreshold = 0.0f;
    public static boolean isPlayerTwoLeftXInverted = false;
    public static boolean isPlayerTwoLeftYInverted = true;
    public static boolean isPlayerTwoRightXInverted = false;
    public static boolean isPlayerTwoRightYInverted = true;
    public static float playerTwoLeftTriggerThreshold = 0.0f;
    public static float playerTwoRightTriggerThreshold = 0.0f;
}


