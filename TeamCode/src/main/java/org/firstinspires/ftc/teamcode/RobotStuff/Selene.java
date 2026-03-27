package org.firstinspires.ftc.teamcode.RobotStuff;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.LimelightWrapper;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
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

    public static Follower follower;
    boolean isShooting = false;
    public boolean isMotifShooting = false;
    public ServoImplEx indicator;
    public Timer distTimer;
    public Timer ballTimer;
    public static Pose currentPose;
    public Pose currentLLPose = new Pose(9, 9, 0);
    public boolean noLLPose = true;
    public boolean threshold = false;
    public LimelightWrapper limelight;
    public int indCycle = 0;
    public double shootTime = 0.75;
    public ColorRangeSensor dist;
    OpMode opmode;
    public boolean runDistance = false;
    public double distThreshold = 100;
    int hasSomething = 0;

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
        limelight = LimelightWrapper.instance; limelight.init();
        distTimer = new Timer();
        ballTimer = new Timer();
        indicator.setPwmRange(new PwmControl.PwmRange(500, 2500));
        indicator.setPwmEnable();
    }

    public void initFollower(HardwareMap hardwareMap) {
        if (follower == null) {
            follower = Constants.createFollower(hardwareMap);
            currentPose = new Pose(9, 9, 0);
            follower.setStartingPose(currentPose);
        }
    }

    public void initFollower(HardwareMap hardwareMap, Pose startingPose) {
        if (follower == null) {
            follower = Constants.createFollower(hardwareMap);
            follower.setStartingPose(startingPose);
        } else follower.setPose(startingPose);
        currentPose = startingPose;
    }
    public void getOpMode(OpMode opmode) {
        this.opmode = opmode;
        runDistance = true;
    }

    public Pose getCurrentPose() {
        return follower.getPose();
    }
    
    void runDist() {
        if (dist.getDistance(DistanceUnit.MM) <= distThreshold && hasSomething == 0) {
            hasSomething = 1;
            distTimer.resetTimer();
        } else if (dist.getDistance(DistanceUnit.MM) >= distThreshold && hasSomething != 0) {
            hasSomething = 0;
        } else if (dist.getDistance(DistanceUnit.MM) <= distThreshold && distTimer.getElapsedTimeSeconds() >= 0.5 && hasSomething == 1) {
            opmode.gamepad1.rumble(250);
            hasSomething = 2;
        }
    }

    @Override
    public void preStart() {
        super.preStart();

        limelight.start();
    }

    @Override
    public void periodic() {
        super.periodic();
        follower.updatePose();
        currentPose = follower.getPose();
        runDist();

        //limelight.setYaw(currentPose.getHeading());

        //PoseResult poseResult = limelight.getPoseResult();
        //currentPose = poseResult.assignIfThreshold(currentPose, poseThreshold, true);

        //follower.setPose(currentPose);

        runIndicator();
        if (Turret.INSTANCE.isInZone(currentPose) && !Turret.INSTANCE.isAtLimit() && autoShooting) Shooter.INSTANCE.StopperOpen().schedule();
        else if (autoShooting) Shooter.INSTANCE.StopperClose().schedule();
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
            Pose newPose = Turret.INSTANCE.isRed() ? new Pose(9, 7.5, Math.toRadians(90)) : new Pose(135, 7.5, Math.toRadians(90));
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
                    Shooter.INSTANCE.StopperOpen(),
                    new Delay(shootTime),
                    Shooter.INSTANCE.StopperClose(),
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
                Shooter.INSTANCE.StopperOpen(),
                intake(),
                new Delay(shootTime),
                Shooter.INSTANCE.StopperClose(),
                stopIntake(),
                new InstantCommand(() -> this.isMotifShooting = false)
        );
    }

    public Command start() {
        return new SequentialGroupFixed(
                Intake.INSTANCE.start(),
                Intake.INSTANCE.off(),
                Shooter.INSTANCE.StopperClose(),
                Turret.INSTANCE.resetHood()
        );
    }
}
