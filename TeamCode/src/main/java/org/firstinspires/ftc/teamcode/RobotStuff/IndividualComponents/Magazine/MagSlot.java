package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils.ArtifactTypes;

import dev.nextftc.ftc.NextFTCOpMode;


public class MagSlot {

    double offset;
    ArtifactTypes content;

    public MagSlot(NextFTCOpMode opMode, double offset) {
        this.offset = offset;

        this.content = ArtifactTypes.NONE;
    }

    public double getSlotOffset() {
        return offset;
    }

    public void setContent(ArtifactTypes content) {
        this.content = content;
    }
}
