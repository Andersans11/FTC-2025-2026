package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes.Swerve;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class VectorStuff {
    public VectorStuff() {

    }

    public double getAmp(Vector2D vector) {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
    }

    public double getTheta(Vector2D vector) {
        return Math.atan2(vector.getY(), vector.getX());
    }

    public double thetaDistance(Vector2D vector1, Vector2D vector2) {
        return Math.abs(getTheta(vector1) - getTheta(vector2));
    }

    public Vector2D VectorFromPolar(double amp, double theta) {
        return new Vector2D(amp * Math.sin(theta), amp * Math.cos(theta));
    }
}
