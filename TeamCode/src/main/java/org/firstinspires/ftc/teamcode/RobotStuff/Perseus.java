package org.firstinspires.ftc.teamcode.RobotStuff;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.PoseTracker;

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

@Configurable
public class Perseus extends BetterSubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public PoseTracker followerTeleOp;

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
    }

    @Override
    public void preStart() {
        super.preStart();
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    //  ------------------------- COMMANDS --------------------------- //

    public Command shootSingle(Utils.ArtifactTypes color) {
        return new SequentialGroup(
                NewMagazine.INSTANCE.setDesiredColor(color),
                Shooter.INSTANCE.spinUp(),
                new Delay(0.5),
                Shooter.INSTANCE.shoot()
        );
    }

    public Command intake() {
        return new SequentialGroup(
                NewMagazine.INSTANCE.setMode(0),
                Intake.INSTANCE.start()
        );
    }

    public Command stopIntake() {
        return Intake.INSTANCE.idle();
    }

    public Command getMotif() {
        // TODO: Add motif passing when camera code is done
        return new NullCommand();
    }

    public Command shootMotif() {
        Command modeSwitch = new NullCommand();

        if (NewMagazine.INSTANCE.mode == 0) {
            modeSwitch = new SequentialGroup(
                    NewMagazine.INSTANCE.setMode(1),
                    NewMagazine.INSTANCE.setDesiredColor(),
                    new Delay(0.5)
            );
        }

        return new SequentialGroup(
                Shooter.INSTANCE.spinUp(),
                modeSwitch,
                Shooter.INSTANCE.shoot(),
                NewMagazine.INSTANCE.incShotsFired(),
                NewMagazine.INSTANCE.setDesiredColor(),
                new Delay(0.3),
                Shooter.INSTANCE.shoot(),
                NewMagazine.INSTANCE.incShotsFired(),
                NewMagazine.INSTANCE.setDesiredColor(),
                new Delay(0.3),
                Shooter.INSTANCE.shoot(),
                NewMagazine.INSTANCE.incShotsFired(),
                Shooter.INSTANCE.idle()
        );
    }

    // ---------------------- METHODS ------------------------------ //


}
