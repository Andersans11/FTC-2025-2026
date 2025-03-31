package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import kotlin.Pair;

public class LiftBase extends Subsystem {
    // BOILERPLATE
    public static final LiftBase INSTANCE = new LiftBase();
    private LiftBase() { }



    // USER CODE
    public MotorEx motor;

    public PIDFController controller = new PIDFController(new PIDCoefficients(0.005, 0.0, 0.0));

    public String name = "liftR";

    public enum liftStates {
        min,
        mid,
        max,
    }

    public liftStates liftState = liftStates.min;

    /*
    using rev 6000 rpm motors - 28 ppr

    12:1 gear ratio > 336 ppr
     */

    public Command liftUp() {
        switch (liftState) {
            case min:
                liftState = liftStates.mid;
                return toMiddle();
            case mid:
                liftState = liftStates.max;
                return toHigh();
            default:
                return toHigh();
        }
    }

    public Command liftDown() {
        switch (liftState) {
            case max:
                liftState = liftStates.mid;
                return toMiddle();
            case mid:
                liftState = liftStates.min;
                return toLow();
            default:
                return toLow();
        }
    }

    public double mmToTicks(double desiredExtension, double encoderPPR) {
        return (-1 * desiredExtension) * (encoderPPR / (38.2 * Math.PI)); // encoders are backwards
    }


    public Command toLow() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                mmToTicks(0, 751.8), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toMiddle() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                mmToTicks(172.975, 751.8), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toHigh() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                mmToTicks(345.95, 751.8 ), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command moveLift(Pair<Float, Float> JoystickValues) {

        double joystickX = (double) (JoystickValues.getFirst());

        return new SetPower(
                motor,
                joystickX,
                this
        );
    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }

    @NonNull
    @Override
    public Command getDefaultCommand() {
        return new HoldPosition(
                motor,
                controller,
                this);
    }
}
