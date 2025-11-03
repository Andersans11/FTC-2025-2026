package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import java.util.function.Supplier;

import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.positionable.Positionable;
import dev.nextftc.hardware.delegates.Caching;
import kotlin.Lazy;

public class ServoExFullRangeJava implements Positionable {

    Supplier<ServoImplEx> servoFactory;
    Double cacheTolerance;
    public ServoExFullRangeJava(Double cacheTolerance, Supplier<ServoImplEx> servoFactory) {
        this.cacheTolerance = cacheTolerance;
        this.servoFactory = servoFactory;
    }

    public ServoExFullRangeJava(Double cacheTolerance, String  name) {
        this(cacheTolerance,
                () -> RobotConfig.getHardwareMap().get(ServoImplEx.class, name));
    }
    public ServoExFullRangeJava(String name) {
        this(0.01, name);
    }
    public ServoExFullRangeJava(Supplier<ServoImplEx> servoFactory) {
        this(0.01, servoFactory);
    }
    final ServoImplEx servo = servoFactory.get();

    @Override
    public double getPosition() {
        // needs Caching() or something
    }

    @Override
    public void setPosition(double v) {

    }
}