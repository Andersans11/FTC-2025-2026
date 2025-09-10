package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils.ArtifactTypes;

public class MagSlot {

    RobotConfig config;
    NextFTCOpMode opMode;
    double offset;
    ArtifactTypes content;

    public MagSlot(NextFTCOpMode opMode, RobotConfig config, double offset) {
        this.config = config;
        this.opMode = opMode;

        this.offset = offset;

        this.content = ArtifactTypes.NONE;
    }

    public double getSlotOffset() {
        return offset;
    }
}
