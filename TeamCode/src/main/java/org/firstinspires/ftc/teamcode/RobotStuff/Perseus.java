package org.firstinspires.ftc.teamcode.RobotStuff;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.PoseTracker;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.PoseTrackingTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.NullCommand;

@Configurable
public class Perseus extends BetterSubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public PoseTracker followerTeleOp;
    boolean isShooting = false;
    boolean isMotifShooting = false;
    public static double hoodToPos = 0.5;
    public ServoImplEx indicator;
    public Timer indTimer;
    public Timer ballTimer;
    public Pose currentPose;
    public static double offX;
    public static double offY;
    public static double offTheta;
    boolean indCycle;
    double currentPWM = 0;

    private Perseus() {
        super(
                Magazine.INSTANCE,
                PoseTrackingTurret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void initSystem() {
        super.initSystem();
        indicator = RobotConfig.Indicator;
        indTimer = new Timer();
        ballTimer = new Timer();
        indicator.setPwmRange(new PwmControl.PwmRange(500, 2500));
        indicator.setPwmEnable();
    }

    public void initFollower(Follower follower) {
        this.follower = follower;
        this.follower.setStartingPose(currentPose);
    }

    @Override
    public void preStart() {
        super.preStart();
    }

    @Override
    public void periodic() {
        super.periodic();
        currentPose = follower.getPose();
    }

    public static double motifShootingSpeed = 0.2;
    public static double shootingSpeed = 0.15;
    public static double shootingSpeed2 = 0.25;

    //  ------------------------- COMMANDS --------------------------- //

    public void runIndicator() {
        if (Magazine.INSTANCE.mode == 0) {
            if (Magazine.INSTANCE.hasBall) {
                if (Magazine.INSTANCE.colorQueue == Utils.ArtifactTypes.PURPLE) {
                    if (indTimer.getElapsedTimeSeconds() >= 0.5) {
                        if (indCycle) {
                            setIndicator(1900);
                            indCycle = false;
                        } else {
                            setIndicator(0);
                            indCycle = true;
                        }
                        indTimer.resetTimer();
                    }
                } else {
                    if (indTimer.getElapsedTimeSeconds() >= 0.5) {
                        if (indCycle) {
                            setIndicator(1450);
                            indCycle = false;
                        } else {
                            setIndicator(0);
                            indCycle = true;
                        }
                        indTimer.resetTimer();
                    }
                }
                if (ballTimer.getElapsedTimeSeconds() >= 3) {
                    Magazine.INSTANCE.hasBall = false;
                    ballTimer.resetTimer();
                }
            } else if (Intake.INSTANCE.intake.getPower() >= 0.8) {
                if (indTimer.getElapsedTimeSeconds() >= 0.25) {
                    if (indCycle) {
                        setIndicator(1700);
                        indCycle = false;
                    } else {
                        setIndicator(0);
                        indCycle = true;
                    }
                    indTimer.resetTimer();
                }
            } else setIndicator(1700);
        } else {
            if (isShooting || isMotifShooting) {
                if (indTimer.getElapsedTimeSeconds() >= 0.25) {
                    if (indCycle) {
                        setIndicator(1100);
                        indCycle = false;
                    } else {
                        setIndicator(0);
                        indCycle = true;
                    }
                    indTimer.resetTimer();
                }
            } else if (PoseTrackingTurret.INSTANCE.controller.isWithinTolerance(new KineticState(2.5))) {
                if (indTimer.getElapsedTimeSeconds() >= 0.5) {
                    if (indCycle) {
                        setIndicator(1200);
                        indCycle = false;
                    } else {
                        setIndicator(0);
                        indCycle = true;
                    }
                    indTimer.resetTimer();
                }
            } else setIndicator(1300);
        }
    }

    public void setIndicator(double PWM) {
        if (PWM != currentPWM) {
            indicator.setPosition(PWMToPower(PWM));
            currentPWM = PWM;
        }
    }

    public double PWMToPower(double PWM) {
        return (PWM - 500) / 2000;
    }

    public Command resetFollower() {
        return new InstantCommand(() ->
            follower.setPose(new Pose(offX, offY, offTheta)));
    }

    /**
     * safely shoot a single artifact of a specified color, while also preventing an ArrayDeque error
     * @param color the color of artifact to shoot
     * @return a SequentialGroup that shoots a single artifact, or a NullCommand if the robot is already shooting
     */
    public Command shootSingle(Utils.ArtifactTypes color) {
        if (!isShooting) {
            return new SequentialGroup(
                    new InstantCommand(() -> this.isShooting = true),
                    Magazine.INSTANCE.setDesiredColor(color),
                    Shooter.INSTANCE.spinUp(),
                    new Delay(shootingSpeed),
                    Shooter.INSTANCE.shoot(),
                    Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE),
                    Shooter.INSTANCE.idle(),
                    new InstantCommand(() -> this.isShooting = false)
            );
        } else {
            return new NullCommand();
        }
    }

    public Command updateHoodPos() {
        return Shooter.INSTANCE.setHoodPos(hoodToPos);
    }

    /**
     * used in shootMotif to shoot a single artifact depending on the motif
     * @param i the index of the slot
     * @return a SequentialGroup that shoots a single artifact, or a NullCommand if the robot is already shooting
     */
    public Command shootSingleMotif(int i) {
        return new SequentialGroup(
                Magazine.INSTANCE.setDesiredColor(i),
                new Delay(shootingSpeed),
                Shooter.INSTANCE.shoot(),
                new Delay(shootingSpeed2),
                Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE),
                Magazine.INSTANCE.setDesiredColor(i + 1)
        );
    }

    public Command intake() {
        return new SequentialGroup(
                Magazine.INSTANCE.setMode(0),
                Intake.INSTANCE.start()
        );
    }

    public Command outtake() {
        return Intake.INSTANCE.reverse();
    }

    public Command stopIntake() {
        return Intake.INSTANCE.idle();
    }

    /**
     * Shoots a motif
     * @return a SequentialGroup that shoots a motif, or a NullCommand if the robot is already shooting
     */
    public Command shootMotif() {
            return new SequentialGroup(
                    new InstantCommand(() -> this.isMotifShooting = true),
                    Shooter.INSTANCE.spinUp(),
                    new Delay(0.25),
                    shootSingleMotif(0),
                    new Delay(motifShootingSpeed),
                    shootSingleMotif(1),
                    new Delay(motifShootingSpeed),
                    shootSingleMotif(2),
                    Shooter.INSTANCE.idle(),
                    new InstantCommand(() -> this.isMotifShooting = false)
            );
    }

    public Command start() {
        return new SequentialGroup(
                Intake.INSTANCE.idle(),
                Shooter.INSTANCE.resetKicker(),
                PoseTrackingTurret.INSTANCE.resetHood()
        );
    }
}
