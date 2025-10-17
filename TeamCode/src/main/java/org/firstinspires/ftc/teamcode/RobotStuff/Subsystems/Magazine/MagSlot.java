package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.ArtifactTypes;


public class MagSlot {

    double offset;
    ArtifactTypes content;

    public MagSlot(double offset) {
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
