package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.gamepad.Button;
import com.rowanmcalpin.nextftc.ftc.gamepad.Control;
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.ResetEncoder;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class VerticalLift extends VerticalLiftInternal {



    public static final VerticalLift INSTANCE = new VerticalLift();
    private VerticalLift() { } // nftc boilerplate

    public void initSystem(RobotConfig robotConfig, NextFTCOpMode opMode) {
        setLimits(0 , DistanceUnit.INCH.fromMm(206));
        this.opMode = opMode;
        this.robotConfig = robotConfig;
        this.leftMotor = robotConfig.LeftVertical.motor;
        this.rightMotor = robotConfig.RightVertical.motor;
        this.motors = new MotorGroup(leftMotor, rightMotor); // TODO: ADD OTHER MOTORS IF APPLICABLE
        initialize();
    }

    public Command toLow() {
        opMode.telemetry.addLine("vertical lift zero");
        return setTargetPosition(0);
    }

    public Command toMiddle() {
        opMode.telemetry.addLine("vertical lift mid");
        return setTargetPosition(DistanceUnit.INCH.fromMm(100));
    }

    public Command toHigh() {
        opMode.telemetry.addLine("vertical lift max");
        return setTargetPosition(DistanceUnit.INCH.fromMm(200));
    } // i work in millimeters and nobody can stop me

    public enum Mappings {
        TO_HIGH,
        TO_MID,
        TO_LOW,
        RESET
    }

    Button TO_HIGH;
    Button TO_MID;
    Button TO_LOW;
    Button RESET;

    public void map(Control control, Mappings mapping) {
        switch (mapping) {
            case TO_HIGH:
                if (control instanceof Button) {
                    this.TO_HIGH = TO_HIGH.getClass().cast(control);
                    TO_HIGH.setPressedCommand(INSTANCE::toHigh);
                } else {
                    throw new IllegalArgumentException("MOVE requires a " + TO_HIGH.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case TO_MID:
                if (control instanceof Button) {
                    this.TO_MID = TO_MID.getClass().cast(control);
                    TO_MID.setPressedCommand(INSTANCE::toMiddle);
                } else {
                    throw new IllegalArgumentException("TO_ZERO requires a " + TO_MID.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case TO_LOW:
                if (control instanceof Button) {
                    this.TO_LOW = TO_LOW.getClass().cast(control);
                    TO_LOW.setPressedCommand(INSTANCE::toMiddle);
                } else {
                    throw new IllegalArgumentException("TO_ZERO requires a " + TO_LOW.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case RESET:
                if (control instanceof Button) {
                    this.RESET = RESET.getClass().cast(control);
                    RESET.setPressedCommand(INSTANCE::resetEncoders);
                } else {
                    throw new IllegalArgumentException("RESET requires a " + RESET.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
        }
    }

}

abstract class VerticalLiftInternal extends Subsystem {


    public NextFTCOpMode opMode;
    public double spoolDiamater = 38.2;// TODO: UPDATE FOR NEW SPOOL
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
        return (-25.4 * desiredExtension) * (encoderPPR / (spoolDiamater * Math.PI));
    }

    public double ticksToInches(double ticks, double encoderPPR) { // for telemetry, do not delete
        return (ticks / -25.4) / (encoderPPR / (spoolDiamater * Math.PI));
    }

    public void setLimits(double lower, double upper) { // use in initialize
        upperLimit = inchesToTicks(upper, robotConfig.LeftVertical.encoderPPR); // ALWAYS IN TICKS
        lowerLimit = inchesToTicks(lower, robotConfig.LeftVertical.encoderPPR); // ANY INCOMING VALUES SHOULD BE CONVERTED TO TICKS
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
        targetPos = inchesToTicks(requestedPos, robotConfig.LeftVertical.encoderPPR);
        return new RunToPosition(
                motors,
                targetPos,
                controller,
                this
        );
    }
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
