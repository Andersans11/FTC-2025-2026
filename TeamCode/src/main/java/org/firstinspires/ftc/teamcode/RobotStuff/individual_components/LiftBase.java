package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.ResetEncoder;
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


    /*
    using rev 6000 rpm motors - 28 ppr

    12:1 gear ratio > 336 ppr
     */

    public double mmToTicks(double desiredExtension, double encoderPPR) {
        return (-1 * desiredExtension) * (encoderPPR / (38.2 * Math.PI)); // encoders are backwards
    }

    public double ticksToMM(double ticks, double encoderPPR) {
        return (-1 * ticks) / (encoderPPR / (38.2 * Math.PI));
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

        double joystickY = (double) (JoystickValues.getSecond());

        return new SetPower(
                motor,
                joystickY,
                this
        );
    }

    public Command resetEncoders() {
        return new ResetEncoder(
                motor,
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
