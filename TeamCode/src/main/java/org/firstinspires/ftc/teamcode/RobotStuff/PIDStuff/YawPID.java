package org.firstinspires.ftc.teamcode.RobotStuff.PIDStuff;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;

@Configurable
public class YawPID {
    double kP;  double secondarykP;  double P; // trust me it saves space
    double kI;  double secondarykI;  double I;
    double kD;  double secondarykD;  double D;

    double threshold;
    boolean useSecondPID;

    double lastError;
    double error;
    double integral;
    double derivative;

    RRoboticsOpMode opMode;

    public final String pidName;
    public static boolean DEBUG = false;

    public YawPID(RRoboticsOpMode opMode, String pidName) {
        this.opMode = opMode;
        this.pidName = pidName;
    }

    public void setCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
    public void setSecondaryCoefficients(double secondarykP, double secondarykI, double secondarykD) {
        this.secondarykP = secondarykP;
        this.secondarykI = secondarykI;
        this.secondarykD = secondarykD;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    public void setSecondary(boolean useSecondPID) {
        this.useSecondPID = useSecondPID;
    }

    /**
     * run the PID
     * @param targetPos the target yaw
     * @param currentPos the actual yaw
     * @param deltaTimeNano the update time in nano seconds
     * @return the power
     */
    public double lockYaw(double targetPos, double currentPos, long deltaTimeNano) {
        lastError = error;
        error = angleFix(targetPos - currentPos);

        integral += error * (deltaTimeNano / Math.pow(10.0, 9));
        derivative = (error - lastError) / (deltaTimeNano / Math.pow(10.0, 9));

        if (useSecondPID && (error * kP) < threshold) {
            P = (error * secondarykP);
            I = (integral * secondarykI);
            D = (derivative * secondarykD);
            opMode.addLine("Using secondary PID");
        }
        else {
            P = (error * kP);
            I = (integral * kI);
            D = (derivative * kD);
            opMode.addLine("Using primary PID");
        }
        yawTelemetry(error, derivative, targetPos, currentPos, lastError);
        return P + I + D;
    }

    public double angleFix(double radians) { // so robot doesn't rotate 350deg to get from 5deg to 355deg

        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }

        return radians;
    }

    public void yawTelemetry(double error, double derivative, double targetPos, double currentPos, double lastError) {
        opMode.addData("target radians:", targetPos);
        opMode.addData("current radians:", currentPos);
        if (DEBUG) {
            opMode.addData("error:", error);
            opMode.addData("derivative:", derivative);
            opMode.addData("last error:", lastError);
            opMode.addData("P:", P);
            opMode.addData("I:", I);
            opMode.addData("D:", D);
        }
    }
}
