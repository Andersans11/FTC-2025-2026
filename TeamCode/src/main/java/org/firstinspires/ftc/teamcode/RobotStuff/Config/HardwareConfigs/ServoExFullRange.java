package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.delegates.Caching;
import dev.nextftc.hardware.positionable.Positionable;

public class ServoExFullRange implements Positionable {

    private ServoImplEx servo;
    private Double cachedPosition;
    private Double cacheTolerance;

    // Constructor with cacheTolerance and servoFactory
    public ServoExFullRange(Double cacheTolerance, ServoFactory servoFactory) {
        this.cacheTolerance = cacheTolerance;
        this.servo = servoFactory.create();
        this.cachedPosition = null;
        this.servo.setPwmRange(new PwmControl.PwmRange(500, 2500));
    }

    // Constructor with servoFactory only (default cacheTolerance = 0.01)
    public ServoExFullRange(ServoFactory servoFactory) {
        this(0.01, servoFactory);
    }

    // Constructor with servo and default cacheTolerance
    public ServoExFullRange(ServoImplEx servo) {
        this(servo, 0.01);
    }

    // Constructor with servo and cacheTolerance
    public ServoExFullRange(ServoImplEx servo, Double cacheTolerance) {
        this(cacheTolerance, () -> servo);
    }

    // Constructor with name and default cacheTolerance
    public ServoExFullRange(String name) {
        this(name, 0.01);
    }

    // Constructor with name and cacheTolerance
    public ServoExFullRange(String name, Double cacheTolerance) {
        this(cacheTolerance, () -> (ServoImplEx) ActiveOpMode.hardwareMap().get(name));
    }

    // Getter for position
    @Override
    public double getPosition() {
        return cachedPosition;
    }

    // Setter for position with caching logic
    @Override
    public void setPosition(double position) {
            if (cachedPosition == null || Math.abs(position - cachedPosition) >= cacheTolerance) {
                servo.setPosition(position);
                cachedPosition = position;
            }
    }

    // Getter for servo
    public ServoImplEx getServo() {
        return servo;
    }

    // Functional interface for servo factory
    @FunctionalInterface
    public interface ServoFactory {
        ServoImplEx create();
    }
}