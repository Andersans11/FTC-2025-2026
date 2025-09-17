package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;


import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils.ArtifactTypes;

import dev.nextftc.ftc.NextFTCOpMode;


public class MagSlot {

    NextFTCOpMode opMode;
    double offset;
    ArtifactTypes content;
    ColorSensor color;

    public MagSlot(NextFTCOpMode opMode, double offset, ColorSensor color) {
        this.opMode = opMode;

        this.offset = offset;
        this.color = color;

        this.content = ArtifactTypes.NONE;
    }

    public double getSlotOffset() {
        return offset;
    }

    public void update() {
        // TODO: get color from sensor, check if color is in a certain range to determine if it's empty, has purple, or has green and update accordingly

    }
}
