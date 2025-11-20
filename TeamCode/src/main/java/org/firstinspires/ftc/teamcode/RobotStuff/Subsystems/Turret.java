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
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;
import kotlin.Pair;
import dev.nextftc.hardware.powerable.SetPower;

@Configurable
public class Turret implements IAmBetterSubsystem {

    public static final Turret INSTANCE = new Turret();

    public MotorEx rotationMotor;
    public HuskyLens camera;
    public TouchSensor limitSwitch;
    public boolean isRedAlliance = true;
    boolean hasSetAlliance = false;
    boolean hasGotMotif = false;
    double pitch;
    Pose pose = new Pose(0, 0, 0);
    Pose oldPose = new Pose(0, 0, 0);
    public double targetAngle = 0;
    boolean isManualControl = false;
    boolean isSweeping = true;
    boolean started = false;
    public Timer timer;
    public ControlSystem controller;
    public double tagPos;
    public enum TurretMode {
        TAG_TRACKING, // Using AprilTags
        RECOVERY, // Either position-based or sweep-based
        MANUAL_PID,
        MANUAL_POWER
    }

    double waluigiWaugh;

    boolean isZeroing = false;

    public double off = 0;

    public TurretMode mode = TurretMode.TAG_TRACKING; //ty waz here ><> <--- fish

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
    public static double kP = 0.005;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double a = 0.04;

    // --------------------- OPMODE --------------------------------- //


    @Override
    public void initialize() {
        controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build();

        timer = new Timer();
        targetAngle = 0;
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
        this.rotationMotor = RobotConfig.TurretRotation.motor;
        camera = new HuskyLens(RobotConfig.camera.getDeviceClient());
        camera.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        limitSwitch = RobotConfig.LimitSwitch;
    }

    @Override
    public void preStart() {

    }

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

    public Command resetPID() {
        return new InstantCommand(() -> controller = ControlSystem.builder()
                .posPid(kP, kI, kD)
                .build()
        );
    }

    public Command setPosition(double pos) {
        return new InstantCommand(() -> {
            mode = TurretMode.MANUAL_PID;
            targetAngle = Math.max(-90, Math.min(90, pos));
            controller.setGoal(new KineticState(off + degreesToTicks(targetAngle)));
        });
    }

    public Command changePosition(double pos) {
        return new InstantCommand(() -> {
            mode = TurretMode.MANUAL_PID;
            targetAngle = targetAngle + pos;
            targetAngle = Math.max(-90, Math.min(90, targetAngle));
            controller.setGoal(new KineticState(off + degreesToTicks(targetAngle)));
        });
    }

    public Command autoControl() {
        return new InstantCommand(() -> mode = TurretMode.TAG_TRACKING);
    }

    public Command setRedAlliance(boolean isRed) {
        if (!this.hasSetAlliance) {
            this.isRedAlliance = isRed;
            this.hasSetAlliance = true;
        }

        return new NullCommand();
    }

    public double degreesToTicks(double degrees) {
        return degrees * 751.8 / 360 * 8;
    }
    public double ticksToDegrees(double ticks) {return ticks / 751.8 * 360 / 8;}

    public double waugh() { // yes, this is how we get the tag x position
        try {
            if (isRedAlliance) {
                return camera.blocks(1)[0].x;
            } else {
                return camera.blocks(2)[0].x;
            }
        } catch (RuntimeException reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee) {
            return 69420;
        }
    }

    public void manualUpdate() {
        rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
    }


    @Override
    public void periodic() {
        waluigiWaugh = waugh();

        if (Magazine.INSTANCE.mode == 0 && !isZeroing) {
            mode = TurretMode.MANUAL_PID;
            targetAngle = 0;
        } else if (Magazine.INSTANCE.mode == 1 && mode == TurretMode.MANUAL_PID && !isZeroing) mode = TurretMode.RECOVERY;
        
        switch (mode) {
            case TAG_TRACKING:
                if (waluigiWaugh != 69420) {
                    targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition() - off) - (a * (waluigiWaugh - 160));
                    timer.resetTimer();
                } else if (timer.getElapsedTimeSeconds() >= 1) {
                    mode = TurretMode.RECOVERY;
                } else {
                    double deltaHeading = pose.getHeading() - oldPose.getHeading();
                    targetAngle = targetAngle - Math.toDegrees(deltaHeading);
                }
                controller.setGoal(new KineticState(off + degreesToTicks(Math.max(-90, Math.min(90, targetAngle)))));
                rotationMotor.setPower(Math.max(-0.75, Math.min(0.75, controller.calculate(rotationMotor.getState()))));
                break;
            case RECOVERY:
                //if (isSweeping) {
                    if (waluigiWaugh != 69420) {
                        targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) - (a * (waluigiWaugh - 160));
                        timer.resetTimer();
                        mode = TurretMode.TAG_TRACKING;
                        started = false;
                    } else {
                        if (!started) {
                            targetAngle = 0;
                            started = true;
                        }
                    }
                /*} else {
                    if (pose != oldPose) {
                        if (!isRedAlliance) {
                            targetAngle = Math.toDegrees(Math.atan((144 - pose.getY()) / pose.getX())); // get angle with right triangle rules
                            targetAngle = 180 - targetAngle; // get supplement of angle
                            targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                            if (targetAngle < 0) {
                                targetAngle = targetAngle + 360; // normalize angle
                            }
                            pitch = (pose.distanceFrom(new Pose(144, 0)) / 144) - 1.25;
                        } else {
                            targetAngle = Math.toDegrees(Math.atan((144 - pose.getY()) / (144 - pose.getX()))); // get angle with right triangle rules
                            targetAngle = targetAngle - pose.getHeading(); // account for robot heading
                            if (targetAngle < 0) {
                                targetAngle = targetAngle + 360; // normalize angle
                            }
                            pitch = (pose.distanceFrom(new Pose(144, 144)) / 144) - 1.25;
                        }
                    }
                }*/
                controller.setGoal(new KineticState(off + degreesToTicks(Math.max(-90, Math.min(90, targetAngle)))));
                rotationMotor.setPower(Math.max(-0.75, Math.min(0.75, controller.calculate(rotationMotor.getState()))));
                break;
            case MANUAL_PID:
                rotationMotor.setPower(Math.max(-0.75, Math.min(0.75, controller.calculate(rotationMotor.getState()))));
                break;
        }

        poseUpdater.update();
        oldPose = pose;
        pose = poseUpdater.getPose();

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
}
