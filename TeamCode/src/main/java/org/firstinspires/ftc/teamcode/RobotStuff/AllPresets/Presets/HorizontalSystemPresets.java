package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;

public class HorizontalSystemPresets {

    public static final HorizontalSystemPresets INSTANCE = new HorizontalSystemPresets()    ;
    private HorizontalSystemPresets() { } // nftc boilerplate
    public Command prepareSubPickup() {  // bind to pressed

        return new NullCommand();
    }

    public Command dropIntoSub() {  // bind to released

        return new NullCommand();
    }

    public Command depositToFloor() { //oz, get rid of sample

        return new NullCommand();
    }

    public Command pickupFromFloor() { // oz, preplaced sample

        return new NullCommand();
    }
}