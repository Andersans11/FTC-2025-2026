package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.RTPAxon;

public class Magazine {

    RobotConfig config;
    NextFTCOpMode opMode;
    MagSlot[] slots;

    RTPAxon[] servos;
    double pos = 0;
    public Magazine(NextFTCOpMode opMode, RobotConfig config) {
        this.config = config;
        this.opMode = opMode;

        this.slots = new MagSlot[] {
                new MagSlot(opMode, config, 0),
                new MagSlot(opMode, config,120),
                new MagSlot(opMode, config,-120)
        };

        this.servos = new RTPAxon[] {
            new RTPAxon(config.CarouselCR1),
            new RTPAxon(config.CarouselCR2),
            new RTPAxon(config.CarouselCR3)
        };
    }

    public void alignBallToTurret(double turretPos) {

    }

}
