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
    boolean isSingleMotifShooting = false;
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

    /**
     * safely shoot a single artifact of a specified color, while also preventing an ArrayDeque error
     * @param color the color of artifact to shoot
     * @return a SequentialGroup that shoots a single artifact, or a NullCommand if the robot is already shooting
     */
    public Command shootSingle(Utils.ArtifactTypes color) {
        if (!isShooting) {
            this.isShooting = true;
            return new SequentialGroup(
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

    /**
     * used in shootMotif to shoot a single artifact depending on the motif
     * @param i the index of the slot
     * @return a SequentialGroup that shoots a single artifact, or a NullCommand if the robot is already shooting
     */
    public Command shootSingleMotif(int i) {
        if (!isSingleMotifShooting) {
            this.isSingleMotifShooting = true;
            return new SequentialGroup(
                    Magazine.INSTANCE.setDesiredColor(i),
                    new Delay(shootingSpeed),
                    Shooter.INSTANCE.shoot(),
                    new Delay(shootingSpeed),
                    Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE),
                    Magazine.INSTANCE.setDesiredColor(i + 1),
                    new InstantCommand(() -> this.isSingleMotifShooting = false)
            );
        } else {
            return new NullCommand();
        }
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

    /**
     * Shoots a motif
     * @return a SequentialGroup that shoots a motif, or a NullCommand if the robot is already shooting
     */
    public Command shootMotif() {
        if (!isMotifShooting) {
            this.isMotifShooting = true;
            return new SequentialGroup(
                    Shooter.INSTANCE.spinUp(),
                    new Delay(0.25),
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
