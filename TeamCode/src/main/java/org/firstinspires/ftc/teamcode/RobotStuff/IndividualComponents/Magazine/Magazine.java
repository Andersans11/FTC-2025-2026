package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.RTPAxon;

public class Magazine implements Subsystem {

    RobotConfig config;
    NextFTCOpMode opMode;
    MagSlot[] slots;

    RTPAxon[] servos;
    double pos = 0;

    public void init(NextFTCOpMode opMode, RobotConfig config) {
        this.config = config;
        this.opMode = opMode;

        this.slots = new MagSlot[] {
                new MagSlot(opMode, config, 0, config.Slot1CS), // this slot starts in front of intake
                new MagSlot(opMode, config,120, config.Slot2CS),
                new MagSlot(opMode, config,-120, config.Slot3CS)
        };

        this.servos = new RTPAxon[] {
            new RTPAxon(config.CarouselCR1),
            new RTPAxon(config.CarouselCR2),
            new RTPAxon(config.CarouselCR3)
        };
    }

    public void update() {

    }

}
