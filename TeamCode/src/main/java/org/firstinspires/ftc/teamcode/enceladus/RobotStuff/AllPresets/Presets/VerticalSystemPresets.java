package org.firstinspires.ftc.teamcode.enceladus.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;

public class VerticalSystemPresets extends Subsystem {

    public static final VerticalSystemPresets INSTANCE = new VerticalSystemPresets()    ;
    private VerticalSystemPresets() { } // nftc boilerplate

    public Command depositLowChamber() {

        return new NullCommand();
    }

    public Command depositHighChamber() {

        return new NullCommand();
    }

    public Command depositLowBasket() {

        return new NullCommand();
    }

    public Command depositHighBasket() {

        return new NullCommand();
    }

    public Command depositToFloor() { // OZ, net zone, dropping a sample

        return new NullCommand();
    }

    public Command pickupFromWall() {

        return new NullCommand();
    }
}
