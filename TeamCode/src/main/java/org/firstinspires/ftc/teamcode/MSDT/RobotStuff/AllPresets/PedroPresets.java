package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.AllPresets;

import com.rowanmcalpin.nextftc.core.command.CommandManager;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.HorizontalLift;

public class PedroPresets {

    public class HorizontalSystemPresets {

        public void prepareSubPickup() {  // bind to pressed

        }
        public void dropIntoSub() {  // bind to released

        }

        public void depositToFloor() { //oz, get rid of sample

        }

        public void pickupFromFloor() { // oz, preplaced sample

        }

    }

    public class VerticalSystemPresets {

        public void depositLowChamber() {

        }

        public void depositHighChamber() {

        }

        public void depositLowBasket() {

        }

        public void depositHighBasket() {

        }

        public void depositToFloor() { // OZ, net zone, dropping a sample

        }

        public void pickupFromWall() {

        }

        public class HoriontalLiftPersets {

            public void minimum() {
                CommandManager.INSTANCE.scheduleCommand(HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM));
            }

            public void maximum() {
                CommandManager.INSTANCE.scheduleCommand(HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM));
            }

            public void flush() {
                CommandManager.INSTANCE.scheduleCommand(HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MID));
            }
        }
    }
}
