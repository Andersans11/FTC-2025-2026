package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.IAmBetterSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;

@Config
public class NewMagazine implements IAmBetterSubsystem {

    public static final NewMagazine INSTANCE = new NewMagazine();
    MagSlot[] slots;
    public int activeSlot; // slot that receives the next ball
    public RTPAxon[] servos;
    ColorSensor color;
    double targetPos = 0;
    double oldTargetPos = 180;
    public Utils.ArtifactTypes[] motif;
    int shotsFired;
    public Utils.ArtifactTypes desiredColor;

    // ------------------------------ CONFIG ----------------------------- //
    public static double kP = 0.008;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double off0 = 0;
    public static double off1 = 120;
    public static double off2 = 240;

    public int mode;

    @Override
    public void initSystem() {
        this.slots = new MagSlot[] {
                new MagSlot(off0), // this slot starts in front of intake
                new MagSlot(off1),
                new MagSlot(off2)
        };
        this.activeSlot = 0;

        this.servos = new RTPAxon[] {
                new RTPAxon(RobotConfig.CarouselCR1),
                new RTPAxon(RobotConfig.CarouselCR2),
                new RTPAxon(RobotConfig.CarouselCR3)
        };

        this.servos[0].setKP(kP);
        this.servos[0].setKI(kI);
        this.servos[0].setKD(kD);

        this.servos[1].setKP(kP);
        this.servos[1].setKI(kI);
        this.servos[1].setKD(kD);

        this.servos[2].setKP(kP);
        this.servos[2].setKI(kI);
        this.servos[2].setKD(kD);

        this.servos[0].setMaxPower(1);
        this.servos[1].setMaxPower(1);
        this.servos[2].setMaxPower(1);

        this.color = RobotConfig.IntakeCS;

        mode = 0; // 1 is shooting, 0 is intake
    }

    @Override
    public void preStart() {}

    @Override
    public void periodic() {

        if ((mode == 1 && slots[activeSlot].content != desiredColor)
                || (mode == 0 && slots[activeSlot].content != Utils.ArtifactTypes.NONE))
            changeActiveSlot();

        targetPos = ((180 + NewTurret.INSTANCE.targetAngle) * mode) + slots[activeSlot].offset;

        if (targetPos != oldTargetPos) {
            servos[0].changeTargetRotation(targetPos);
            servos[1].changeTargetRotation(targetPos);
            servos[2].changeTargetRotation(targetPos);
        }

        servos[0].update();
        servos[0].update();
        servos[0].update();
    }

    // -------------------- COMMANDS ------------------------ //
    public Command changeActiveSlot() {
        boolean foundOne = false;
        switch (mode) {
            case 0:
                for (int i = 0; i == 3; i++) {
                    if (slots[i].content == Utils.ArtifactTypes.NONE) {
                        activeSlot = i;
                        i = 3;
                        foundOne = true;
                    }
                }
                if (foundOne) mode = 1;
                break;
            case 1:
                for (int i = 0; i == 3; i++) {
                    if (slots[i].content == desiredColor) {
                        activeSlot = i;
                        i = 3;
                        foundOne = true;
                    }
                }
                if (foundOne) mode = 0;
                break;

        }

        return new NullCommand();
    }

    public Command setActiveSlotContent(Utils.ArtifactTypes content) {
        slots[activeSlot].content = content;

        return new NullCommand();
    }

    // --------------------- METHODS ------------------------ //

    /**
     * Sets the desired color
     * @param desiredColor Manual color you want
     **/
    public void setDesiredColor(Utils.ArtifactTypes desiredColor) {
        this.desiredColor = desiredColor;
    }

    /**
     * Sets the desired color to that of the next motif Artifact
     **/
    public void setDesiredColor() {
        this.desiredColor = motif[shotsFired];
    }

    public Utils.ArtifactTypes getSlotColor(int slot) {
        return slots[slot-1].content;
    }
}
