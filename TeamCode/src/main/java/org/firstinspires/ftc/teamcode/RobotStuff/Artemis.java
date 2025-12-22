package org.firstinspires.ftc.teamcode.RobotStuff;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.PoseTracker;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.PoseTrackingTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.NullCommand;

@Configurable
public class Artemis extends BetterSubsystemGroup {

    public static final Artemis INSTANCE = new Artemis();

    public Follower follower;
    public PoseTracker followerTeleOp;
    boolean isShooting = false;
    public boolean isMotifShooting = false;
    public ServoImplEx indicator;
    public Timer indTimer;
    public Timer ballTimer;
    public Pose currentPose = new Pose(0, 0, 0);
    public static double offX = 9;
    public static double offY = 9;
    public static double offTheta = 90;
    boolean indCycle;
    double currentPWM = 0;
    private Artemis() {
        super(
                Magazine.INSTANCE,
                PoseTrackingTurret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    public enum IndicatorMode {
        INTAKE_IDLE,
        INTAKE_ACTIVE,
        HAS_BALL,
        SHOOTING_IDLE,
        SHOOTING_TRACKING,
        SHOOTING_TRACKED,
        SHOOTING_ACTIVE,
        INIT,
        INIT_DONE
    }

    public IndicatorMode indMode = IndicatorMode.INIT;

    public Utils.ArtifactTypes[] PPG = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN
    };
    public Utils.ArtifactTypes[] GPP = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE
    };
    public Utils.ArtifactTypes[] PGP = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE
    };

    @Override
    public void initSystem() {
        super.initSystem();
        indicator = RobotConfig.Indicator;
        indTimer = new Timer();
        ballTimer = new Timer();
        indicator.setPwmRange(new PwmControl.PwmRange(500, 2500));
        indicator.setPwmEnable();
    }

    public void initFollower(HardwareMap hardwareMap, boolean customStartingPose) {
        this.follower = Constants.createFollower(hardwareMap);
        PoseTrackingTurret.INSTANCE.initPoseUpdater(this.follower);
        if (customStartingPose) this.follower.setStartingPose(currentPose);
    }

    @Override
    public void preStart() {
        super.preStart();
    }

    @Override
    public void periodic() {
        super.periodic();
        follower.updatePose();
        currentPose = follower.getPose();
        runIndicator();
    }

    public static double motifShootingSpeed = 0.4;
    public static double shootingSpeed = 0.15;
    public static double shootingSpeed2 = 0.1;

    //  ------------------------- COMMANDS --------------------------- //

    public void runIndicator() {
        switch (indMode) {
            case INIT:
                setIndicator(1300);
                break;
            case INIT_DONE:
                setIndicator(1500);
                break;
            case INTAKE_IDLE:
                setIndicator(1650);
                break;
            case INTAKE_ACTIVE:
                if (indTimer.getElapsedTimeSeconds() >= 0.25) {
                    if (indCycle) {
                        setIndicator(1650);
                        indCycle = false;
                    } else {
                        setIndicator(0);
                        indCycle = true;
                    }
                    indTimer.resetTimer();
                }
                break;
            case HAS_BALL:
                switch (Magazine.INSTANCE.colorQueue) {
                    case PURPLE:

                }
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
        if (PoseTrackingTurret.INSTANCE.isRed) return new InstantCommand(() ->
            follower.setPose(new Pose(offX, offY, Math.toRadians(offTheta))));
        else return new InstantCommand(() ->
                follower.setPose(new Pose(offX + 54, offY, Math.toRadians(offTheta))));
    }

    /**
     * safely shoot a single artifact of a specified color, while also preventing an ArrayDeque error
     * @param color the color of artifact to shoot
     * @return a SequentialGroup that shoots a single artifact, or a NullCommand if the robot is already shooting
     */
    public Command shootSingle(Utils.ArtifactTypes color) {
        if (!isShooting) {
            this.isShooting = true;
            return new SequentialGroup(
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

    /**
     * used in shootMotif to shoot a single artifact depending on the motif
     * @param i the index of the motif
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
                    new Delay(0.75),
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
                Shooter.INSTANCE.idle(),
                PoseTrackingTurret.INSTANCE.resetHood()
        );
    }
}
