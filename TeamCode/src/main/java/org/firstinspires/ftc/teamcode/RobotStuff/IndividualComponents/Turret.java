package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;




import com.qualcomm.hardware.dfrobot.HuskyLens;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.powerable.SetPower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils.*;

public class Turret implements Subsystem {

    NextFTCOpMode opMode;
    MotorEx rotationMotor;
    HuskyLens camera;
    HuskyLens.Block[] detections;
    ArtifactTypes[] motif;


    public void init(NextFTCOpMode opMode, boolean eddieTracking) {
        this.opMode = opMode;

        this.rotationMotor = RobotConfig.TurretRotation.motor;

        this.camera = RobotConfig.camera;

        if (eddieTracking) camera.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        else camera.selectAlgorithm(HuskyLens.Algorithm.FACE_RECOGNITION);

        RobotConfig.playerTwo.LeftX.greaterThan(0.01).or(() -> RobotConfig.playerTwo.LeftX.lessThan(-0.01).get())
                .whenTrue(() -> new SetPower(rotationMotor, Sensitivities.turretTurnSpeed * RobotConfig.playerTwo.LeftX.get()))
                .whenFalse(() -> new SetPower(rotationMotor, 0));
    }

    public ArtifactTypes[] getMotif() {
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
        return motif;
    }


}
