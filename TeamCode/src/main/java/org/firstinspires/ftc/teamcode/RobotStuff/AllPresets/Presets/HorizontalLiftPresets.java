package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;

import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;

public class HorizontalLiftPresets extends Subsystem {

    public static final HorizontalLiftPresets INSTANCE = new HorizontalLiftPresets()    ;
    private HorizontalLiftPresets() { } // nftc boilerplate

    public Command minimum() {
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM);
    }
    public Command minimum(Float f) {
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM);
    }

    public Command maximum() {
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM);
    }
    public Command maximum(Float f) {
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM);
    }

    public Command mid() {
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MID);
    }
}