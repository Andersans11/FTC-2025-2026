package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Misc;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DeltaTimer {


    public long startTimeNano;
    public long deltaTimeNano;

    public DeltaTimer() {
        startTimeNano = System.nanoTime();
    }

    public void reset() {
        startTimeNano = System.nanoTime();
    }

    public long getTimeNano() {
        return System.nanoTime() - startTimeNano;
    }

    public double getTimeMilli() {
        return (System.nanoTime() - startTimeNano) / (double) ElapsedTime.MILLIS_IN_NANO;
    }

    public double getTimeSeconds() {
        return (System.nanoTime()- startTimeNano) / (double) ElapsedTime.SECOND_IN_NANO;
    }

    public long getDelta() {
        deltaTimeNano = getTimeNano();
        reset();
        return deltaTimeNano;
    }
}