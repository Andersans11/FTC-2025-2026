package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.RTPAxon;

public class Magazine implements Subsystem {

    NextFTCOpMode opMode;
    MagSlot[] slots;

    RTPAxon[] servos;
    double pos = 0;

    public void init(NextFTCOpMode opMode) {
        this.opMode = opMode;

        this.slots = new MagSlot[] {
                new MagSlot(opMode, 0, RobotConfig.Slot1CS), // this slot starts in front of intake
                new MagSlot(opMode,120, RobotConfig.Slot2CS),
                new MagSlot(opMode,-120, RobotConfig.Slot3CS)
        };

        this.servos = new RTPAxon[] {
            new RTPAxon(RobotConfig.CarouselCR1),
            new RTPAxon(RobotConfig.CarouselCR2),
            new RTPAxon(RobotConfig.CarouselCR3)
        };
    }

    public void update() {

    }

}
