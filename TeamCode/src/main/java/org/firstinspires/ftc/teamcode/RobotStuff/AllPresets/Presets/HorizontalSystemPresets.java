package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.CommandManager;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.core.command.utility.delays.WaitUntil;
import com.rowanmcalpin.nextftc.core.command.utility.statemachine.AdvancingCommand;

import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Intake;

public class HorizontalSystemPresets extends Subsystem {

    public static final HorizontalSystemPresets INSTANCE = new HorizontalSystemPresets()    ;
    private HorizontalSystemPresets() { } // nftc boilerplate

    AdvancingCommand advancingPressedCommand = new AdvancingCommand();
    AdvancingCommand advancingReleasedCommand = new AdvancingCommand();


    public void stuff() {
        advancingPressedCommand.add(HorizontalLiftPresets.INSTANCE.maximum());
        advancingPressedCommand.setState(() -> 0);

        advancingReleasedCommand.add(HorizontalLiftPresets.INSTANCE.minimum());
        advancingReleasedCommand.setState(() -> 0);
    }
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

    public Command intakeSequencePressedCommand(Float f) {
        return advancingPressedCommand;
    }

    public Command intakeSequenceReleasedCommand(Float f) {
        return advancingReleasedCommand;
    }
}