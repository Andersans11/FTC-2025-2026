package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.qualcomm.hardware.dfrobot.HuskyLens;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils.*;

public class Turret implements IBetterSubsystem {

    public static final Turret INSTANCE = new Turret();

    MotorEx rotationMotor;
    boolean isRedAlliance;
    HuskyLens camera;
    ArtifactTypes[] motif;
    HuskyLens.Algorithm trackingMode;
    double targetPos;
    double oldTargetPos;
    double pitch;

    final double lensCenterFromFloor = 279.87241113;
    final double lensAngleFromVertical = 20.0; // deg

    @Override
    public void initialize() {}

    @Override
    public void hardware() {
        this.rotationMotor = RobotConfig.TurretRotation.motor;
        this.camera = RobotConfig.camera;
    }

    @Override
    public void commands() {

    }

    public void setTrackingMode(HuskyLens.Algorithm mode) {
        trackingMode = mode;
    }

    public void setRedAlliance() {
        isRedAlliance = true;
    }

    public ArtifactTypes[] getMotif() {
        camera.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        if (camera.blocks(2).length != 0)
            motif = new ArtifactTypes[] {
                    ArtifactTypes.GREEN,
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.PURPLE
            };
        else if (camera.blocks(3).length != 0) motif = new ArtifactTypes[] {
                ArtifactTypes.PURPLE,
                ArtifactTypes.GREEN,
                ArtifactTypes.PURPLE
            };
        else if (camera.blocks(4).length != 0) motif = new ArtifactTypes[] {
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.GREEN
            };
        camera.selectAlgorithm(trackingMode);
        return motif;
    }

    public double getServoPitch() {
        if ((isRedAlliance && camera.blocks(5).length != 0) ||
                (!isRedAlliance && camera.blocks(6).length != 0)) {
            int tagY = camera.blocks(5)[0].y;

            return (double) 240 / tagY; // if tag is at max height, is 1
        }
        return 0;
    }


    public Command update() {
        
        if ((isRedAlliance && camera.blocks(5).length != 0) ||
                (!isRedAlliance && camera.blocks(6).length != 0)) {
            int tagX = camera.blocks(5)[0].x;

            if (tagX < 158 || tagX > 162) {
                double power = (double) (160 - tagX) / 80;
                return new SetPower(rotationMotor, power);
            } else {
                return new SetPower(rotationMotor, 0);
            }
        } else {
            return new NullCommand(); // Put PID Tracking here; this is for when the camera can't see the tag
        }

    } // TODO: IMPLEMENT PID TRACKING FOR PITCH AND YAW ON TURRET

    @Override
    public void binds() {

    }

    @Override
    public void periodic() {
        update().invoke();
    }
}
