package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;




import com.qualcomm.hardware.dfrobot.HuskyLens;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils.*;

public class Turret implements Subsystem {

    public static final Turret INSTANCE = new Turret();

    MotorEx rotationMotor;
    HuskyLens camera;
    ArtifactTypes[] motif;
    HuskyLens.Algorithm trackingMode;

    @Override
    public void initialize() {

        this.rotationMotor = RobotConfig.TurretRotation.motor;

        this.camera = RobotConfig.camera;

        RobotConfig.playerTwo.LeftX.greaterThan(0.01).or(() -> RobotConfig.playerTwo.LeftX.lessThan(-0.01).get())
                .whenTrue(() -> new SetPower(rotationMotor, Sensitivities.turretTurnSpeed * RobotConfig.playerTwo.LeftX.get()))
                .whenFalse(() -> new SetPower(rotationMotor, 0));
    }

    public void setTrackingMode(HuskyLens.Algorithm mode) {
        trackingMode = mode;
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
        else motif = new ArtifactTypes[] {
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.PURPLE,
                    ArtifactTypes.GREEN
            };
        camera.selectAlgorithm(trackingMode);
        return motif;
    }


}
