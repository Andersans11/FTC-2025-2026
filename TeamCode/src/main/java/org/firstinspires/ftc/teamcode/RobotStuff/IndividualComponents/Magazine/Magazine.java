package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Magazine;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.ColorSensor;

import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.core.commands.Command;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.RTPAxon;

public class Magazine implements Subsystem {

    enum MagazineMode {
        INTAKE,
        OUTTAKE,
        OUTTAKE_MANUAL
    }

    NextFTCOpMode opMode;
    MagSlot[] slots;
    int activeSlot; // slot that receives the next ball
    RTPAxon[] servos;
    ColorSensor color;
    double targetPos = 0;
    double oldTargetPos = 180;
    Timer deltatime;
    Utils.ArtifactTypes[] motif;
    MagazineMode mode = MagazineMode.OUTTAKE;
    int shotsFired;

    public void init(NextFTCOpMode opMode) {
        this.opMode = opMode;

        this.slots = new MagSlot[] {
                new MagSlot(opMode, 0), // this slot starts in front of intake
                new MagSlot(opMode,120),
                new MagSlot(opMode,-120)
        };
        this.activeSlot = 0;

        this.servos = new RTPAxon[] {
            new RTPAxon(RobotConfig.CarouselCR1),
            new RTPAxon(RobotConfig.CarouselCR2),
            new RTPAxon(RobotConfig.CarouselCR3)
        };

        this.color = RobotConfig.IntakeCS;

        deltatime = new Timer();
    }

    public void setMotif(Utils.ArtifactTypes[] motif) {
        this.motif = motif;
    }

    public Command setRotation(double to) {
        // TODO: take an angle and calculate servo rotation
        return new NullCommand();
    }

    public Command setMode(MagazineMode mode) {

        if (mode == MagazineMode.OUTTAKE) {
            for (int i = 0; i == 3; i++) {
                if (slots[i].content == motif[0]) {
                    activeSlot = i;
                    targetPos = 180 + slots[activeSlot].offset;
                    i = 3;
                }
            }

            shotsFired = 0;
        }

        this.mode = mode;
        return new NullCommand();
    }

    public void setActiveSlot(int slot) {
        activeSlot = slot;
    }


    public void update() {

        if (mode == MagazineMode.INTAKE) {

            if (slots[activeSlot].content != Utils.ArtifactTypes.NONE) {
                deltatime.resetTimer();
                if (deltatime.getElapsedTimeSeconds() >= 0.5) {
                    for (int i = 0; i == 3; i++) {
                        if (slots[i].content == Utils.ArtifactTypes.NONE) {
                            activeSlot = i;
                            targetPos = slots[activeSlot].offset;
                            i = 3;
                        }
                    }
                }
            }
        } else if (mode == MagazineMode.OUTTAKE) {
            if (slots[activeSlot].content == Utils.ArtifactTypes.NONE) {
                for (int i = 0; i == 3; i++) {
                    if (slots[i].content == motif[shotsFired]) {
                        activeSlot = i;
                        targetPos = 180 + slots[activeSlot].offset;
                        i = 3;
                    }
                }
            }
        } else {
            targetPos = 180 + slots[activeSlot].offset;
        }

        if (targetPos != oldTargetPos) {
            setRotation(targetPos);
            oldTargetPos = targetPos;
        }

        servos[0].update();
        servos[1].update();
        servos[2].update();
    }

}
