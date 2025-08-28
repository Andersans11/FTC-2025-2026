package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Misc;

import java.util.ArrayList;

import kotlin.Pair;

public class DifferenceArrayList extends ArrayList<Float> {

    public DifferenceArrayList(ArrayList<Float> initialContents) {
        this.addAll(initialContents);
    }

    public DifferenceArrayList() {}

    public Float getCollectiveAverage() {
        float total = 0;
        for (float measuredDiff : this) {
            total += measuredDiff;
        }
        return total / this.toArray().length;
    }

    public Pair<Float, Double> getMinAtIndex() {
        float min = this.get(0); // start at index zero
        double atIndex = 0;
        for (float measuredDiff : this) {
            if (measuredDiff < min) {
                min = measuredDiff; // if the measured diff is less than the last record min
                atIndex = this.indexOf(measuredDiff); // set min to that and set index to index of measured
            }
        }
        return new Pair<>(min, atIndex);
    }
    public Pair<Float, Double> getMaxAtIndex() {
        float max = this.get(0); // start at index zero
        double atIndex = 0;
        for (float measuredDiff : this) {
            if (measuredDiff > max) {
                max = measuredDiff; // if the measured diff is less than the last record min
                atIndex = this.indexOf(measuredDiff); // set min to that and set index to index of measured
            }
        }
        return new Pair<>(max, atIndex);
    }

    public Float getMin() {
        float min = this.get(0); // start at index zero
        for (float measuredDiff : this) {
            if (measuredDiff < min) {
                min = measuredDiff; // if the measured diff is less than the last record min
            }
        }
        return min;
    }
    public Float getMax() {
        float max = this.get(0); // start at index zero
        for (float measuredDiff : this) {
            if (measuredDiff > max) {
                max = measuredDiff; // if the measured diff is less than the last record min
            }
        }
        return max;
    }

    public Float lastAdded() {
        int atIndex = this.size() - 1;
        return this.get(atIndex);
    }

}
