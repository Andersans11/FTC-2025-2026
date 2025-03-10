package org.firstinspires.ftc.teamcode.example.java;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
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

    /*
    for yellow jacket motors:
    27 inches of extension for 4300 encoder ticks
    0.00627906976 inches per encoder
    let y equal desired extension
    let x equal the encoder count to solve for
    or 27x/4300 = y

    27x/4300 = 13.625 (max extension)
    *4300
    27x=58587.5
    x=2169.90740741

    yeah this might not work but hey why not

    for rev hd hex motors
    28 encoder counts per revolution
    gobilda pulley has od of 40mm -> 1.575 in
    circumference = π x 1.575
    encoder counts per in = 28/1.575π
    */
    //aka:
    double c = 1.575 * (Math.PI);
    double encoderCountPerInchRev = 28/c;
    int EncoderCountPerInchGobilda = 27/4300;
    // i highly doubt that this is right


    public Command toLow() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                0.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }
    /*
    public Command toMiddle() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                500.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }
    */
    public Command toHigh() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                2169.90740741, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }
    
    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }
}
