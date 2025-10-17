package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;

public class TestMagazine extends NextFTCOpMode {
    long deltaTime;
    DeltaTimer deltaTimer;
    RobotCentricDrive robotCentricDrive;

    public TestMagazine() {
        addComponents(
                new SubsystemComponent(Magazine.INSTANCE),
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        RobotConfig.initConfig(this);
        robotCentricDrive = new RobotCentricDrive(this);
    }

    @Override
    public void onStartButtonPressed() {
        Magazine.INSTANCE.setMotif(new Utils.ArtifactTypes[]{
                Utils.ArtifactTypes.PURPLE,
                Utils.ArtifactTypes.GREEN,
                Utils.ArtifactTypes.PURPLE,
        });
        Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE_MOTIF);
        robotCentricDrive.schedule();
    }

    @Override
    public void onUpdate() {
        deltaTime = deltaTimer.getDelta();

        robotCentricDrive.update(deltaTime);
    }
}
