package org.firstinspires.ftc.teamcode.RobotStuff;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.PoseTracker;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.NullCommand;

@Configurable
public class Perseus extends BetterSubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public PoseTracker followerTeleOp;

    boolean isShooting = false;
    boolean isMotifShooting = false;
    public static double hoodToPos = 0.5;

    private Perseus() {
        super(
                Magazine.INSTANCE,
                Turret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    public Utils.ArtifactTypes[] PPG = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN
    };
    public Utils.ArtifactTypes[] GPP = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE
    };
    public Utils.ArtifactTypes[] PGP = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN,
            Utils.ArtifactTypes.PURPLE
    };

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

    public static double motifShootingSpeed = 0.2;
    public static double shootingSpeed = 0.1;

    //  ------------------------- COMMANDS --------------------------- //

    public Command shootSingle(Utils.ArtifactTypes color) {
        if (!isShooting) {
            return new SequentialGroup(
                    new InstantCommand(() -> this.isShooting = true),
                    Magazine.INSTANCE.setDesiredColor(color),
                    Shooter.INSTANCE.spinUp(),
                    new Delay(shootingSpeed),
                    Shooter.INSTANCE.shoot(),
                    Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE),
                    Shooter.INSTANCE.idle(),
                    new InstantCommand(() -> this.isShooting = false)
            );
        } else {
            return new NullCommand();
        }
    }

    public Command updateHoodPos() {
        return Shooter.INSTANCE.setHoodPos(hoodToPos);
    }

    public Command shootSingleMotif(int i) {
        return new SequentialGroup(
                Magazine.INSTANCE.setDesiredColor(i),
                new Delay(shootingSpeed),
                Shooter.INSTANCE.shoot(),
                new Delay(shootingSpeed),
                Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE),
                Magazine.INSTANCE.setDesiredColor(i + 1)
        );
    }

    public Command intake() {
        return new SequentialGroup(
                Magazine.INSTANCE.setMode(0),
                Intake.INSTANCE.start()
        );
    }

    public Command outtake() {
        return Intake.INSTANCE.reverse();
    }

    public Command stopIntake() {
        return Intake.INSTANCE.idle();
    }

    public Command shootMotif() {
        if (!isMotifShooting) {
            return new SequentialGroup(
                    new InstantCommand(() -> this.isMotifShooting = true),
                    Shooter.INSTANCE.spinUp(),
                    shootSingleMotif(0),
                    new Delay(motifShootingSpeed),
                    shootSingleMotif(1),
                    new Delay(motifShootingSpeed),
                    shootSingleMotif(2),
                    Shooter.INSTANCE.idle(),
                    new InstantCommand(() -> this.isMotifShooting = false)
            );
        } else {
            return new NullCommand();
        }
    }

    public Command start() {
        return new SequentialGroup(
                Intake.INSTANCE.idle(),
                Shooter.INSTANCE.resetKicker()
        );
    }
}
