package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;

import com.qualcomm.robotcore.hardware.ColorSensor;

import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.core.commands.Command;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.RTPAxon;

public class Magazine implements Subsystem {

    NextFTCOpMode opMode;
    MagSlot[] slots;
    MagSlot activeSlot; // slot that receives the next ball
    RTPAxon[] servos;
    ColorSensor color;
    double pos = 0;

    public void init(NextFTCOpMode opMode) {
        this.opMode = opMode;

        this.slots = new MagSlot[] {
                new MagSlot(opMode, 0), // this slot starts in front of intake
                new MagSlot(opMode,120),
                new MagSlot(opMode,-120)
        };
        this.activeSlot = slots[0];

        this.servos = new RTPAxon[] {
            new RTPAxon(RobotConfig.CarouselCR1),
            new RTPAxon(RobotConfig.CarouselCR2),
            new RTPAxon(RobotConfig.CarouselCR3)
        };

        this.color = RobotConfig.IntakeCS;
    }

    public Command setRotation(double to) {
        // TODO: take an angle and calculate servo rotation
        return new NullCommand();
    }

    public void update() {

    }

}
