package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.GPPGPP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_GPP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_PGP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_PPG;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.PGPPGP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.PPGPPG;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;
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
    public boolean isLookingForMotif = false;
    Pose redPose = new Pose(136, 136);
    Pose bluePose = new Pose(8, 136);
    public Pose targetPose = new Pose(136, 136);
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
    Timer timer;


    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.0005;
    public static double kI = 0.0;
    public static double kD = 0.00002;
    public static double hoodToPos = 0.8;
    public static double farHoodPos = 0.84;
    public static double closeHoodPos = 0.9;
    public boolean isFar;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build();
        timer = new Timer();
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
        return new InstantCommand(() -> this.mode = TurretMode.POSE_TRACKING);
    }

    public boolean isInZone(Pose currentPose) {
        double x = currentPose.getX();
        double y = currentPose.getY();
        return (((y >= -x + 144) && (y >= x)) ||
                ((y <= -x + 96) && (y <= x - 48))) &&
                !isAtLimit();
    }

    public boolean isAtLimit() {
        return ticksToDegrees(controller.getGoal().getPosition()) == maxLim || ticksToDegrees(controller.getGoal().getPosition()) == minLim;
    }

    public Command toggleTrackObelisk() {
        return new InstantCommand(() -> {
            if (isLookingForMotif) {
                this.isLookingForMotif = false;
                this.targetPose = isRed ? redPose : bluePose;
            } else {
                this.isLookingForMotif = true;
                this.targetPose = motifPose;
            }
        });
    }
    public Command trackGoal() {
        return new InstantCommand(() -> {
            this.isLookingForMotif = false;
            this.targetPose = isRed ? redPose : bluePose;
        });
    }

    public boolean isRed() {
        return isRed;
    }

    public Command resetPID() {
        return new InstantCommand(() -> controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build()
        );
    }

    public Command resetHood() {
        return VPIDShooter.INSTANCE.setHoodPos(hoodToPos);
    }


    /**
     * directly set the target position for the turret motor (within limits)
     * @param pos position to set the motor to
     * @return an InstantCommand that sets the position
     */
    public Command setPosition(double pos) {
        return new InstantCommand(() -> {
            mode = TurretMode.IDLE;
            targetYaw = Math.max(minLim, Math.min(maxLim, pos));
            controller.setGoal(new KineticState(degreesToTicks(targetYaw)));
        });
    }

    public Command setPosition(double pos, double hoodPos) {
        return new InstantCommand(() -> {
            mode = TurretMode.IDLE;
            targetYaw = Math.max(minLim, Math.min(maxLim, pos));
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

        if (dist >= 100) {
            isFar = true;
            return farHoodPos;
        }
        else if (dist <= 25) {
            isFar = false;
            return 0.86;
        }
        isFar = false;
        return -(0.001 * dist) + closeHoodPos;
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

    @Override
    public void periodic() {

        Pose currentPose = Artemis.INSTANCE.currentPose;

        switch (mode) {
            case POSE_TRACKING:
                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.atan(Math.abs(targetPose.getY() - currentPose.getY()) / Math.abs(targetPose.getX() - currentPose.getX()));
                if (isRed) targetYaw = Math.toDegrees(targetYaw - currentPose.getHeading());
                else targetYaw = Math.toDegrees(Math.PI - targetYaw - currentPose.getHeading());

                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
                VPIDShooter.INSTANCE.setHoodPos(calcHoodPower(currentPose.distanceFrom(targetPose))).schedule();
                break;
            case IDLE:
                rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
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
