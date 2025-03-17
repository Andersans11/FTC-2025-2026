package org.firstinspires.ftc.teamcode.RobotStuff.pidStuff;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

public class CustomPID {
    double kP;
    double kI;
    double kD;

    double P;
    double I;
    double D;

    double lastError;
    double integralSum;

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

    public double lockYaw(double targetPos, double currentPos, double deltaTime) {
        double error = angleFix(targetPos - currentPos);
        integralSum += error * deltaTime;
        double derivative = (error - lastError) / deltaTime;
        lastError = error;
        P = (error * kP);
        I = (integralSum * kI);
        D = (derivative * kD);
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
