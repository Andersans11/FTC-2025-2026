package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.GPPGPP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_GPP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_PGP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_PPG;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.PGPPGP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.PPGPPG;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.MotorEx;
import kotlin.Pair;

@Configurable
public class PoseTrackingTurret implements IAmBetterSubsystem {

    public static final PoseTrackingTurret INSTANCE = new PoseTrackingTurret();

    public MotorEx rotationMotor;
    public HuskyLens camera;
    public VoltageSensor voltageSensor;
    public boolean isRed = true;
    public boolean hasSetAlliance = false;
    public boolean hasGotMotif = false;
    Pose pose = new Pose(0, 0, 0);
    Pose oldPose = new Pose(0, 0, 0);
    Pose redPose = new Pose(144, 144);
    Pose bluePose = new Pose(0, 144);
    public Pose targetPose = new Pose(144, 144);
    Pose motifPose = new Pose(144, 72);
    public double targetYaw = 0;
    public double targetPitch = 0.0;
    public static double minLim = -45;
    public static double maxLim = 40;
    public ControlSystem controller;
    public enum TurretMode {
        POSE_TRACKING,
        IDLE
    }


    public TurretMode mode = TurretMode.POSE_TRACKING;
    public Follower poseUpdater;
    Timer timer;

    double voltageMid = 12.5;
    double altPerV = -0.02;

    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.0005;
    public static double kI = 0.0;
    public static double kD = 0.00002;
    public static double hoodToPos = 0.2;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build();
        timer = new Timer();
    }

    public void initPoseUpdater(OpMode opmode) {
        poseUpdater = Constants.createFollower(opmode.hardwareMap);
        poseUpdater.setStartingPose(pose);
    }

    public void initPoseUpdater(Follower follower) {
        poseUpdater = follower;
    }

    @Override
    public void initSystem() {
        this.rotationMotor = RobotConfig.TurretRotation.getMotor();
        camera = new HuskyLens(RobotConfig.camera.getDeviceClient());
        camera.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        voltageSensor = RobotConfig.VoltageSensor;
    }

    @Override
    public void preStart() {

    }

    public Command setIdle() {
        return new InstantCommand(() -> this.mode = TurretMode.IDLE);
    }

    public Command setTracking() {
        return new InstantCommand(() -> this.mode = TurretMode.IDLE);
    }

    public Command trackMotif() {
        return new InstantCommand(() -> this.targetPose = motifPose);
    }

    public Command trackGoal() {
        return new InstantCommand(() -> {
            this.targetPose = isRed ? redPose : bluePose;
        });
    }

    public Command resetPose() {
        return new InstantCommand(() -> {
            Pose newPose = this.isRed ? new Pose(9, 9, Math.toRadians(90)) : new Pose(135, 9, Math.toRadians(90)); // one pose two pose red pose blue pose
            poseUpdater.setPose(newPose);
        });
    }

    public Command resetPID() {
        return new InstantCommand(() -> controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build()
        );
    }

    public Command resetHood() {
        return Shooter.INSTANCE.setHoodPos(hoodToPos);
    }


    /**
     * directly set the target position for the turret motor (within limits)
     * @param pos position to set the motor to
     * @return an InstantCommand that sets the position
     */
    public Command setPosition(double pos) {
        return new InstantCommand(() -> {
            targetYaw = Math.max(-90, Math.min(90, pos));
            controller.setGoal(new KineticState(degreesToTicks(targetYaw)));
        });
    }

    public Command setPosition(double turretPos, double hoodPos) {
        return new InstantCommand(() -> {
            targetYaw = Math.max(-90, Math.min(90, turretPos));
            controller.setGoal(new KineticState(degreesToTicks(targetYaw)));
        });
    }

    /**
     * change the current target angle by a given pos
     * @param pos the pos to change the target angle by
     * @return an InstantCommand that changes the target angle by the given pos
     */
    public Command changePosition(double pos) {
        return new InstantCommand(() -> {
            targetYaw = targetYaw + pos;
            targetYaw = Math.max(-90, Math.min(90, targetYaw));
            controller.setGoal(new KineticState(degreesToTicks(targetYaw)));
        });
    }

    public Command autoControl() {
        return new InstantCommand(() -> mode = TurretMode.POSE_TRACKING);
    }

    public Command setRedAlliance() {
        return new InstantCommand(() -> {
            if (!hasSetAlliance) {
                this.isRed = true;
                this.targetPose = redPose;
            }
        });
    }

    public Command setBlueAlliance() {
        return new InstantCommand(() -> {
            if (!hasSetAlliance) {
                this.isRed = false;
                this.targetPose = bluePose;
            }
        });
    }

    public double degreesToTicks(double degrees) {
        return degrees * 8000 / 360 * 5;
    }

    public double ticksToDegrees(double ticks) {
        return ticks / 8000 * 360 / 5;
    }

    public double calcHoodPower(double dist) {
        double voltageDiff = voltageSensor.getVoltage() - voltageMid;
        double powerMod = altPerV * voltageDiff;

        if (dist >= 100) return 0.78;
        else if (dist <= 25) return 0.85;
        return -(0.001 * dist) + 0.85 + powerMod;
    }

    public Pair<Integer, Integer> waugh() { // yes, this is how we get the tag x and y position
        try {
            HuskyLens.Block tag;
            if (isRed) {
                tag = camera.blocks(1)[0];
            } else {
                tag = camera.blocks(2)[0];
            }
            return new Pair<>(tag.x, tag.y);
        } catch (RuntimeException reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee) {
            return new Pair<>(69420, 42069);
        }
    }

    public static double a = 0.04;

    boolean started = false;

    @Override
    public void periodic() {
        Pair<Integer, Integer> waluigi = waugh();

        poseUpdater.update();
        oldPose = pose;
        pose = poseUpdater.getPose();

        //if (Magazine.INSTANCE.mode == 0 && mode == TurretMode.POSE_TRACKING) mode = TurretMode.IDLE;
        //else if (Magazine.INSTANCE.mode == 1 && mode == TurretMode.IDLE) mode = TurretMode.POSE_TRACKING;
        
        switch (mode) {
            case POSE_TRACKING:
                if (waluigi.component1() != 69420) {
                    targetYaw = ticksToDegrees(rotationMotor.getCurrentPosition()) - (a * (waluigi.component1() - 160));
                    timer.resetTimer();
                } else if (timer.getElapsedTimeSeconds() >= 2.5) {
                    mode = TurretMode.IDLE;
                } else {
                    double deltaHeading = (pose.getHeading()) - oldPose.getHeading();
                    targetYaw = targetYaw - Math.toDegrees(deltaHeading);
                }
                Shooter.INSTANCE.hood.setPosition(calcHoodPower(Artemis.INSTANCE.currentPose.distanceFrom(targetPose)));
                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
                break;
            case IDLE:
                if (waluigi.component1() != 69420) {
                    targetYaw = ticksToDegrees(rotationMotor.getCurrentPosition()) - (a * (waluigi.component1() - 160));
                    Shooter.INSTANCE.hood.setPosition(calcHoodPower(Artemis.INSTANCE.currentPose.distanceFrom(targetPose)));
                    timer.resetTimer();
                    mode = TurretMode.POSE_TRACKING;
                    started = false;
                } else {
                    if (!started) {
                        targetYaw = 0;
                        controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                        started = true;
                    }
                    rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
                }
                break;
        }

        if (!hasGotMotif) {
            if (camera.blocks(MOTIF_GPP).length != 0) {
                Magazine.INSTANCE.motif = GPPGPP;
                Magazine.INSTANCE.setMode(0);
                trackGoal();
                hasGotMotif = true;
            } else if (camera.blocks(MOTIF_PGP).length != 0) {
                Magazine.INSTANCE.motif = PGPPGP;
                Magazine.INSTANCE.setMode(0);
                trackGoal();
                hasGotMotif = true;
            } else if (camera.blocks(MOTIF_PPG).length != 0) {
                Magazine.INSTANCE.motif = PPGPPG;
                Magazine.INSTANCE.setMode(0);
                trackGoal();
                hasGotMotif = true;
            }
        }
    }
}
