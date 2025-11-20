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
    double hoodTargetPos;
    Pair<Integer, Integer> waluigi;

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
            targetAngle = Math.max(-90, Math.min(90, pos));
            controller.setGoal(new KineticState(off + degreesToTicks(targetAngle)));
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
            targetAngle = targetAngle + pos;
            targetAngle = Math.max(-90, Math.min(90, targetAngle));
            controller.setGoal(new KineticState(off + degreesToTicks(targetAngle)));
        });
    }

    public Command autoControl() {
        return new InstantCommand(() -> mode = TurretMode.RECOVERY);
    }

    /**
     * tf dp you think it does
     * @param isRed do you really need me to explain this
     */
    public Command setRedAlliance(boolean isRed) {
        if (!this.hasSetAlliance) {
            this.isRedAlliance = isRed;
            this.hasSetAlliance = true;
        }

        return new NullCommand();
    }

    public Command setHoodPos(double pos) {
        return Shooter.INSTANCE.setHoodPos(pos);
    }

    public double degreesToTicks(double degrees) {
        return degrees * 751.8 / 360 * 8;
    }
    public double ticksToDegrees(double ticks) {return ticks / 751.8 * 360 / 8;}

    /**
     * Safely get the tag x and y position, return a dummy value if the tag is not found
     * @return a Pair containing the x and y positions, or dummy values
     */
    public Pair<Integer, Integer> waugh() { // yes, this is how we get the tag x and y position
        try {
            if (isRedAlliance) {
                HuskyLens.Block tag = camera.blocks(1)[0];
                return new Pair<>(tag.x, tag.y);
            } else {
                HuskyLens.Block tag = camera.blocks(2)[0];
                return new Pair<>(tag.x, tag.y);
            }
        } catch (RuntimeException reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee) {
            return new Pair<>(69420, 42069);
        }
    }

    public void manualUpdate() {
        rotationMotor.setPower(controller.calculate(rotationMotor.getState()));
    }

    public static double linearM = 0.006;
    public static double logA = 0.01;
    public static double logB = 1.25;
    public static double logH = 84;
    public static double logK = 0.5;
    public static double quadA = -0.0000464;
    public static double quadH = 172;
    public static double quadK = 0.686;

    /**
     * calculate the optimal hood servo position using a piecewise function
     * @param tagY the y position of the tag in the camera's coordinate system
     * @return the servo power
     */
    public double calcHoodPos(int tagY) {
        if (tagY < 0 || tagY > 240) {
            return 0.0; // this shouldn't be possible but it should be taken into account anyways
        }

        if (tagY < 85.30149) { // 0 < x < 85.30149
            return linearM * tagY; // f(x) = 0.006x
        } else if (85.30149 <= tagY && tagY <= 126) { // 85.30149 <= x <= 126
            return logA * (Math.log10(tagY - logH) / Math.log10(logB)) + logK; // f(x) = 0.01 * (log(tagY - 84) / log(1.25)) + 0.5
        } else { // 126 < x <= 240
            return quadA * ((tagY - quadH) * (tagY - quadH)) + quadK; // f(x) = -0.0000464(tagY - 172)^2 + 0.686
        }
    }


    @Override
    public void periodic() {
        waluigi = waugh();

        if (Magazine.INSTANCE.mode == 0 && !isZeroing) {
            mode = TurretMode.MANUAL_PID;
            targetAngle = 0;
        } else if (Magazine.INSTANCE.mode == 1 && mode == TurretMode.MANUAL_PID && !isZeroing) mode = TurretMode.RECOVERY;
        
        switch (mode) {
            case TAG_TRACKING:
                if (waluigi.component1() != 69420) {
                    targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition() - off) - (a * (waluigiWaugh - 160));
                    hoodTargetPos = calcHoodPos(waluigi.component2());
                    timer.resetTimer();
                } else if (timer.getElapsedTimeSeconds() >= 1) {
                    mode = TurretMode.RECOVERY;
                } else {
                    double deltaHeading = pose.getHeading() - oldPose.getHeading();
                    targetAngle = targetAngle - Math.toDegrees(deltaHeading);
                }
                controller.setGoal(new KineticState(off + degreesToTicks(Math.max(-90, Math.min(90, targetAngle)))));
                setHoodPos(hoodTargetPos).schedule();
                rotationMotor.setPower(Math.max(-0.75, Math.min(0.75, controller.calculate(rotationMotor.getState()))));
                break;
            case RECOVERY:
                //if (isSweeping) {
                    if (waluigiWaugh != 69420) {
                        targetAngle = ticksToDegrees(rotationMotor.getCurrentPosition()) - (a * (waluigiWaugh - 160));
                        hoodTargetPos = calcHoodPos(waluigi.component2());
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
                hasGotMotif = true;
            } else if (camera.blocks(4).length != 0) {
                Magazine.INSTANCE.motif = PGPPGP;
                hasGotMotif = true;
            } else if (camera.blocks(5).length != 0) {
                Magazine.INSTANCE.motif = PPGPPG;
                hasGotMotif = true;
            }
        }
    }
}
