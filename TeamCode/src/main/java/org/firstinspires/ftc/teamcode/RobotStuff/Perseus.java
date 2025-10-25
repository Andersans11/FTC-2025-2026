package org.firstinspires.ftc.teamcode.RobotStuff;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.NewMagazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;

public class Perseus extends BetterSubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public Follower followerTeleOp;

    private Perseus() {
        super(
                NewMagazine.INSTANCE,
                NewTurret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void initSystem() {
        super.initSystem();

        follower = Constants.createFollower(RobotConfig.getHardwareMap());
        followerTeleOp = Constants.createFollower(RobotConfig.getHardwareMap());
    }

    @Override
    public void preStart() {
        super.preStart();
    }

    @Override
    public void periodic() {
        super.periodic();
        NewMagazine.INSTANCE.passTurretPos(NewTurret.INSTANCE.getTurretPos());
        NewTurret.INSTANCE.passRobotPose(followerTeleOp.getPose());
    }

    //  ------------------------- COMMANDS --------------------------- //

    public Command shootSingle(Utils.ArtifactTypes color) {
        return new SequentialGroup(
                NewMagazine.INSTANCE.colorShooting(color),
                Shooter.INSTANCE.spinUp(),
                new Delay(0.5),
                Shooter.INSTANCE.shoot()
        );
    }

    public Command intake() {
        return new SequentialGroup(
                NewMagazine.INSTANCE.setMode(true),
                Intake.INSTANCE.start()
        );
    }

    public Command stopIntake() {
        return Intake.INSTANCE.idle();
    }

    public Command getMotif() {
        NewMagazine.INSTANCE.setMotif(NewTurret.INSTANCE.getMotif());
        return new NullCommand();
    }

    public Command shootMotif() {
        Command modeSwitch = new NullCommand();

        if (NewMagazine.INSTANCE.yej) {
            modeSwitch = new SequentialGroup(
                    NewMagazine.INSTANCE.setMode(false),
                    new Delay(0.5)
            );
        }

        return new SequentialGroup(
                Shooter.INSTANCE.spinUp(),
                modeSwitch,
                Shooter.INSTANCE.shoot(),
                NewMagazine.INSTANCE.incShotsFired(),
                new Delay(0.3),
                Shooter.INSTANCE.shoot(),
                NewMagazine.INSTANCE.incShotsFired(),
                new Delay(0.3),
                Shooter.INSTANCE.shoot(),
                NewMagazine.INSTANCE.incShotsFired(),
                Shooter.INSTANCE.idle()
        );
    }

    // ---------------------- METHODS ------------------------------ //
    public void setManualControlTurret(boolean isManual) {
        NewTurret.INSTANCE.setManualControl(isManual);
    }


}
