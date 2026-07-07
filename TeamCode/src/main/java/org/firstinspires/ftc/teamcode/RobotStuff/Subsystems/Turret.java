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

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Hardware.ServoExFullRange;
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

    public ServoExFullRange[] servos;
    public VoltageSensor voltageSensor;
    public Vector robotToGoalVector = new Vector(0, 0);
    public boolean isRed = true;
    public boolean hasSetAlliance = false;
    public boolean hasGotMotif = false;
    public boolean isLookingForMotif = false;
    Pose redPose = new Pose(184, 184);
    Pose bluePose = new Pose(8, 184);
    Pose redPrism = new Pose(104, 184);
    Pose bluePrism = new Pose(88, 184);
    public Pose targetPose = new Pose(184, 184);
    Pose motifPose = new Pose(144, 72);
    public double targetYaw = 0;
    public double targetPitch = 0.0;
    public static double minLim = -135;
    public static double maxLim = 125;
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
    public double robotHeading = 0;

    // ------------------------- CONFIG ------------------------------- //

    public static double scoreHeight = 40;
    public static double scoreAngle = -30;
    public double scoreAngleRad = Math.toRadians(scoreAngle);
    public static double scoreRadius = 5;
    public static double kP = 0.0005;
    public static double kI = 0.0;
    public static double kD = 0.00002;
    public int hoodToPos = 0;
    public boolean isFar;

    public double oldTurretPos = 361;

    public double hoodAngle = 0;
    public double flywheelSpeed = 0;
    public double newHoodAngle = 0;
    public double newFlywheelSpeed = 0;

    public double turretDistFromCenter = 1.72149606;

    public static boolean runVComp = false;
    public static double vP = 0.25;
    public static double hP = 0.0004;

    public static double turretOff = 10;

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
        this.servos = new ServoExFullRange[] {
                RobotConfig.TurretServo1.getServo(),
                RobotConfig.TurretServo2.getServo()
        };
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
                ((y <= -x + 102) && (y <= x - 42))) &&
                !isAtLimit();
    }

    public boolean isAtLimit() {
        return ticksToDegrees(controller.getGoal().getPosition()) == maxLim || ticksToDegrees(controller.getGoal().getPosition()) == minLim;
    }

    public double calculateTicksPerSecond(double velocity) {
        return (velocity - 40.63507) / 0.210041;
    }

    public double angleToServoPower(double angle) {
        angle = 9.25 * (62.5 - angle);
        return 1 - (angle / 355);
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

    public void switchTargets() {
        switch ((int) targetPose.getX()) {
            case 8:
                targetPose = bluePrism;
                break;
            case 88:
                targetPose = bluePose;
                break;
            case 184:
                targetPose = redPrism;
                break;
            case 104:
                targetPose = redPose;
                break;
        }
    }

    public void calcTurretPositions(Pose currentPose, Vector robotVel) {
        Pose turretPose = getTurretPose(currentPose);
        robotToGoalVector = getrobotToGoalVector(getTurretPose(turretPose));

        Shooter.INSTANCE.setHoodPos(angleToServoPower(Shooter.INSTANCE.calcHoodPower(robotToGoalVector.getMagnitude()))).schedule();
        Shooter.INSTANCE.setGoal(Shooter.INSTANCE.calcShooterPower(robotToGoalVector.getMagnitude())).schedule();

        targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                Math.atan(Math.abs(targetPose.getY() - currentPose.getY()) / Math.abs(targetPose.getX() - currentPose.getX()));
        if (isRed) targetYaw = Math.toDegrees(targetYaw - currentPose.getHeading());
        else targetYaw = Math.toDegrees(Math.PI - targetYaw - currentPose.getHeading());

        setTurretPos(targetYaw + turretOff);
    }

    public void calcTurretPositionsEq(Pose currentPose, Vector robotVel) {
        Pose turretPose = getTurretPose(currentPose);
        robotToGoalVector = getrobotToGoalVector(getTurretPose(turretPose));

        double g = 32.174 * 12;
        double x = robotToGoalVector.getMagnitude() - scoreRadius;
        double y = scoreHeight;
        double a = scoreAngleRad;

        hoodAngle = MathFunctions.clamp(Math.atan(2 * y / x - Math.tan(a)), Math.toRadians(40), Math.toRadians(62.5));

        flywheelSpeed = Math.sqrt(g * x * x / (2 * Math.pow(Math.cos(hoodAngle), 2) * (x * Math.tan(hoodAngle) - y)));

        double coordinateTheta = robotVel.getTheta() - robotToGoalVector.getTheta();

        double parallelComponent = -Math.cos(coordinateTheta) * robotVel.getMagnitude();
        double perpendicularComponent = Math.sin(coordinateTheta) * robotVel.getMagnitude();

        double vz = flywheelSpeed * Math.sin(hoodAngle);
        double time = x / (flywheelSpeed * Math.cos(hoodAngle));
        double ivr = x / time + parallelComponent;
        double nvr = Math.sqrt(ivr * ivr + perpendicularComponent * perpendicularComponent);
        double ndr = nvr * time;

        newHoodAngle = MathFunctions.clamp(Math.atan(vz / nvr), Math.toRadians(40), Math.toRadians(62.5));

        newFlywheelSpeed = Math.sqrt(g * ndr * ndr / (2 * Math.pow(Math.cos(hoodAngle), 2) * (ndr * Math.tan(hoodAngle) - y)));

        Shooter.INSTANCE.setHoodPos(angleToServoPower(Math.toDegrees(newHoodAngle))).schedule();
        Shooter.INSTANCE.setGoal(calculateTicksPerSecond(newFlywheelSpeed)).schedule();

        double turretVelCompOff = Math.atan(perpendicularComponent / ivr);

        targetYaw = // get yaw angle using trig, targetYaw = arctan(opposite/adjacent)
                Math.atan(Math.abs(targetPose.getY() - currentPose.getY()) / Math.abs(targetPose.getX() - currentPose.getX()));
        if (isRed) targetYaw = Math.toDegrees(targetYaw - currentPose.getHeading());
        else targetYaw = Math.toDegrees(Math.PI - targetYaw - currentPose.getHeading());

        targetYaw = targetYaw - Math.toDegrees(turretVelCompOff);

        setTurretPos(targetYaw);
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
        return Shooter.INSTANCE.setHoodPos(1);
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

    public Command setHoodPosition(double hoodPos) {
        return new InstantCommand(() -> {
            mode = TurretMode.IDLE;
            hoodAngle = hoodPos;
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

    public void setTurretPos(double pos) {
        if (pos != oldTurretPos) {
            servos[0].setPosition(degreesToTicks(Math.max(minLim, Math.min(maxLim, pos))));
            servos[1].setPosition(degreesToTicks(Math.max(minLim, Math.min(maxLim, pos))) + 0.01);
            oldTurretPos = pos;
        }
    }

    public double degreesToTicks(double degrees) {
        return (degrees / 19 * 7 / 15 * 50 / 355) + 0.5;
    }

    public double ticksToDegrees(double ticks) {
        return (ticks - 0.5) * 19 / 7 * 15 / 50 * 355;
    }

    @Override
    public void periodic() {

        Pose currentPose = Selene.currentPose;

        switch (mode) {
            case POSE_TRACKING:
                calcTurretPositions(currentPose, Selene.INSTANCE.follower.getVelocity());
                break;
            case NO_SHOOTER:
                Pose turretPose = getTurretPose(currentPose);

                double coordinateTheta = Selene.INSTANCE.follower.getVelocity().getTheta() - robotToGoalVector.getTheta();
                double perpendicularComponent = Math.sin(coordinateTheta) * robotVel.getMagnitude();

                robotHeading = Math.toDegrees(turretPose.getHeading());
                if (robotHeading < 0) robotHeading = robotHeading + 360;

                if (isRed) targetYaw = Math.toDegrees(robotToGoalVector.getTheta() - Math.toRadians(robotHeading));
                else targetYaw = Math.toDegrees(robotToGoalVector.getTheta() - Math.toRadians(robotHeading));

                if (runVComp) targetYaw = targetYaw - perpendicularComponent * vP;

                setTurretPos(targetYaw);
                break;
            case TESTING:
                setTurretPos(targetYaw);
                break;
            case IDLE:
                double hoodPower = hoodAngle - (Shooter.INSTANCE.controller.getGoal().getVelocity() - Shooter.INSTANCE.shooters.getVelocity()) * hP;
                Shooter.INSTANCE.setHoodPos(hoodPower).schedule();
                break;
        }

    }
}
