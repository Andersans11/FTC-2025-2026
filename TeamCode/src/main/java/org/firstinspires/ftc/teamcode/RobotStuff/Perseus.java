package org.firstinspires.ftc.teamcode.RobotStuff;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import java.util.function.Function;
import java.util.function.Supplier;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;

public class Perseus extends BetterSubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public Follower followerTeleOp;
    public Supplier<Command> shootMotif;
    public Function<Magazine.MagazineMode, Command> setMode;
    public Function<Utils.ArtifactTypes, Command> shootSingle;
    public Supplier<Command> intake;
    public Supplier<Command> stopIntake;
    public Supplier<Command> getMotif;



    private Perseus() {
        super(
                Magazine.INSTANCE,
                Turret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void hardware() {
        super.hardware();

        follower = Constants.createFollower(RobotConfig.getHardwareMap());
        followerTeleOp = Constants.createFollower(RobotConfig.getHardwareMap());
    }

    @Override
    public void commands() {
        super.commands();

        this.shootMotif = () -> {
            Command modeSwitch = new NullCommand();

            if (Magazine.INSTANCE.getMode() == Magazine.MagazineMode.INTAKE) {
                modeSwitch = new SequentialGroup(
                        Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE),
                        new Delay(0.5)
                );
            }

            return new SequentialGroup(
                    Shooter.INSTANCE.spinUp,
                    modeSwitch,
                    Shooter.INSTANCE.shoot.get(),
                    Magazine.INSTANCE.incShotsFired(),
                    new Delay(0.3),
                    Shooter.INSTANCE.shoot.get(),
                    Magazine.INSTANCE.incShotsFired(),
                    new Delay(0.3),
                    Shooter.INSTANCE.shoot.get(),
                    Magazine.INSTANCE.incShotsFired(),
                    Shooter.INSTANCE.idle
            );
        };

        this.setMode = (mode) -> {

            Command command = new NullCommand();

            switch (mode) {
                case OUTTAKE:
                    command  = new SequentialGroup(
                            Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE),
                            Shooter.INSTANCE.idle
                    );
                    break;
                case INTAKE:
                    command  = new SequentialGroup(
                            Magazine.INSTANCE.setMode(Magazine.MagazineMode.INTAKE),
                            Shooter.INSTANCE.spinDown
                    );
                    break;
            }
            return command;
        };

        this.shootSingle = (color) -> new SequentialGroup(
                Magazine.INSTANCE.ColorShooting(color),
                Shooter.INSTANCE.spinUp,
                new Delay(0.5),
                Shooter.INSTANCE.shoot.get(),
                Shooter.INSTANCE.idle
        );

        this.intake = () -> {
            Command command;

            if (Magazine.INSTANCE.getMode() == Magazine.MagazineMode.INTAKE) {
                command = Intake.INSTANCE.start;
            } else {
                command = new NullCommand();
            }

            return command;
        };

        this.stopIntake = () -> Intake.INSTANCE.idle;

        this.getMotif = () -> {
            Magazine.INSTANCE.setMotif(Turret.INSTANCE.getMotif());
            return new NullCommand();
        };
    }

    @Override
    public void binds() {
        // don't do other binds because the commands would be used here
        RobotConfig.ButtonControls.SHOOT_GREEN.whenBecomesTrue(this.shootSingle.apply(Utils.ArtifactTypes.GREEN));
        RobotConfig.ButtonControls.SHOOT_PURPLE.whenBecomesTrue(this.shootSingle.apply(Utils.ArtifactTypes.PURPLE));

        RobotConfig.ButtonControls.INTAKE.whenBecomesTrue(this.intake.get());
        RobotConfig.ButtonControls.INTAKE.whenBecomesFalse(this.stopIntake.get());

        RobotConfig.ButtonControls.SHOOT_MOTIF.whenBecomesTrue(this.shootMotif.get());

        Turret.INSTANCE.rotationSupp = RobotConfig.RangeControls.TURRET_ROT;
    }

    public Command start() {
        return new NullCommand(); //Add stuff to do on start
    }

    public void setManualControl(boolean isManual) {
        Turret.INSTANCE.setManualControl(isManual);
    }

    @Override
    public void periodic() {
        super.periodic();
        Magazine.INSTANCE.passTurretPos(Turret.INSTANCE.getTurretPos());
        Turret.INSTANCE.passRobotPose(followerTeleOp.getPose());
    }
}
