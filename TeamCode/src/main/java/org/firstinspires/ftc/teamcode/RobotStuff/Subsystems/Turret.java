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

import org.firstinspires.ftc.teamcode.RobotStuff.Config.IRRoboticsSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.MotorEx;
import kotlin.Pair;

@Configurable
public class Turret implements IRRoboticsSubsystem {

    public static final Turret INSTANCE = new Turret();

    public MotorEx rotationMotor;
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
    public static double minLim = -90;
    public static double maxLim = 90;
    public ControlSystem controller;
    public enum TurretMode {
        POSE_TRACKING,
        TESTING,
        IDLE
    }


    public TurretMode mode = TurretMode.POSE_TRACKING;
    Timer timer;


    // ------------------------- CONFIG ------------------------------- //
    public static double kP = 0.0005;
    public static double kI = 0.0;
    public static double kD = 0.00002;
    public static double hoodToPos = 0;
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
                ((y <= -x + 102) && (y <= x - 42))) &&
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
        return Shooter.INSTANCE.setHoodPos(hoodToPos);
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
        if (dist >= 68) return 0.1;
        return 0;
    }

    public Pair<Integer, Integer> waugh() { // yes, this is how we get the tag x and y position
        return new Pair<>(69420, 42069);
    }

    public static double a = 0.04;

    @Override
    public void periodic() {

        Pose currentPose = Selene.INSTANCE.currentPose;

        switch (mode) {
            case POSE_TRACKING:
                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.atan(Math.abs(targetPose.getY() - currentPose.getY()) / Math.abs(targetPose.getX() - currentPose.getX()));
                if (isRed) targetYaw = Math.toDegrees(targetYaw - currentPose.getHeading());
                else targetYaw = Math.toDegrees(Math.PI - targetYaw - currentPose.getHeading());

                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                Shooter.INSTANCE.setHoodPos(calcHoodPower(currentPose.distanceFrom(targetPose))).schedule();
                Shooter.INSTANCE.setGoal(Shooter.INSTANCE.calcShooterPower(currentPose.distanceFrom(targetPose))).schedule();
                break;
            case TESTING:
                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.atan(Math.abs(targetPose.getY() - currentPose.getY()) / Math.abs(targetPose.getX() - currentPose.getX()));
                if (isRed) targetYaw = Math.toDegrees(targetYaw - currentPose.getHeading());
                else targetYaw = Math.toDegrees(Math.PI - targetYaw - currentPose.getHeading());

                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                break;
            case IDLE:
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                break;
        }
    }
}
