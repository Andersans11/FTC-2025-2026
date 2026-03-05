package org.firstinspires.ftc.teamcode.RobotStuff;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
public class Selene extends RRoboticsSubsystemGroup {

    public static final Selene INSTANCE = new Selene();

    public Follower follower;
    boolean isShooting = false;
    public boolean isMotifShooting = false;
    public ServoImplEx indicator;
    public Timer indTimer;
    public Timer ballTimer;
    public Pose currentPose = new Pose(9, 9, 0);
    public int indCycle = 0;
    public double shootTime = 0.5; // TODO: This value is kinda inaccurate

    public double currentPWM = 0;
    private Selene() {
        super(
                Turret.INSTANCE,
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

    @Override
    public void initSystem() {
        super.initSystem();
        indicator = RobotConfig.Indicator;
        indTimer = new Timer();
        ballTimer = new Timer();
        indicator.setPwmRange(new PwmControl.PwmRange(500, 2500));
        indicator.setPwmEnable();
    }

    public void initFollower(HardwareMap hardwareMap) {
        this.follower = Constants.createFollower(hardwareMap);
        this.follower.setStartingPose(currentPose);
    }

    public void initFollower(HardwareMap hardwareMap, Pose startingPose) {
        this.follower = Constants.createFollower(hardwareMap);
        this.follower.setStartingPose(startingPose);
    }

    public Pose getCurrentPose() {
        return this.follower.getPose();
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
        if (Turret.INSTANCE.isInZone(currentPose) && !Turret.INSTANCE.isAtLimit() && autoShooting) Shooter.INSTANCE.StopperUp().schedule();
        else if (autoShooting) Shooter.INSTANCE.StopperDown().schedule();
    }

    public boolean autoShooting = false;

    //  ------------------------- COMMANDS --------------------------- //

    public void runIndicator() {}

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
        return new InstantCommand(() -> {
            Pose newPose = Turret.INSTANCE.isRed() ? new Pose(9, 9, Math.toRadians(90)) : new Pose(135, 9, Math.toRadians(90));
            follower.setPose(newPose);
        });
    }

    public Command setAutoShooting() {
        return new InstantCommand(() -> autoShooting = !autoShooting);
    }



    public Command intake() {
        return new SequentialGroupFixed(
                Intake.INSTANCE.active()
        );
    }

    public Command outtake() {
        return new SequentialGroupFixed(
                Intake.INSTANCE.active(),
                Intake.INSTANCE.reverse()
        );
    }

    public Command stopIntake() {
        return new SequentialGroupFixed(
                Intake.INSTANCE.off(),
                Intake.INSTANCE.start()
        );
    }

    /**
     * Shoots a motif
     * @return a SequentialGroupFixed that shoots a motif
     */
    public Command shootMotif() { // TODO: Airsort
            return new SequentialGroupFixed(
                    new InstantCommand(() -> this.isMotifShooting = true),
                    Shooter.INSTANCE.StopperUp(),
                    new Delay(shootTime),
                    Shooter.INSTANCE.StopperDown(),
                    new InstantCommand(() -> this.isMotifShooting = false)
            );
    }

    /**
     * Shoots all Artifacts in the robot
     * @return a SequentialGroupFixed that shoots a motif
     */
    public Command shootAll() {
        return new SequentialGroupFixed(
                new InstantCommand(() -> this.isMotifShooting = true),
                Shooter.INSTANCE.StopperUp(),
                intake(),
                new Delay(shootTime),
                Shooter.INSTANCE.StopperDown(),
                stopIntake(),
                new InstantCommand(() -> this.isMotifShooting = false)
        );
    }

    public Command start() {
        return new SequentialGroupFixed(
                Intake.INSTANCE.start(),
                Intake.INSTANCE.off(),
                Shooter.INSTANCE.StopperDown(),
                Turret.INSTANCE.resetHood()
        );
    }
}
