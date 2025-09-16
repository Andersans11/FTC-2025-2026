package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;


import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;

public class ReadyPlayerOne { // just a wrapper so that we can have custom names and some advanced management
// yes we are wrapping a wrapper but don't pay attention to that
    GamepadEx gamepadEx;
    public ReadyPlayerOne(Gamepad gamepad) {
        if (gamepad == null) {
            throw new NullPointerException("you can't drive without the inputs you goober");
        }

        this.gamepadEx = new GamepadEx(() -> gamepad);
    }


    ///////////////////////////////////////--- X/Y/A/B BUTTONS ---/////////////////////////////////////////



    public Button Cross = new Button(() -> gamepadEx.cross().get());
    public Button Circle = new Button(() -> gamepadEx.circle().get());
    public Button Square = new Button(() -> gamepadEx.square().get());
    public Button Triangle = new Button(() -> gamepadEx.triangle().get());

    public void update_buttons() {
        Cross.update("tele");
        Circle.update("tele");
        Square.update("tele");
        Triangle.update("tele");
    }



    ///////////////////////////////////////--- BUMPERS ---/////////////////////////////////////////

    public Button LeftBumper = new Button(() -> gamepadEx.leftBumper().get());
    public Button RightBumper = new Button(() -> gamepadEx.rightBumper().get());

    public void update_bumpers() {
        LeftBumper.update("tele");
        RightBumper.update("tele");
    }


    ///////////////////////////////////////--- DPAD ---/////////////////////////////////////////

    public Button DpadUp = new Button(() -> gamepadEx.dpadUp().get());
    public Button DpadDown = new Button(() -> gamepadEx.dpadDown().get());
    public Button DpadLeft = new Button(() -> gamepadEx.dpadLeft().get());
    public Button DpadRight = new Button(() -> gamepadEx.dpadRight().get());

    public void update_dpad() {
        DpadUp.update("tele");
        DpadDown.update("tele");
        DpadLeft.update("tele");
        DpadRight.update("tele");
    }


    ///////////////////////////////////////--- LEFT JOYSTICK ---/////////////////////////////////////////

    public Range ForwardAxis = new Range(
            () -> gamepadEx.leftStickY().get()
    );

    public Range StrafeAxis = new Range(
            () -> gamepadEx.leftStickX().get()
    );

    public Button LeftButton = new Button(() -> gamepadEx.leftStickButton().get());

    public void update_left() {
        ForwardAxis.update();
        StrafeAxis.update();
        LeftButton.update("tele");
    }


    ///////////////////////////////////////--- RIGHT JOYSTICK ---/////////////////////////////////////////



    public Range TurnAxis = new Range(
            () -> gamepadEx.rightStickX().get()
    );

    public Range RightY = new Range(
            () -> gamepadEx.rightStickY().get()
    );

    public Button RightButton = new Button(() -> gamepadEx.rightStickButton().get());

    public void update_right() {
        TurnAxis.update();
        RightY.update();
        RightButton.update("tele");
    }


    ///////////////////////////////////////--- TRIGGERS ---/////////////////////////////////////////


    public Button SlowDown = gamepadEx.leftTrigger().atLeast(Sensitivities.playerOneLeftTriggerThreshold)
            .whenTrue(() -> Sensitivities.driveModifier = 0.4f)
            .whenFalse(() -> Sensitivities.driveModifier = 1f);
    public Button RightTrigger = gamepadEx.rightTrigger().atLeast(Sensitivities.playerOneRightTriggerThreshold);

    public void update_triggers() {
        SlowDown.update("tele");
        RightTrigger.update("tele");
    }
    public void update_all() {
        update_buttons();
        update_bumpers();
        update_dpad();
        update_left();
        update_right();
        update_triggers();
    }


}
