package org.firstinspires.ftc.teamcode.RobotStuff.misc;

public class DeltaTimer {

    public long startTimeNano;
    public long deltaTimeNano;
    public long lastDeltaTimeNano;

    public DeltaTimer() {
        startTimeNano = System.nanoTime();
        lastDeltaTimeNano = startTimeNano;
    }

    public void reset() {
        startTimeNano = System.nanoTime();
        lastDeltaTimeNano = startTimeNano;
    }

    public long getRuntimeNano() {
        return System.nanoTime() - startTimeNano;
    }

    public long getDelta() {
        deltaTimeNano = System.nanoTime() - lastDeltaTimeNano;
        lastDeltaTimeNano = System.nanoTime();
        return deltaTimeNano;
    }
}
