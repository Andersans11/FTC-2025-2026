package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.IAmBetterSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
public class Magazine implements IAmBetterSubsystem {

    public static final Magazine INSTANCE = new Magazine();
    MagSlot[] slots;
    public int activeSlot; // slot that receives the next ball
    public ServoExFullRange[] servos;
    public ColorRangeSensor color;
    public double targetPos = 0;
    public double oldTargetPos = 0;
    public Utils.ArtifactTypes[] motif = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN
    };
    public int shotsFired = 0;
    public Utils.ArtifactTypes desiredColor = Utils.ArtifactTypes.PURPLE;
    boolean usingSec = false;
    public double turretOff = 0;

    Utils.ArtifactTypes colorQueue = Utils.ArtifactTypes.NONE;

    // ------------------------------ CONFIG ----------------------------- //

    public static double off0 = 0;
    public static double off1 = 120;
    public static double off2 = 240;
    public static double off = 42;
    public static double dist = 110;
    public static double dist2 = 400;
    public int it = 0;
    public int mode = 0;

    public Timer timer;

    @Override
    public void initSystem() {
        this.slots = new MagSlot[] {
                new MagSlot(off0), // this slot starts in front of intake
                new MagSlot(off1),
                new MagSlot(off2)
        };
        this.activeSlot = 0;

        servos = new ServoExFullRange[]{
                RobotConfig.CarouselCR1.servo,
                RobotConfig.CarouselCR2.servo,
                RobotConfig.CarouselCR3.servo
        };

        this.color = RobotConfig.IntakeCS;

        servos[0].setPosition(slots[0].offset);
        servos[1].setPosition(slots[0].offset);
        servos[2].setPosition(slots[0].offset);

        timer = new Timer();
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {

        targetPos = ((180 + Turret.INSTANCE.targetAngle + turretOff) * mode) + slots[activeSlot].offset;

        if (targetPos != oldTargetPos) {
            while (targetPos + off >= 355) {
                targetPos = targetPos - 360;
            }
            servos[0].setPosition((targetPos + off) / 355);
            servos[1].setPosition((targetPos + off) / 355);
            servos[2].setPosition((targetPos + off) / 355);
            oldTargetPos = targetPos;
        }
        if (slots[activeSlot].content != desiredColor) changeActiveSlot().schedule();

        getColor();
    }

    // -------------------- COMMANDS / METHODS ------------------------ //
    public Command changeActiveSlot() {
        return new InstantCommand(() -> {
            boolean foundOne = false;
            if (slots[0].content == desiredColor) {
                activeSlot = 0;
                foundOne = true;
            } else if (slots[1].content == desiredColor) {
                activeSlot = 1;
                foundOne = true;
            } else if (slots[2].content == desiredColor) {
                activeSlot = 2;
                foundOne = true;
            } else if (desiredColor == Utils.ArtifactTypes.GREEN || desiredColor == Utils.ArtifactTypes.PURPLE) {
                if (slots[0].content == Utils.ArtifactTypes.GREEN || slots[0].content == Utils.ArtifactTypes.PURPLE) {
                    activeSlot = 0;
                    foundOne = true;
                } else if (slots[1].content == Utils.ArtifactTypes.GREEN || slots[1].content == Utils.ArtifactTypes.PURPLE) {
                    activeSlot = 1;
                    foundOne = true;
                } else if (slots[2].content == Utils.ArtifactTypes.GREEN || slots[2].content == Utils.ArtifactTypes.PURPLE) {
                    activeSlot = 2;
                    foundOne = true;
                }
                this.it++;
            }
            if (!foundOne) {
                if (mode == 0) setMode(1).schedule();
                else setMode(0).schedule();
            }
        });
    }


    public Command setActiveSlotContent(Utils.ArtifactTypes content) {
        return new InstantCommand(() -> {
            slots[activeSlot].content = content;
        });
    }

    public Command setSlotContent(int slot, Utils.ArtifactTypes content) {
        return new InstantCommand(() -> {
            slots[slot].content = content;
        });
    }

    public void getColor() {
        if (color.getDistance(DistanceUnit.MM) <= dist) { // Range now
            if ((color.red() + color.blue()) / 2 < color.green()) {
                colorQueue = Utils.ArtifactTypes.GREEN;
            } else {
                colorQueue = Utils.ArtifactTypes.PURPLE;
            }
            timer.resetTimer();
        } else if (color.getDistance(DistanceUnit.MM) <= dist2) {
            timer.resetTimer();
        } else if (colorQueue != Utils.ArtifactTypes.NONE && timer.getElapsedTimeSeconds() >= 0.25) {
            setActiveSlotContent(colorQueue).schedule();
            colorQueue = Utils.ArtifactTypes.NONE;
        }
    }

    /**
     * Sets the desired color
     * @param desiredColor Manual color you want
     **/
    public Command setDesiredColor(Utils.ArtifactTypes desiredColor) {
        return new InstantCommand(() -> this.desiredColor = desiredColor);
    }

    public Command incShotsFired() {
        return new InstantCommand(() -> {
            shotsFired++;
            if (shotsFired == 3) shotsFired = 0;
        });
    }

    /**
     * Sets the desired color to that of the next motif Artifact
     **/
    public Command setDesiredColor(int i) {
        return new InstantCommand(() -> this.desiredColor = motif[i + shotsFired]);
    }

    public Command setMode(int mode) {
        return new SequentialGroup(
            new Delay(0.25),
            new InstantCommand(() -> {
                if (mode == 0) desiredColor = Utils.ArtifactTypes.NONE;
                else desiredColor = motif[shotsFired];
                this.mode = mode;
                })
        );
    }
    public Utils.ArtifactTypes getSlotColor(int slot) {
        return slots[slot].content;
    }
}
