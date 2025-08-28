package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes.Swerve;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Misc.VectorStuff;

public class SwervePod extends Subsystem{

    MotorEx driveMotor;
    SwervePivot pivot;

    public double amp;
    public double theta;

    public Vector2D oldVector;

    public Vector2D mirrorVector;

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

    public void update(Vector2D driveVector) {

        if (VectorStuff.getTheta(driveVector) > Math.PI) {
            mirrorVector = VectorStuff.VectorFromPolar(Math.max(-1, Math.min(VectorStuff.getAmp(driveVector), 1)) * -1, VectorStuff.getTheta(driveVector) - Math.PI);
        } else {
            mirrorVector = VectorStuff.VectorFromPolar(Math.max(-1, Math.min(VectorStuff.getAmp(driveVector), 1)) * -1, VectorStuff.getTheta(driveVector) + Math.PI);
        }

        if (VectorStuff.thetaDistance(oldVector, mirrorVector) < VectorStuff.thetaDistance(oldVector, driveVector)) {
            amp = Math.max(-1, Math.min(VectorStuff.getAmp(mirrorVector), 1));
            theta = Math.toDegrees(VectorStuff.getTheta(mirrorVector));
            oldVector = mirrorVector;
        } else {
            amp = Math.max(-1, Math.min(VectorStuff.getAmp(driveVector), 1));
            theta = Math.toDegrees(VectorStuff.getTheta(driveVector));
            oldVector = driveVector;
        }

        pivot.changeTargetRotation(theta);
        pivot.update();
        new SetPower(driveMotor, amp).invoke();
    }

    public double getAmp() {
        return amp;
    }

    public double getTheta() {
        return theta;
    }
}
