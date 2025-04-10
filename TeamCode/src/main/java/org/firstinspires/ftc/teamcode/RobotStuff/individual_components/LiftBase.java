package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.conditionals.PassiveConditionalCommand;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.ResetEncoder;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import kotlin.Pair;
import kotlin.jvm.functions.Function0;

public class LiftBase extends Subsystem {

    public static final LiftBase INSTANCE = new LiftBase();
    private LiftBase() { }



    public MotorEx motor;

    public PIDFController controller = new PIDFController(new PIDCoefficients(0.005, 0.0, 0.0));

    public String name = "liftR";

    public double upperLimit;
    public double lowerLimit;

    public boolean overLimits;


    /*
    using rev 6000 rpm motors - 28 ppr

    12:1 gear ratio > 336 ppr
     */

    public double mmToTicks(double desiredExtension, double encoderPPR) {
        return (-1 * desiredExtension) * (encoderPPR / (38.2 * Math.PI));
    }

    public double ticksToMM(double ticks, double encoderPPR) {
        return (-1 * ticks) / (encoderPPR / (38.2 * Math.PI));
    }

    public double inchesToTicks(double desiredExtension, double encoderPPR) {
        return (-25.4 * desiredExtension) * (encoderPPR / (38.2 * Math.PI));
    }

    public double ticksToInches(double ticks, double encoderPPR) {
        return (ticks / -25.4) / (encoderPPR / (38.2 * Math.PI));
    }

    public void setLimits(double upper, double lower) {
        upperLimit = upper;
        lowerLimit = lower;
    }

    //TODO: boolean values work but they are inverted in some way // if i remove it from telemetry then i can pretend it doesn't exist
    public boolean goingPastMaxExtension(double joystickY) {return (motor.getCurrentPosition() >= inchesToTicks(upperLimit, 751.8) && ((joystickY * -1) > 0));}

    public boolean goingPastMinExtension(double joystickY) {return (motor.getCurrentPosition() <= inchesToTicks(lowerLimit, 751.8) && ((joystickY * -1) < 0));}


    public Function0<Boolean> goingOverLimits(double liftStick) {
        overLimits = (goingPastMinExtension(liftStick) || goingPastMaxExtension(liftStick));
        return () -> overLimits;
    }


    public Command toLow() {
        return new RunToPosition(motor,
                inchesToTicks(lowerLimit, 751.8),
                controller,
                this);
    }

    public Command toMiddle() {
        return new RunToPosition(motor,
                inchesToTicks(6, 751.8),
                controller,
                this);
    }

    public Command toHigh() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                inchesToTicks(12, 751.8 ), // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command resetEncoders() {
        return new ResetEncoder(
                motor,
                this
        );
    }

    public Function0<Command> moveLift(double joystickY) {
        return () -> new SetPower(
                motor,
                joystickY,
                this
        );
    }

    public Function0<Command> stop() {
        return () -> new HoldPosition(
                motor,
                controller,
                this);
    }

    public Command moveLiftBetter(Pair<Float, Float> JoystickValues) {

        double joystickY = (JoystickValues.getSecond() * -1);

        return new PassiveConditionalCommand(
                goingOverLimits(joystickY),
                moveLift(joystickY),
                stop()
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
