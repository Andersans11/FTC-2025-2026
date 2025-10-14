package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Basic Mecanum Drive")
public class BasicMecanumDrive extends NextFTCOpMode {

    long deltaTime;
    DeltaTimer deltaTimer;
    RobotCentricDrive robotCentricDrive;

    public BasicMecanumDrive() {
        addComponents(
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
        robotCentricDrive.schedule();
    }

    @Override
    public void onUpdate() {
        deltaTime = deltaTimer.getDelta();

        robotCentricDrive.update(deltaTime);
    }
}
