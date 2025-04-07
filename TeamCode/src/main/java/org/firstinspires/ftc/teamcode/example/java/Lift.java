package org.firstinspires.ftc.teamcode.example.java;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

public class Lift extends Subsystem {
    // BOILERPLATE
    public static final Lift INSTANCE = new Lift();
    private Lift() { }



    // USER CODE
    public MotorEx motor;

    public PIDFController controller = new PIDFController(new PIDCoefficients(0.005, 0.0, 0.0));

    public String name = "liftR";

    public enum liftStates {
        min,
        mid,
        max,
    }

    private liftStates liftState = liftStates.min;

    /*
    both rev and gobilda 6000rpm motors have 28 counts per revolution
    28:360
    pitch diameter of gt2 mount is 38.2
    circumference is 38.2π
    38.2π:360

    checks out according to the gobilda website:
    "A 60 Tooth GT2 Pulley drives this kit 120mm per rotation."
    38.2 * π = ~120

    28 encoder counts per 38.2π mm of extension

    for reference
    rpm  | ppr
    6000 | 28
    1620 | 103.8
    1150 | 145.1
    435  | 384.5
    312  | 537.7
    223  | 751.8
    117  | 1,425.1
    84   | 1,993.6
    60   | 2,786.2
    43   | 3895.9
    30   | 5,281.1

    435 rpm is the fastest recommended motor, can extend full 240mm viper slide kit in just under a second

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

    public double mmToTicks(double desiredExtension, double encoderPPR) { // no one should let me name anything
        return desiredExtension * (encoderPPR / (38.2 * Math.PI));
    }


    public Command toLow() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                mmToTicks(0, 537.7), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toMiddle() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                mmToTicks(172.975, 537.7), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toHigh() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                mmToTicks(345.95, 537.7), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
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
