package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Presets;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;

// robot parts
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;

public class Presets {

    public class HorizontalSystemPresets {

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

    public class VerticalSystemPresets {

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

    public class HoriontalLiftPersets {

        public Command minimum() {
            return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM);
        }

        public Command maximum() {
            return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM);
        }

        public Command flush() {
            return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.FLUSH);
        }
    }
}
