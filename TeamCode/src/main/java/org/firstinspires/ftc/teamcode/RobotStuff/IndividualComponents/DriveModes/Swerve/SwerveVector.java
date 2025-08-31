package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.Swerve;

public class SwerveVector {

    private final double x; // x = mag cos(theta)
    private final double y; // y = mag sin(theta)
    private final double magnitude; // mag = sqrt(x^2 + y^2)
    private final double theta; // theta = arctan(y / x)

    public SwerveVector(double a, SwerveVector u) {
        this.x = a * u.x;
        this.y = a * u.y;

        this.magnitude = Math.sqrt(x*x + y*y);
        this.theta = Math.atan(y / x);
    }

    public SwerveVector(double scalar1, SwerveVector vec1, double scalar2, SwerveVector vec2, double scalar3, SwerveVector vec3) {
        this.x = scalar1 * vec1.x + scalar2 * vec2.x + scalar3 * vec3.x;
        this.y = scalar1 * vec1.y + scalar2 * vec2.y + scalar3 * vec3.y;

        this.magnitude = Math.sqrt(x*x + y*y);
        this.theta = Math.atan(y / x);
    }

    public SwerveVector(double magnitude, double theta) {
        this.magnitude = magnitude;
        this.theta = theta;

        this.x = magnitude * Math.cos(theta);
        this.y = magnitude * Math.sin(theta);
    }

    public double getTheta() {
        return theta;
    }

    public double getThetaDeg() {
        return Math.toDegrees(theta);
    }

    public double getMag() {
        return magnitude;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double thetaDist(SwerveVector comp) {
        return Math.abs(this.theta - comp.theta);
    }

}
