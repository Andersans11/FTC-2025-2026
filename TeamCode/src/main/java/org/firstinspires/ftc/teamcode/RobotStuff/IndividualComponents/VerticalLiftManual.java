package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class VerticalLiftManual extends Subsystem {
    public static final VerticalLiftManual INSTANCE = new VerticalLiftManual();
    private VerticalLiftManual() { } // nftc boilerplate

    public MotorEx leftMotor, rightMotor, secondRightMotor;

    public MotorGroup motors;

    public PIDFController liftController = new PIDFController(0.005, 0.0, 0.0);

    public void initSystem(RobotConfig robotConfig) {
        this.leftMotor = robotConfig.LeftVertical.motor;
        this.rightMotor = robotConfig.RightVertical.motor;
        this.secondRightMotor = robotConfig.SecondRightVertical.motor;
        this.motors = new MotorGroup(leftMotor, rightMotor, secondRightMotor); // TODO: ADD OTHER MOTORS IF APPLICABLE
    }

    public double mmToTicks(double desiredExtension) {
        return (-desiredExtension) * (140 / (25 * Math.PI));
    }//                                ^      ^
    //                        encoder PPR  spool diameter
    public double ticksToMm(double ticks) { // for telemetry, do not delete
        return (-ticks) / (140 / (25 * Math.PI));
    }

    public Command MoveUp() {
        return new SetPower(motors, 0.75);
    }
    public Command MoveDown() {
        return new SetPower(motors, -0.5);
    }

    public Command Stop() {
        return new SetPower(motors, 0);
    }

    /**
     * Position is in cm instead of mm
     **/
    public Command SetPosition(double position) {
        return new RunToPosition(motors, mmToTicks(position * 10), liftController);
    }
}
