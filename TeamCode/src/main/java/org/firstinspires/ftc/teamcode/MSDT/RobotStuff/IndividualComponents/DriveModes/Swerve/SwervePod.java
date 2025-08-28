package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes.Swerve;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Set;

public class SwervePod extends Subsystem{

    MotorEx driveMotor;
    RTPAxon pivot;

    VectorStuff vectorStuff = new VectorStuff();

    public static double kP = 0.1;
    public static double kI = 0;
    public static double kD = 0;

    public SwervePod(MotorEx drive, CRServo pivot, AnalogInput encoder) {
        driveMotor = drive;
        this.pivot = new RTPAxon(pivot, encoder);
        this.pivot.setPidCoeffs(kP, kI, kD);
    }

    public Command update(Vector2D driveVector) {
        pivot.changeTargetRotation(Math.toDegrees(vectorStuff.getTheta(driveVector)));
        pivot.update();
        return new SetPower(driveMotor, Math.max(0, Math.min(vectorStuff.getAmp(driveVector), 1)));
    }
}
