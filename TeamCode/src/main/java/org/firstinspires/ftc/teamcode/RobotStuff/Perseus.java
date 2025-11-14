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

@Configurable
public class Perseus extends BetterSubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public PoseTracker followerTeleOp;

    private Perseus() {
        super(
                Magazine.INSTANCE,
                Turret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    public Utils.ArtifactTypes[] motif = new Utils.ArtifactTypes[] {
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.PURPLE,
            Utils.ArtifactTypes.GREEN
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

    //  ------------------------- COMMANDS --------------------------- //

    public Command shootSingle(Utils.ArtifactTypes color) {
        return new SequentialGroup(
                Magazine.INSTANCE.setDesiredColor(color),
                Shooter.INSTANCE.spinUp(),
                new Delay(0.25),
                Shooter.INSTANCE.shoot(),
                Magazine.INSTANCE.setActiveSlotContent(Utils.ArtifactTypes.NONE),
                Shooter.INSTANCE.idle()
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

        return new SequentialGroup(
                Shooter.INSTANCE.spinUp(),
                shootSingle(motif[0]),
                new Delay(0.3),
                shootSingle(motif[1]),
                new Delay(0.3),
                shootSingle(motif[2]),
                Shooter.INSTANCE.idle()
        );
    }

    public Command start() {
        return new SequentialGroup(
                Magazine.INSTANCE.setMode(1),
                new Delay(0.25),
                Intake.INSTANCE.idle()
        );
    }
    // ---------------------- METHODS ------------------------------ //


}
