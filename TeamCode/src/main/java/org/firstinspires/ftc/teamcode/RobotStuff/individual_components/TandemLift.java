package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import kotlin.Pair;

public class TandemLift extends Subsystem {
    // BOILERPLATE
    public static final TandemLift INSTANCE = new TandemLift();
    private TandemLift() { }



    // USER CODE
    public MotorEx right_motor;
    public MotorEx left_motor;

    double maxExtension = 618; // in mm

    public MotorGroup liftMotors;

    public PIDFController controller = new PIDFController(new PIDCoefficients(0.005, 0.0, 0.0));

    public String right_name = "liftR";
    public String left_name = "liftL";

    public enum liftStates {
        min,
        mid,
        max,
    }

    private liftStates liftState = liftStates.min;

    /*
    using rev 6000 rpm motors - 28 ppr

    15:1 gear ratio > 420 ppr
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

    public double mmToTicks(double desiredExtension, double encoderPPR, double ratio) {
        desiredExtension = Math.min(desiredExtension, maxExtension);
        return desiredExtension * ((encoderPPR * ratio) / (38.2 * Math.PI));
    }


    public Command toLow() {
        return new RunToPosition(liftMotors, // MOTOR TO MOVE
                mmToTicks(0, 28, 15), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toMiddle() {
        return new RunToPosition(liftMotors, // MOTOR TO MOVE
                mmToTicks(309, 28, 15), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toHigh() {
        return new RunToPosition(liftMotors, // MOTOR TO MOVE
                mmToTicks(618, 28, 15), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command moveLift(Pair<Float, Float> JoystickValues) {

        double joystickX = (double) (JoystickValues.getFirst());

        return new SetPower(
                liftMotors,
                joystickX,
                this
        );
    }

    @Override
    public void initialize() {
        right_motor = new MotorEx(right_name);
        left_motor = new MotorEx(left_name);

        liftMotors = new MotorGroup(left_motor, right_motor);
    }

    @NonNull
    @Override
    public Command getDefaultCommand() {
        return new HoldPosition(
                liftMotors,
                controller,
                this);
    }
}
