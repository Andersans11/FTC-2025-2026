package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.Swerve;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

public class SwervePod extends Subsystem {

    MotorEx driveMotor;
    SwervePivot pivot;

    public double mag;
    public double theta;

    public SwerveVector oldVector;

    public SwerveVector mirrorVector;

    public static double kP = 0.1;
    public static double kI = 0;
    public static double kD = 0;

    public SwervePod(MotorEx drive, CRServo pivot, AnalogInput encoder) {
        driveMotor = drive;
        this.pivot = new SwervePivot(pivot, encoder);
        this.pivot.setPidCoeffs(kP, kI, kD);
    }

    public void init() {
        pivot.changeTargetRotation(0);
    }

    public void initLoop() {
        pivot.update();
    }

    public void update(SwerveVector driveVector) {

        if (driveVector.getTheta() > Math.PI) {
            mirrorVector = new SwerveVector(Math.max(-1, Math.min(driveVector.getMag(), 1)) * -1, driveVector.getTheta() + Math.PI);

        } else {
            mirrorVector = new SwerveVector(Math.max(-1, Math.min(driveVector.getMag(), 1)) * -1, driveVector.getTheta() - Math.PI);
        }

        if (oldVector.thetaDist(mirrorVector) < oldVector.thetaDist(driveVector)) {
            mag = Math.max(-1, Math.min(mirrorVector.getMag(), 1));
            theta = mirrorVector.getThetaDeg();
            oldVector = mirrorVector;

        } else {
            mag = Math.max(-1, Math.min(driveVector.getMag(), 1));
            theta = driveVector.getThetaDeg();
            oldVector = driveVector;
        }

        pivot.changeTargetRotation(theta);
        pivot.update();

        new SetPower(driveMotor, mag).invoke();
    }

    public double getAmp() {
        return mag;
    }

    public double getTheta() {
        return theta;
    }

}
