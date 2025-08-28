package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Misc;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class VectorStuff {
    public VectorStuff() {

    }

    public static double getAmp(Vector2D vector) {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
    }

    public static double getTheta(Vector2D vector) {
        return Math.atan2(vector.getY(), vector.getX());
    }

    public static double thetaDistance(Vector2D vector1, Vector2D vector2) {
        return Math.abs(getTheta(vector1) - getTheta(vector2));
    }

    /**
     * Calculate a vector
     * @param theta Theta IN RADIANS
     * @return The calculated vector
     */
    public static Vector2D VectorFromPolar(double amp, double theta) {
        return new Vector2D(amp * Math.sin(theta), amp * Math.cos(theta));
    }
}
