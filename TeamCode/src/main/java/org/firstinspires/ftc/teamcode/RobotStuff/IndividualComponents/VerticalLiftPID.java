package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class VerticalLiftPID extends Subsystem {
    public static final VerticalLiftPID INSTANCE = new VerticalLiftPID();
    private VerticalLiftPID() { } // nftc boilerplate

    public MotorEx leftMotor, rightMotor, secondRightMotor;

    public MotorGroup motors;

    public double targetPosition;

    public static double kP = 0.00075;
    public static double kI = 0;
    public static double kD = 0;

    public PIDFController liftController = new PIDFController(kP, kI, kD);

    public void initSystem(RobotConfig robotConfig) {
        this.leftMotor = robotConfig.LeftVertical.motor;
        this.rightMotor = robotConfig.RightVertical.motor;
        this.secondRightMotor = robotConfig.SecondRightVertical.motor;
        this.motors = new MotorGroup(secondRightMotor, leftMotor, rightMotor);
    }

    public double mmToTicks(double desiredExtension) {
        return (-desiredExtension) * (140 / (25 * Math.PI));
    }//                                ^      ^
    //                        encoder PPR  spool diameter
    public double ticksToMm(double ticks) { // for telemetry, do not delete
        return (-ticks) / (140 / (25 * Math.PI));
    }

    /**
     * Position is in cm instead of mm
     **/
    public Command SetPosition(double positionCM) {
        targetPosition = positionCM;
        return new RunToPosition(motors, mmToTicks(positionCM * 10), liftController);
    }

    @NonNull
    @Override
    public Command getDefaultCommand() {
        liftController.setKD(kD);
        liftController.setKI(kI);
        liftController.setKP(kP);
        return new HoldPosition(
                motors,
                liftController,
                this
        );
    }

    public boolean isAtPosition() {
        return ticksToMm(secondRightMotor.getCurrentPosition()) == targetPosition * 10;
    }

    public double CurrentPosition() {
        return ticksToMm(secondRightMotor.getCurrentPosition()) / 10;
    }
}
