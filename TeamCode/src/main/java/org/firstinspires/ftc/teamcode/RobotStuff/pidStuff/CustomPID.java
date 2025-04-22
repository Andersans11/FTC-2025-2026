package org.firstinspires.ftc.teamcode.RobotStuff.pidStuff;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class CustomPID {
    double kP;
    double kI;
    double kD;

    double secondarykP;
    double secondarykI;
    double secondarykD;
    double threshold;

    boolean useSecondPID;

    double P;
    double I;
    double D;

    double lastError;
    double error;
    double integral;
    double deltaTimeNano;
    double previousDeltaTimeNano;

    Telemetry telemetry;
    RobotConfig config;

    public final String pidName;

    public CustomPID(Telemetry telemetry, RobotConfig config, String pidName) {
        this.config = config;
        this.telemetry = telemetry;
        this.pidName = pidName;
    }

    public void setCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
    public void setSecondaryCoefficients(double kP, double kI, double kD) {
        this.secondarykP = kP;
        this.secondarykI = kI;
        this.secondarykD = kD;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    public void setSecondary(boolean useSecondPID) {
        this.useSecondPID = useSecondPID;
    }

    public double lockYaw(double targetPos, double currentPos, double deltaTime) {
        lastError = error;
        error = angleFix(targetPos - currentPos);
        integral += error * deltaTime;
        double derivative = (error - lastError) / deltaTime;


        P = (error * kP);
        if (useSecondPID && P < threshold) {
            P = (error * secondarykP);
            I = (integral * secondarykI);
            D = (derivative * secondarykD);
            telemetry.addLine("Using secondary PID");
        } else {
            I = (integral * kI);
            D = (derivative * kD);
            telemetry.addLine("Using primary PID");
        }
        yawTelemetry(error, derivative, targetPos, currentPos, lastError);
        return P + I + D;
    }

    public double lockYawPedro(double targetPos, double currentPos) {
        lastError = error;
        error = angleFix(targetPos - currentPos);

        deltaTimeNano = System.nanoTime() - previousDeltaTimeNano;
        previousDeltaTimeNano = System.nanoTime();

        integral += error * (deltaTimeNano / Math.pow(10.0, 9));
        double derivative = (error - lastError) / (deltaTimeNano / Math.pow(10.0, 9));

        if (useSecondPID && (error * kP) < threshold) {
            P = (error * secondarykP);
            I = (integral * secondarykI);
            D = (derivative * secondarykD);
            telemetry.addLine("Using secondary PID");
        }
        else {
            P = (error * kP);
            I = (integral * kI);
            D = (derivative * kD);
            telemetry.addLine("Using primary PID");
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
        telemetry.addData("error:", error);
        telemetry.addData("target radians:", targetPos);
        telemetry.addData("current radians:", currentPos);
        telemetry.addData("derivative:", derivative);
        telemetry.addData("last error:", lastError);
        telemetry.addData("P:", P);
        telemetry.addData("I:", I);
        telemetry.addData("D:", D);
    }
}
