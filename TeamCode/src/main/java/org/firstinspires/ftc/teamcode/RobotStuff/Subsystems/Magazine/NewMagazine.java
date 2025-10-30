package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.IAmBetterSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.ServoEx;

public class NewMagazine implements IAmBetterSubsystem {
    public static final NewMagazine INSTANCE = new NewMagazine();
    MagSlot[] slots;
    public int activeSlot; // slot that receives the next ball
    public ServoEx[] servos;
    ColorSensor color;
    public double targetPos = 0;
    public double oldTargetPos = 0;
    public Utils.ArtifactTypes[] motif = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE
    };
    int shotsFired;
    public Utils.ArtifactTypes desiredColor;
    boolean usingSec = false;

    // ------------------------------ CONFIG ----------------------------- //
    public static double kP = 0.008;
    public static double kI = 0.001;
    public static double kD = 0.00013;

    public static double off0 = 0;
    public static double off1 = 120;
    public static double off2 = 240;
    public int i = 0;
    public int mode;

    @Override
    public void initSystem() {
        this.slots = new MagSlot[] {
                new MagSlot(off0), // this slot starts in front of intake
                new MagSlot(off1),
                new MagSlot(off2)
        };
        this.activeSlot = 0;

        this.servos = new ServoEx[] {
                RobotConfig.CarouselCR1.servo,
                RobotConfig.CarouselCR2.servo,
                RobotConfig.CarouselCR3.servo
        };

        this.color = RobotConfig.IntakeCS;

        setMode(0);
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {

        targetPos = ((180 + NewTurret.INSTANCE.targetAngle) * mode) + slots[activeSlot].offset;

        if (targetPos != oldTargetPos) {
            servos[0].setPosition(targetPos);
            servos[1].setPosition(targetPos);
            servos[2].setPosition(targetPos);
            oldTargetPos = targetPos;
        }
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
            }
            if (!foundOne) {
                if (mode == 0) setMode(1);
                else setMode(0);
                this.i++;
            }
        });
    }

    public Command setActiveSlotContent(Utils.ArtifactTypes content) {
        return new InstantCommand(() -> {
            slots[activeSlot].content = content;
            changeActiveSlot().schedule();
        });
    }

    /**
     * Sets the desired color
     * @param desiredColor Manual color you want
     **/
    public Command setDesiredColor(Utils.ArtifactTypes desiredColor) {
        return new InstantCommand(() -> this.desiredColor = desiredColor);
    }

    /**
     * Sets the desired color to that of the next motif Artifact
     **/
    public Command setDesiredColor() {
        return new InstantCommand(() -> this.desiredColor = motif[shotsFired]);
    }

    public Command setMode(int mode) {
        return new InstantCommand(() -> {
            if (mode == 0) desiredColor = Utils.ArtifactTypes.NONE;
            else desiredColor = motif[0];
            shotsFired = 0;
            this.mode = mode;
        });
    }

    public Command incShotsFired() {
        return new InstantCommand(() -> {
            shotsFired++;
            if (shotsFired == 3) shotsFired = 0;
            setActiveSlotContent(Utils.ArtifactTypes.NONE).schedule();
        });
    }

    public Utils.ArtifactTypes getSlotColor(int slot) {
        return slots[slot].content;
    }
}
