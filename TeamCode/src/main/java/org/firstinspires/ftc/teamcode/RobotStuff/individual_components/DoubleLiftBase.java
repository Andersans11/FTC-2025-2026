package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.ResetEncoder;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;


public abstract class DoubleLiftBase extends Subsystem {



    public MotorEx leftMotor;
    public MotorEx rightMotor;

    public MotorGroup motors;

    public RobotConfig robotConfig;

    public PIDFController controller = new PIDFController(new PIDCoefficients(0.005, 0.0, 0.0));

    public double upperLimit;
    public double lowerLimit;
    public double targetPos;
    public double oldPos;

    public double inchesToTicks(double desiredExtension, double encoderPPR) {
        return (-25.4 * desiredExtension) * (encoderPPR / (38.2 * Math.PI));
    }

    public double ticksToInches(double ticks, double encoderPPR) { // for telemetry, do not delete
        return (ticks / -25.4) / (encoderPPR / (38.2 * Math.PI));
    }

    public void setLimits(double lower, double upper) { // use in initialize
        upperLimit = inchesToTicks(upper, 336); // ALWAYS IN TICKS
        lowerLimit = inchesToTicks(lower, 336); // ANY INCOMING VALUES SHOULD BE CONVERTED TO TICKS
    }

    public void fixTargetPos() {
        if (targetPos < lowerLimit) {
            targetPos = lowerLimit;
        }
        if (targetPos > upperLimit) {
            targetPos = upperLimit;
        }
    }

    public Command resetEncoders() {
        return new ResetEncoder(
                motors.getLeader(), // only encoder values of leader motor are ever used
                this
        );
    }

    public Command setTargetPosition(double requestedPos) {

        fixTargetPos();

        oldPos = targetPos;
        targetPos = inchesToTicks(requestedPos, 336);
        return new RunToPosition(
                motors,
                targetPos,
                controller,
                this
        );
    }
    public abstract void configure(RobotConfig robotConfig);
    @Override
    public abstract void initialize();

    @NonNull
    @Override
    public Command getDefaultCommand() {
        return new HoldPosition(
                motors,
                controller,
                this
        );
    }
}
