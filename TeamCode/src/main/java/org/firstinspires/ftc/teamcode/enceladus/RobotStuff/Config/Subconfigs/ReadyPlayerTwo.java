package org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs;

import com.rowanmcalpin.nextftc.ftc.gamepad.Button;
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx;
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick;
import com.rowanmcalpin.nextftc.ftc.gamepad.JoystickAxis;
import com.rowanmcalpin.nextftc.ftc.gamepad.Trigger;

import kotlin.jvm.functions.Function0;

public class ReadyPlayerTwo {

    GamepadEx gamepadEx;

    public ReadyPlayerTwo(GamepadEx gamepadEx) {
        if (gamepadEx == null) {
            throw new NullPointerException("you can't drive without the inputs you goober");
        }
        this.gamepadEx = gamepadEx;
    }
    ///////////////////////////////////////--- X/Y/A/B BUTTONS ---/////////////////////////////////////////
    Function0<Boolean> getA = () -> gamepadEx.getA().getState();
    Function0<Boolean> getB = () -> gamepadEx.getB().getState();
    Function0<Boolean> getX = () -> gamepadEx.getX().getState();
    Function0<Boolean> getY = () -> gamepadEx.getY().getState();
    Function0<Boolean> getTriangle = () -> gamepadEx.getY().getState();
    Function0<Boolean> getSquare = () -> gamepadEx.getX().getState();
    Function0<Boolean> getCross = () -> gamepadEx.getA().getState();
    Function0<Boolean> getCircle = () -> gamepadEx.getB().getState();

    public Button A = new Button(getA);
    public Button B = new Button(getB);
    public Button X = new Button(getX);
    public Button Y = new Button(getY);
    public Button Cross = new Button(getCross);
    public Button Circle = new Button(getCircle);
    public Button Square = new Button(getSquare);
    public Button Triangle = new Button(getTriangle);

    public void update_buttons() {
        A.update();
        B.update();
        X.update();
        Y.update();
        Cross.update();
        Circle.update();
        Square.update();
        Triangle.update();
    }

    ///////////////////////////////////////--- BUMPERS ---/////////////////////////////////////////
    Function0<Boolean> getLBumper = () -> gamepadEx.getLeftBumper().getState();
    Function0<Boolean> getRBumper = () -> gamepadEx.getRightBumper().getState();

    public Button LeftBumper = new Button(getLBumper);
    public Button RightBumper = new Button(getRBumper);

    public void update_bumpers() {
        LeftBumper.update();
        RightBumper.update();
    }


    ///////////////////////////////////////--- DPAD ---/////////////////////////////////////////
    Function0<Boolean> getDpadUp = () -> gamepadEx.getDpadUp().getState();
    Function0<Boolean> getDpadDown = () -> gamepadEx.getDpadDown().getState();
    Function0<Boolean> getDpadLeft = () -> gamepadEx.getDpadLeft().getState();
    Function0<Boolean> getDpadRight = () -> gamepadEx.getDpadRight().getState();

    public Button DpadUp = new Button(getDpadUp);
    public Button DpadDown = new Button(getDpadDown);
    public Button DpadLeft = new Button(getDpadLeft);
    public Button DpadRight = new Button(getDpadRight);

    public void update_dpad() {
        DpadUp.update();
        DpadDown.update();
        DpadLeft.update();
        DpadRight.update();
    }


    ///////////////////////////////////////--- LEFT JOYSTICK ---/////////////////////////////////////////
    Function0<Float> getLeftX = () -> gamepadEx.getLeftStick().getX();
    Function0<Float> getLeftY = () -> gamepadEx.getLeftStick().getY();
    Function0<Boolean> getLeftButton = () -> gamepadEx.getLeftStick().getButton().getState();

    public Joystick LeftStick = new Joystick(
            getLeftX,
            getLeftY,
            getLeftButton,
            Sensitivities.playerTwoLeftXAxisThreshold,
            Sensitivities.playerTwoLeftYAxisThreshold,
            Sensitivities.isPlayerTwoLeftYInverted //NextFTC only does vertical inversion for the Joystick object
    );

    public JoystickAxis LeftY = new JoystickAxis(
            getLeftY,
            Sensitivities.playerTwoLeftYAxisThreshold,
            Sensitivities.isPlayerTwoLeftYInverted
    );

    public JoystickAxis LeftX = new JoystickAxis(
            getLeftX,
            Sensitivities.playerTwoLeftXAxisThreshold,
            Sensitivities.isPlayerTwoLeftXInverted
    );

    public Button LeftButton = new Button(getLeftButton);

    public void update_left() {
        LeftStick.update();
        LeftY.update();
        LeftX.update();
        LeftButton.update();
    }


    ///////////////////////////////////////--- RIGHT JOYSTICK ---/////////////////////////////////////////
    Function0<Float> getRightX = () -> gamepadEx.getRightStick().getX();
    Function0<Float> getRightY = () -> gamepadEx.getRightStick().getY();
    Function0<Boolean> getRightButton = () -> gamepadEx.getRightStick().getButton().getState();

    public Joystick RightStick = new Joystick(
            getRightX,
            getRightY,
            getRightButton,
            Sensitivities.playerTwoRightXAxisThreshold,
            Sensitivities.playerTwoRightYAxisThreshold,
            Sensitivities.isPlayerTwoRightYInverted //NextFTC only does vertical, not horizontal inversion for the Joystick object
    );

    public JoystickAxis RightX = new JoystickAxis(
            getRightX,
            Sensitivities.playerTwoRightXAxisThreshold,
            Sensitivities.isPlayerTwoRightXInverted
    );

    public JoystickAxis RightY = new JoystickAxis(
            getRightY,
            Sensitivities.playerTwoRightYAxisThreshold,
            Sensitivities.isPlayerTwoRightYInverted
    );

    public Button RightButton = new Button(getRightButton);

    public void update_right() {
        RightStick.update();
        RightX.update();
        RightY.update();
        RightButton.update();
    }


    ///////////////////////////////////////--- TRIGGERS ---/////////////////////////////////////////
    Function0<Float> getLTrigger = () -> gamepadEx.getLeftTrigger().getValue();
    Function0<Float> getRTrigger = () -> gamepadEx.getRightTrigger().getValue();

    public Trigger LeftTrigger = new Trigger(getLTrigger, Sensitivities.playerTwoLeftTriggerThreshold);
    public Trigger RightTrigger = new Trigger(getRTrigger, Sensitivities.playerTwoRightTriggerThreshold);

    public void update_triggers() {
        LeftTrigger.update();
        RightTrigger.update();
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
