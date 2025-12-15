package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
public class Magazine implements IAmBetterSubsystem {

    public static final Magazine INSTANCE = new Magazine();
    List<MagSlot> slots;

    public int activeSlot; // slot that receives the next ball
    public ServoExFullRange[] servos;
    public ColorRangeSensor color;
    public DistanceSensor range;
    public double targetPos = 0;
    public double oldTargetPos = 0;
    public Utils.ArtifactTypes[] motif = Utils.PPGPPG;
    public int shotsFired = 0;
    public Utils.ArtifactTypes desiredColor = Utils.ArtifactTypes.PURPLE;
    boolean usingSec = false;
    public double turretOff = 0;

    public Utils.ArtifactTypes colorQueue = Utils.ArtifactTypes.NONE;

    // ------------------------------ CONFIG ----------------------------- //

    public static double off0 = 0;
    public static double off1 = 120;
    public static double off2 = 240;
    public static double off = 2;
    public static double dist = 60;
    public int it = 0;
    public int mode = 0;

    public boolean hasBall;

    public Timer timer;

    @Override
    public void initSystem() {
        this.slots = Arrays.asList(
                new MagSlot(off0), // this slot starts in front of intake
                new MagSlot(off1),
                new MagSlot(off2)
        );
        this.activeSlot = 0;

        servos = new ServoExFullRange[]{
                RobotConfig.CarouselCR1.getServo(),
                RobotConfig.CarouselCR2.getServo(),
                RobotConfig.CarouselCR3.getServo()
        };

        this.color = RobotConfig.IntakeCS;
        this.range = RobotConfig.IntakeDS;

        servos[0].setPosition(slots.get(0).offset);
        servos[1].setPosition(slots.get(0).offset);
        servos[2].setPosition(slots.get(0).offset);

        timer = new Timer();
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {

        targetPos = ((180 + PoseTrackingTurret.INSTANCE.targetYaw + turretOff) * mode) + slots.get(activeSlot).offset;

        if (targetPos != oldTargetPos) {
            while (targetPos + off >= 355) {
                targetPos = targetPos - 360;
            }
            servos[0].setPosition((targetPos + off) / 355);
            servos[1].setPosition((targetPos + off) / 355);
            servos[2].setPosition((targetPos + off) / 355);
            oldTargetPos = targetPos;
        }
        if (slots.get(activeSlot).content != desiredColor) changeActiveSlot().schedule();

        getColor();
    }

    // -------------------- COMMANDS / METHODS ------------------------ //
    public Command changeActiveSlot() {
        return new InstantCommand(() -> {
            AtomicBoolean foundOne = new AtomicBoolean(false);
            slots.forEach(magSlot -> {
                foundOne.compareAndSet(!magSlot.hasColor(desiredColor), true);
                activeSlot = slots.indexOf(magSlot);
            });
            // for each slot, check if it has desired color, then invert
            // if it has the color, the inversion makes it false, and since false matches the initial value of false, it sets it to true
            if (!foundOne.get()) {
                slots.forEach(magSlot -> {
                    foundOne.compareAndSet(!magSlot.hasColor(desiredColor), true);
                    activeSlot = slots.indexOf(magSlot);
                });
            }

            if (!foundOne.get()) {
                if (mode == 0) setMode(1).schedule();
                else setMode(0).schedule();
            }
        });
    }


    public Command setActiveSlotContent(Utils.ArtifactTypes content) {
        return new InstantCommand(() -> {
            slots.get(activeSlot).setContent(content);
            colorQueue = content;
        });
    }

    public int getSlotsFilled() {
        int numberFilled = 0;

        for (MagSlot slot : slots) if (slot.hasArtifact()) numberFilled++;

        return numberFilled;

    }

    public Command setSlotContent(int slot, Utils.ArtifactTypes content) {
        return new InstantCommand(() -> slots.get(slot).setContent(content));
    }

    public void getColor() {
        if (color.getDistance(DistanceUnit.MM) <= dist || range.getDistance(DistanceUnit.MM) <= dist) { // Range now
            if ((color.red() + color.blue()) / 2 < color.green()) {
                setActiveSlotContent(Utils.ArtifactTypes.GREEN).schedule();
                colorQueue = Utils.ArtifactTypes.GREEN;
            } else {
                setActiveSlotContent(Utils.ArtifactTypes.PURPLE).schedule();
                colorQueue = Utils.ArtifactTypes.PURPLE;
            }
            hasBall = true;
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
        return slots.get(slot).content;
    }

    public static class MagSlot {

        double offset;
        Utils.ArtifactTypes content;

        public MagSlot(double offset) {
            this.offset = offset;

            this.content = Utils.ArtifactTypes.NONE;
        }

        public boolean hasArtifact() {
            return this.content != Utils.ArtifactTypes.NONE;
        }

        public boolean hasColor(Utils.ArtifactTypes color) {
            return hasArtifact() && this.content == color;
        }

        public void setContent(Utils.ArtifactTypes content) {
            this.content = content;
        }
    }
}
