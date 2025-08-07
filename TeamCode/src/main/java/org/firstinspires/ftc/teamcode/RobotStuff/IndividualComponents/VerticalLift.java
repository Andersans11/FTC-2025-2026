package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
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

    public void initSystem(RobotConfig robotConfig) {
        this.leftMotor = robotConfig.LeftVertical.motor;
        this.rightMotor = robotConfig.RightVertical.motor;
        this.secondRightMotor = robotConfig.SecondRightVertical.motor;
        this.motors = new MotorGroup(leftMotor, rightMotor, secondRightMotor); // TODO: ADD OTHER MOTORS IF APPLICABLE
    }

    public static double bucketPosition = 0.0;
    public static double chamberPosition = 0.0;
    public static double transferPosition = 0.0;
    public static double preTransferPosition = 0.0;

    public enum LiftPreset {
        BUCKET,
        CHAMBER,
        TRANSFER,
        PRETRANSFER
    }

    public Command setTargetPosition(LiftPreset Preset) { // set target pos via preset value
        switch (Preset) {
            case BUCKET:
                targetPos = bucketPosition;  break;

            case CHAMBER:
                targetPos = chamberPosition;  break;

            case TRANSFER:
                targetPos = transferPosition;  break;

            case PRETRANSFER:
                targetPos = preTransferPosition;
        }
        return new RunToPosition(
                motors,
                mmToTicks(targetPos),
                controller,
                this
        );
    }

}

abstract class VerticalLiftInternal extends Subsystem {

    public MotorEx leftMotor, rightMotor, secondRightMotor;

    public MotorGroup motors;

    public RobotConfig robotConfig;

    public PIDFController controller = new PIDFController(0.005, 0.0, 0.0);

    public double targetPos;

    public double mmToTicks(double desiredExtension) {
        return (-desiredExtension) * (140 / (40.0 * Math.PI));
    }//                                ^      ^
    //                        encoder PPR  spool diameter
    public double ticksToMm(double ticks) { // for telemetry, do not delete
        return (-ticks) / (140 / (40.0 * Math.PI));
    }


    public Command resetEncoders() {
        return new ResetEncoder(
                motors.getLeader(), // only encoder values of leader motor are ever used
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
