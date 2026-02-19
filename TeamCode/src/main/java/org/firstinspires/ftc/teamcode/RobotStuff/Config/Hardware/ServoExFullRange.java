package org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import dev.nextftc.hardware.positionable.Positionable;

public class ServoExFullRange implements Positionable {

    private final ServoImplEx servo;
    private Double cachedPosition;
    private final Double cacheTolerance;

    /**
     * NextFTC's ServoEx, but full range.
     * @param cacheTolerance the tolerance for setting / storing position
     * @param servoFactory a provider for the servo
     */
    public ServoExFullRange(Double cacheTolerance, ServoFactory servoFactory) {
        this.cacheTolerance = cacheTolerance;
        this.servo = servoFactory.create();
        this.cachedPosition = null;
        this.servo.setPwmRange(new PwmControl.PwmRange(500, 2500));
    }


    /**
     * NextFTC's ServoEx, but full range.
     * @param servoFactory a provider for the servo
     */
    public ServoExFullRange(ServoFactory servoFactory) {
        this(0.01, servoFactory);
    }

    @Override
    public double getPosition() {
        try {
            return cachedPosition;
        } catch (RuntimeException reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee) {
            return -1;
        }
    }

    @Override
    public void setPosition(double position) {
            if (cachedPosition == null || Math.abs(position - cachedPosition) >= cacheTolerance) {
                servo.setPosition(position);
                cachedPosition = position;
            }
    }

    public ServoImplEx getServo() {
        return servo;
    }

    @FunctionalInterface
    public interface ServoFactory {
        ServoImplEx create();
    }
}