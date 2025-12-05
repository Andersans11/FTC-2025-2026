package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;
import kotlin.Pair;

@Configurable
public class PoseTrackingTurret implements IAmBetterSubsystem {

    public static final PoseTrackingTurret INSTANCE = new PoseTrackingTurret();

    public MotorEx rotationMotor;
    public HuskyLens camera;
    public TouchSensor limitSwitch;
    public boolean isRedAlliance = true;
    public boolean hasSetAlliance = false;
    public boolean hasGotMotif = false;
    Pose pose = new Pose(0, 0, 0);
    Pose oldPose = new Pose(0, 0, 0);
    Pose redPose = new Pose(0, 0,-45); // temp
    Pose bluePose = new Pose(0, 0, 45); //temp
    Pose shootPose;
    double heightDiff = 100 - 50;
    /*
     TODO: these values are not accurate, the actual should be:
      height of where we want to shoot (possibly right above the ramp or smthn)
      -
      the height of the top of the turret rotation, basically where we shoot balls out of
     */
    public double targetYaw = 0;
    public double targetPitch = 0.0;
    public ControlSystem controller;
    public enum TurretMode {
        MANUAL_PID,
        MANUAL_POWER,
        POSE_TRACKING
    }

    boolean isZeroing = false;
    public double off = 0;

    public TurretMode mode = TurretMode.POSE_TRACKING;
    public Follower poseUpdater;
    private final Utils.ArtifactTypes[] GPPGPP = new Utils.ArtifactTypes[]{
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE
    };

    private final Utils.ArtifactTypes[] PGPPGP = new Utils.ArtifactTypes[]{
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE
    };

    private final Utils.ArtifactTypes[] PPGPPG = new Utils.ArtifactTypes[]{
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN
    };

    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.0075;
    public static double kI = 0.0;
    public static double kD = 0.0001;
    public static double a = 0.04;
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
        limitSwitch = RobotConfig.LimitSwitch;
    }

    @Override
    public void preStart() {

    }

    /**
     * zeroes the turret using
     * @return a sequential group that zeroes the turret
     */
    public Command zero() {
        return new SequentialGroup(
                new InstantCommand(() -> {
                    isZeroing = true;
                    mode = TurretMode.MANUAL_PID;
                    controller.setGoal(new KineticState(degreesToTicks(45)));
                }),
                new WaitUntil(() -> rotationMotor.getCurrentPosition() >= degreesToTicks(44)),
                new InstantCommand(() -> mode = TurretMode.MANUAL_POWER),
                new SetPower(rotationMotor, -0.5),
                new WaitUntil(() -> limitSwitch.isPressed()),
                new SetPower(rotationMotor, 0),
                new InstantCommand(() -> {
                    off = rotationMotor.getCurrentPosition();
                    isZeroing = false;
                })
        );
    }

    public Command resetHood() {
        return Shooter.INSTANCE.setHoodPos(0.5);
    }

    public Command resetPID() {
        return new InstantCommand(() -> controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build()
        );
    }

    /**
     * directly set the target position for the turret motor (within limits)
     * @param pos position to set the motor to
     * @return an InstantCommand that sets the position
     */
    public Command setPosition(double pos) {
        return new InstantCommand(() -> {
            mode = TurretMode.MANUAL_PID;
            targetYaw = Math.max(-90, Math.min(90, pos));
            controller.setGoal(new KineticState(off + degreesToTicks(targetYaw)));
        });
    }

    public Command setPosition(double turretPos, double hoodPos) {
        return new InstantCommand(() -> {
            mode = TurretMode.MANUAL_PID;
            targetYaw = Math.max(-90, Math.min(90, turretPos));
            controller.setGoal(new KineticState(off + degreesToTicks(targetYaw)));
        });
    }

    /**
     * change the current target angle by a given pos
     * @param pos the pos to change the target angle by
     * @return an InstantCommand that changes the target angle by the given pos
     */
    public Command changePosition(double pos) {
        return new InstantCommand(() -> {
            mode = TurretMode.MANUAL_PID;
            targetYaw = targetYaw + pos;
            targetYaw = Math.max(-90, Math.min(90, targetYaw));
            controller.setGoal(new KineticState(off + degreesToTicks(targetYaw)));
        });
    }

    public Command autoControl() {
        return new InstantCommand(() -> mode = TurretMode.POSE_TRACKING);
    }

    public Command setRedAlliance(boolean isRed) {
        return new InstantCommand(() -> {
            this.isRedAlliance = isRed;
            this.hasSetAlliance = true;
            this.shootPose = isRed ? redPose : bluePose;
        });
    }

    public double degreesToTicks(double degrees) {
        return degrees * 751.8 / 360 * 8;
    }
    public double ticksToDegrees(double ticks) {
        return ticks / 751.8 * 360 / 8;
    }

    public void manualUpdate() {
        rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
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

        if (Magazine.INSTANCE.mode == 0 && !isZeroing) {
            mode = TurretMode.MANUAL_PID;
            targetYaw = 0;
        } else if (Magazine.INSTANCE.mode == 1 && mode == TurretMode.MANUAL_PID && !isZeroing) mode = TurretMode.POSE_TRACKING;
        
        switch (mode) {
            case POSE_TRACKING:
                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.toDegrees(Math.atan((shootPose.getY() - pose.getY()) / (shootPose.getX() - pose.getX())));
                targetYaw = fixTarget(targetYaw - pose.getHeading());
                double hDistance = pose.distanceFrom(shootPose);
                targetPitch = Math.toDegrees(Math.atan(heightDiff / hDistance));
                targetPitch -= hoodAngleOffset;

                controller.setGoal(new KineticState(off + degreesToTicks(Math.max(-90, Math.min(90, targetYaw)))));
                rotationMotor.setPower(Math.max(-0.75, Math.min(0.75, controller.calculate(rotationMotor.getState()))));
                Shooter.INSTANCE.setHoodPos(calcHoodPower(targetPitch));
                break;
            case MANUAL_PID:
                rotationMotor.setPower(Math.max(-0.75, Math.min(0.75, controller.calculate(rotationMotor.getState()))));
                break;
        }

        if (!hasGotMotif) {
            if (camera.blocks(3).length != 0) {
                Magazine.INSTANCE.motif = GPPGPP;
                Magazine.INSTANCE.setMode(0);
                hasGotMotif = true;
            } else if (camera.blocks(4).length != 0) {
                Magazine.INSTANCE.motif = PGPPGP;
                Magazine.INSTANCE.setMode(0);
                hasGotMotif = true;
            } else if (camera.blocks(5).length != 0) {
                Magazine.INSTANCE.motif = PPGPPG;
                Magazine.INSTANCE.setMode(0);
                hasGotMotif = true;
            }
        }
    }

    public double fixTarget(double target) {
        while (target < 0) {
            target += 360;
        }
        while (target >= 360) { // if is 360, make 0
            target -= 360;
        }
        return target;
    }
}
