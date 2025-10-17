package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.ColorSensor;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.commands.Command;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;

public class Magazine implements Subsystem {

    public static final Magazine INSTANCE = new Magazine();

    public enum MagazineMode {
        INTAKE,
        OUTTAKE_MOTIF,
        OUTTAKE,
        OUTTAKE_MANUAL
    }

    MagSlot[] slots;
    int activeSlot; // slot that receives the next ball
    RTPAxon[] servos;
    ColorSensor color;
    double targetPos = 0;
    double oldTargetPos = 180;
    Timer deltatime;
    Utils.ArtifactTypes[] motif;
    MagazineMode mode = MagazineMode.OUTTAKE_MOTIF;
    int shotsFired;
    double turretPos;

    @Override
    public void initialize() {

        this.slots = new MagSlot[] {
                new MagSlot(0), // this slot starts in front of intake
                new MagSlot(120),
                new MagSlot(-120)
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

    public void getColor() {
        if (Math.abs((color.red() + color.blue())/2 - color.green()) >= 100) { // If the difference between green and purple is greater than a certain value
            if ((color.red() + color.blue()) / 2 < color.green()) {
                slots[activeSlot].setContent(Utils.ArtifactTypes.GREEN);
            } else {
                slots[activeSlot].setContent(Utils.ArtifactTypes.PURPLE);
            }
        }
    }

    public Command incShotsFired() {
        shotsFired++;
        return new NullCommand();
    }

    public void passTurretPos(double pos) {
        turretPos = pos;
    }

    public Command setRotation(double angleDegrees) {
        servos[0].setTargetRotation(angleDegrees);
        servos[1].setTargetRotation(angleDegrees);
        servos[2].setTargetRotation(angleDegrees);
        return new NullCommand();
    }

    public Command setMode(MagazineMode mode) {

        if (mode == MagazineMode.OUTTAKE_MOTIF) {
            for (int i = 0; i == 3; i++) {
                if (slots[i].content == motif[0]) {
                    activeSlot = i;
                    targetPos = 180 + slots[activeSlot].offset + turretPos;
                    while (targetPos >= 360) {
                        targetPos = targetPos - 360;
                    }
                    i = 3;
                }
            }

            shotsFired = 0;
        }

        this.mode = mode;
        return new NullCommand();
    }

    public MagazineMode getMode() {
        return mode;
    }

    public Command setActiveSlot(int slot) {
        activeSlot = slot;
        return new NullCommand();
    }

    public Command slot1() {
        targetPos = slots[0].offset;

        if (targetPos != oldTargetPos) {
            setRotation(targetPos);
            oldTargetPos = targetPos;
        }

        servos[0].update();
        servos[1].update();
        servos[2].update();

        return setActiveSlot(0);
    }
    public Command slot2() {
        targetPos = slots[1].offset;

        if (targetPos != oldTargetPos) {
            setRotation(targetPos);
            oldTargetPos = targetPos;
        }

        servos[0].update();
        servos[1].update();
        servos[2].update();

        return setActiveSlot(1);
    }
    public Command slot3() {
        targetPos = slots[2].offset;

        if (targetPos != oldTargetPos) {
            setRotation(targetPos);
            oldTargetPos = targetPos;
        }

        servos[0].update();
        servos[1].update();
        servos[2].update();

        return setActiveSlot(2);
    }



    @Override
    public void periodic() {
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
        } else if (mode == MagazineMode.OUTTAKE_MOTIF) {
            if (slots[activeSlot].content == Utils.ArtifactTypes.NONE) {
                for (int i = 0; i == 3; i++) {
                    if (slots[i].content == motif[shotsFired]) {
                        activeSlot = i;
                        targetPos = 180 + slots[activeSlot].offset + turretPos;
                        while (targetPos >= 360) {
                            targetPos = targetPos - 360;
                        }
                        i = 3;
                    }
                }
            }
        } else if (mode == MagazineMode.OUTTAKE) {
            if (slots[activeSlot].content == Utils.ArtifactTypes.NONE) {
                for (int i = 0; i == 3; i++) {
                    if (slots[i].content != Utils.ArtifactTypes.NONE) {
                        activeSlot = i;
                        targetPos = 180 + slots[activeSlot].offset + turretPos;
                        while (targetPos >= 360) {
                            targetPos = targetPos - 360;
                        }
                        i = 3;
                    }
                }
            }

        } else {
            targetPos = 180 + slots[activeSlot].offset + turretPos;
            while (targetPos >= 360) {
                targetPos = targetPos - 360;
            }
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
