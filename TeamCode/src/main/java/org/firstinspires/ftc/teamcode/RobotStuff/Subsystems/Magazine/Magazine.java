package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.ColorSensor;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.commands.Command;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.IBetterSubsystem;

@Config
public class Magazine implements IBetterSubsystem {

    public static final Magazine INSTANCE = new Magazine();

    MultipleTelemetry telemetry;

    public enum MagazineMode {
        INTAKE,
        OUTTAKE,
    }

    MagSlot[] slots;
    public int activeSlot; // slot that receives the next ball
    public RTPAxon[] servos;
    ColorSensor color;
    double targetPos = 0;
    double oldTargetPos = 180;
    Timer deltatime;
    Utils.ArtifactTypes[] motif;
    int shotsFired;
    public double turretPos;
    public double oldTurretPos;
    boolean colorShooting = false;
    boolean manualControl = false;

    public static double kP = 0.008;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double off0 = 0;
    public static double off1 = 120;
    public static double off2 = 240;

    public boolean yej = false;
    public int tre = 0;
    public int fal = 0;

    @Override
    public void initialize() {
        this.slots = new MagSlot[] {
                new MagSlot(off0), // this slot starts in front of intake
                new MagSlot(off1),
                new MagSlot(off2)
        };
        this.activeSlot = 0;

        deltatime = new Timer();

        telemetry = new MultipleTelemetry();

        this.yej = false;
    }

    @Override
    public void hardware() {
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
    }

    @Override
    public void commands() {

    }

    public void resetEncoders() {
        servos[0].forceResetTotalRotation();
        servos[1].forceResetTotalRotation();
        servos[2].forceResetTotalRotation();
    }

    @Override
    public void binds() {
        RobotConfig.ButtonControls.MAGAZINE_SLOT1.whenTrue(this::slot1);
        RobotConfig.ButtonControls.MAGAZINE_SLOT2.whenTrue(this::slot2);
        RobotConfig.ButtonControls.MAGAZINE_SLOT3.whenTrue(this::slot3);
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

    public String getContent(int slot) {
        switch (slots[slot].content) {
            case PURPLE:
                return "PURPLE";
            case GREEN:
                return "GREEN";
        }
        return "NONE";
    }

    public Command incShotsFired() {
        shotsFired++;
        if (shotsFired == 3) {
            shotsFired = 0;
        }
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

    public Command ColorShooting(Utils.ArtifactTypes color) {
        colorShooting = true;
        for (int i = 0; i == 3; i++) {
            if (slots[i].content == color || i == 2) {
                activeSlot = i;
                targetPos = 180 + slots[activeSlot].offset + turretPos;
                while (targetPos >= 360) {
                    targetPos = targetPos - 360;
                }
                i = 3;
            }
        }
        shotsFired = 0;
        return new NullCommand();
    }

    public String getModeString() {
        if (yej) {
            return "INTAKE";
        } else {
            return "OUTTAKE";
        }
    }
    public Command setMode(boolean mode) {

        if (!mode) {
            colorShooting = false;
            for (int i = 0; i == 3; i++) {
                if (slots[i].content == motif[0] || i == 2) {
                    activeSlot = i;
                    targetPos = 180 + slots[activeSlot].offset + turretPos;
                    while (targetPos >= 360) {
                        targetPos = targetPos - 360;
                    }
                    i = 3;
                }
            }
            fal++;
            //this.yej = false;
            shotsFired = 0;
        } else {
            tre++;
            this.yej = true;
        }
        return new NullCommand();
    }

    public Command setActiveSlot(int slot) {
        activeSlot = slot;
        return new NullCommand();
    }

    public Command slot1() {
        return setActiveSlot(0);
    }
    public Command slot2() {
        return setActiveSlot(1);
    }
    public Command slot3() {
        return setActiveSlot(2);
    }
    public double getTargetPos(){
        return (servos[0].getTargetRotation() + servos[1].getTargetRotation() + servos[2].getTargetRotation()) / 3;
    }

    public double getActualPos(){
        return (servos[0].getTotalRotation() + servos[1].getTotalRotation() + servos[2].getTotalRotation()) / 3;
    }

    @Override
    public void periodic() {
        if (!manualControl) {
            if (yej) {

                if (slots[activeSlot].content != Utils.ArtifactTypes.NONE) {
                    deltatime.resetTimer();
                    if (deltatime.getElapsedTimeSeconds() >= 0.5) {
                        for (int i = 0; i == 3; i++) {
                            if (slots[i].content == Utils.ArtifactTypes.NONE) {
                                activeSlot = i;
                                i = 3;
                            }
                        }
                    }
                }
            } else {
                if (slots[activeSlot].content == Utils.ArtifactTypes.NONE && !colorShooting) {
                    for (int i = 0; i == 3; i++) {
                        if (slots[i].content == motif[shotsFired] || i == 2) {
                            activeSlot = i;
                            i = 3;
                        }
                    }
                }
            }

            if (turretPos != oldTurretPos) {
                if (yej) {
                    targetPos = slots[activeSlot].offset;
                } else {
                    targetPos = 180 + slots[activeSlot].offset + turretPos;
                }
                while (targetPos >= 360) {
                    targetPos = targetPos - 360;
                }
                oldTurretPos = turretPos;
            }
        } else {
            targetPos = slots[activeSlot].offset;
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
