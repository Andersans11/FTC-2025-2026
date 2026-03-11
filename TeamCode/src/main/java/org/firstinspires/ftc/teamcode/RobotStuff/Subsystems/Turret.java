package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.GPPGPP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_GPP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_PGP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.MOTIF_PPG;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.PGPPGP;
import static org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.PPGPPG;

import android.util.SparseArray;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.pedropathing.util.Timer;
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
        NO_SHOOTER,
        TESTING,
        IDLE
    }


    public TurretMode mode = TurretMode.POSE_TRACKING;
    Timer timer;

    public Vector robotVel;

    // ------------------------- CONFIG ------------------------------- //

    public static double closeScoreHeight = 40;
    public static double closeScoreAngle = -30;
    public double closeScoreAngleRad = Math.toRadians(closeScoreAngle);
    public static double closeScoreRadius = 5;

    public static double farScoreHeight = 40;
    public static double farScoreAngle = -20;
    public double farScoreAngleRad = Math.toRadians(farScoreAngle);
    public static double farScoreRadius = 5;
    public static double kP = 0.0005;
    public static double kI = 0.0;
    public static double kD = 0.00002;
    public int hoodToPos = 0;
    public boolean isFar;

    public double hoodAngle = 0;
    public double flywheelSpeed = 0;

    public double newHoodAngle = 0;
    public double newFlywheelSpeed = 0;

    public double turretDistFromCenter = 1.72149606;

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
        robotVel = new Vector();
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
        return (((y >= -x + 128) && (y >= x - 16)) ||
                ((y <= -x + 112) && (y <= x - 32))) &&
                !isAtLimit();
    }

    public boolean isAtLimit() {
        return ticksToDegrees(controller.getGoal().getPosition()) == maxLim || ticksToDegrees(controller.getGoal().getPosition()) == minLim;
    }

    public double calculateTicksPerSecond(double velocity, double launchAngle) {
        return (1100.0 * velocity) / (198.07 - 0.63 * launchAngle);
    }

    public double angleToServoPower(double angle) {
        return 0.024 * angle - 0.84;
    }

    public double calcHoodPosition(double dist) {
        if (dist >= 68) {
            return 0.1;
        }
        return 0.0;
    }

    public Vector getrobotToGoalVector(Pose currentPose) {
        return targetPose.getAsVector().minus(currentPose.getAsVector());
    }

    public Pose getTurretPose(Pose currentPose) {
        double turretDir = currentPose.getHeading() - Math.PI;
        if (turretDir < 0) turretDir = turretDir + 2 * Math.PI;
        Vector turretDiff = new Vector(turretDistFromCenter, turretDir);
        Vector currentVector = currentPose.getAsVector();
        Vector turretVector = currentVector.plus(turretDiff);
        return new Pose(turretVector.getXComponent(), turretVector.getYComponent(), currentPose.getHeading());
    }

    public Pose getTurretPose(Pose currentPose, double turretPos) {
        double turretDir = currentPose.getHeading() - Math.PI;
        if (turretDir < 0) turretDir = turretDir + 2 * Math.PI;
        Vector turretDiff = new Vector(turretDistFromCenter, turretDir);
        Vector currentVector = currentPose.getAsVector();
        Vector turretVector = currentVector.plus(turretDiff);
        return new Pose(turretVector.getXComponent(), turretVector.getYComponent(), currentPose.getHeading() + Math.toRadians(turretPos));
    }

    public void calcTurretPositions(Pose currentPose, Vector robotVel) {
        Pose turretPose = getTurretPose(currentPose);
        Vector robotToGoalVector = getrobotToGoalVector(getTurretPose(turretPose));

        /*double g = 32.174 * 12;

        double x;
        double y;
        double a;

        if (robotToGoalVector.getMagnitude() >= 100) {
            x = robotToGoalVector.getMagnitude() - farScoreRadius;
            y = farScoreHeight;
            a = farScoreAngleRad;
        } else {
            x = robotToGoalVector.getMagnitude() - closeScoreRadius;
            y = closeScoreHeight;
            a = closeScoreAngleRad;
        }

        hoodAngle = MathFunctions.clamp(Math.atan(2 * y / x - Math.tan(a)), Math.toRadians(30), Math.toRadians(55));

        flywheelSpeed = Math.sqrt(g * x * x / (2 * Math.pow(Math.cos(hoodAngle), 2) * (x * Math.tan(hoodAngle) - y)));

        double coordinateTheta = robotVel.getTheta() - robotToGoalVector.getTheta();

        double parallelComponent = -Math.cos(coordinateTheta) * robotVel.getMagnitude();
        double perpendicularComponent = Math.sin(coordinateTheta) * robotVel.getMagnitude();

        double vz = flywheelSpeed * Math.sin(hoodAngle);
        double time = x / (flywheelSpeed * Math.cos(hoodAngle));
        double ivr = x / time + parallelComponent;
        double nvr = Math.sqrt(ivr * ivr + perpendicularComponent * perpendicularComponent);
        double ndr = nvr * time;

        newHoodAngle = MathFunctions.clamp(Math.atan(vz / nvr), Math.toRadians(30), Math.toRadians(55));

        newFlywheelSpeed = Math.sqrt(g * ndr * ndr / (2 * Math.pow(Math.cos(hoodAngle), 2) * (ndr * Math.tan(hoodAngle) - y)));

        Shooter.INSTANCE.setHoodPos(angleToServoPower(90 - Math.toDegrees(newHoodAngle))).schedule();
        Shooter.INSTANCE.setGoal(calculateTicksPerSecond(newFlywheelSpeed, Math.toDegrees(newHoodAngle))).schedule();

        double turretVelCompOff = Math.atan(perpendicularComponent / ivr);*/

        Shooter.INSTANCE.setHoodPos(calcHoodPosition(robotToGoalVector.getMagnitude())).schedule();
        Shooter.INSTANCE.setGoal(Shooter.INSTANCE.calcShooterPower(robotToGoalVector.getMagnitude())).schedule();

        targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                Math.atan(Math.abs(targetPose.getY() - turretPose.getY()) / Math.abs(targetPose.getX() - turretPose.getX()));
        if (isRed) targetYaw = Math.toDegrees(targetYaw - turretPose.getHeading());
        else targetYaw = Math.toDegrees(Math.PI - targetYaw - turretPose.getHeading());

        //targetYaw = targetYaw - Math.toDegrees(turretVelCompOff);

        controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
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
        return Shooter.INSTANCE.setHoodPos(0);
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

    public Command autoControl(boolean doShooter) {
        return new InstantCommand(() -> mode = doShooter ? TurretMode.POSE_TRACKING : TurretMode.NO_SHOOTER);
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

    @Override
    public void periodic() {

        Pose currentPose = Selene.INSTANCE.currentPose;

        switch (mode) {
            case POSE_TRACKING:
                calcTurretPositions(currentPose, Selene.INSTANCE.follower.getVelocity());
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                break;
            case NO_SHOOTER:
                Pose turretPose = getTurretPose(currentPose);

                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.atan(Math.abs(targetPose.getY() - turretPose.getY()) / Math.abs(targetPose.getX() - turretPose.getX()));
                if (isRed) targetYaw = Math.toDegrees(targetYaw - turretPose.getHeading());
                else targetYaw = Math.toDegrees(Math.PI - targetYaw - turretPose.getHeading());

                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                break;
            case TESTING:
                targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                        Math.atan(Math.abs(targetPose.getY() - currentPose.getY()) / Math.abs(targetPose.getX() - currentPose.getX()));
                if (isRed) targetYaw = Math.toDegrees(targetYaw - currentPose.getHeading());
                else targetYaw = Math.toDegrees(Math.PI - targetYaw - currentPose.getHeading());

                robotVel = Selene.INSTANCE.follower.getVelocity();
                robotVel.setComponents(robotVel.getMagnitude(), robotVel.getTheta() - currentPose.getHeading() + Math.toRadians(targetYaw));

                controller.setGoal(new KineticState(degreesToTicks(Math.max(minLim, Math.min(maxLim, targetYaw)))));
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                break;
            case IDLE:
                rotationMotor.setPower(-controller.calculate(rotationMotor.getState()));
                break;
        }

    }
}
