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
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class PoseTrackingTurret implements IAmBetterSubsystem {

    public static final PoseTrackingTurret INSTANCE = new PoseTrackingTurret();

    public MotorEx rotationMotor;
    public HuskyLens camera;
    public boolean isRed = true;
    public boolean hasSetAlliance = false;
    public boolean hasGotMotif = false;
    Pose pose = new Pose(0, 0, 0);
    Pose oldPose = new Pose(0, 0, 0);
    Pose redPose = new Pose(144, 144);
    Pose bluePose = new Pose(0, 144);
    Pose shootPose = new Pose(144, 144);
    double heightDiff = 100 - 50;
    /*
     TODO: these values are not accurate, the actual should be:
      height of where we want to shoot (possibly right above the ramp or smthn)
      -
      the height of the top of the turret rotation, basically where we shoot balls out of
     */
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

    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.0005;
    public static double kI = 0.0;
    public static double kD = 0.00002;
    public static double hoodToPos = 0.2;
    public static double hoodAngleOffset = 90;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build();
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

    public Command resetPose() {
        return new InstantCommand(() -> {
            Pose newPose = this.isRed ? redPose : bluePose; // red pose blue pose one pose two pose
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
                this.shootPose = redPose;
                this.hasSetAlliance = true;
            }
        });
    }

    public Command setBlueAlliance() {
        return new InstantCommand(() -> {
            if (!hasSetAlliance) {
                this.isRed = false;
                this.shootPose = bluePose;
                this.hasSetAlliance = true;
            }
        });
    }

    public double degreesToTicks(double degrees) {
        return degrees * 8000 / 360 * 5;
    }

    public double ticksToDegrees(double ticks) {
        return ticks / 8000 * 360 / 5;
    }

    public double calcHoodPower(double target) {
        if (target > 40) return 1;
        return target / 40;
    }

    @Override
    public void periodic() {

        poseUpdater.update();
        oldPose = pose;
        pose = poseUpdater.getPose();

        //if (Magazine.INSTANCE.mode == 0) mode = TurretMode.IDLE;
        
        switch (mode) {
            case POSE_TRACKING:
                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.atan((shootPose.getY() - pose.getY()) / (shootPose.getX() - pose.getX()));
                targetYaw = Math.toDegrees(targetYaw - pose.getHeading());
                double hDistance = pose.distanceFrom(shootPose);
                targetPitch = Math.toDegrees(Math.atan(heightDiff / hDistance));
                targetPitch -= hoodAngleOffset;

                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, -targetYaw)))));
                rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
                //Shooter.INSTANCE.setHoodPos(calcHoodPower(targetPitch));
                break;
            case IDLE:
                controller.setGoal(new KineticState(0));
                rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
                break;
        }

        if (!hasGotMotif) {
            if (camera.blocks(MOTIF_GPP).length != 0) {
                Magazine.INSTANCE.motif = GPPGPP;
                Magazine.INSTANCE.setMode(0);
                hasGotMotif = true;
            } else if (camera.blocks(MOTIF_PGP).length != 0) {
                Magazine.INSTANCE.motif = PGPPGP;
                Magazine.INSTANCE.setMode(0);
                hasGotMotif = true;
            } else if (camera.blocks(MOTIF_PPG).length != 0) {
                Magazine.INSTANCE.motif = PPGPPG;
                Magazine.INSTANCE.setMode(0);
                hasGotMotif = true;
            }
        }
    }
}
