package org.firstinspires.ftc.teamcode.RobotStuff;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;

public class Perseus extends SubsystemGroup {

    public static final Perseus INSTANCE = new Perseus();

    public Follower follower;
    public Follower followerTeleOp;

    private Perseus() {
        super(
                Magazine.INSTANCE,
                Turret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    public void init(OpMode opmode) {
        follower = Constants.createFollower(opmode.hardwareMap);

    }

    public Command start() {
        return new NullCommand(); //Add stuff to do on start
    }

    public Command setMode(Magazine.MagazineMode mode) {

        Command command = new NullCommand();

        switch (mode) {
            case OUTTAKE:
                command  = new SequentialGroup(
                        Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE),
                        Shooter.INSTANCE.idle()
                );
                break;
            case INTAKE:
                command  = new SequentialGroup(
                        Magazine.INSTANCE.setMode(Magazine.MagazineMode.INTAKE),
                        Shooter.INSTANCE.spinDown()
                );
                break;
        }
        return command;
    }

    public Command shootMotif() {

        Command modeSwitch = new NullCommand();

        if (Magazine.INSTANCE.getMode() == Magazine.MagazineMode.INTAKE) {
            modeSwitch = new SequentialGroup(
                    Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE),
                    new Delay(0.5)
            );
        }

        return new SequentialGroup(
                Shooter.INSTANCE.spinUp(),
                modeSwitch,
                Shooter.INSTANCE.shoot(),
                Magazine.INSTANCE.incShotsFired(),
                new Delay(0.3),
                Shooter.INSTANCE.shoot(),
                Magazine.INSTANCE.incShotsFired(),
                new Delay(0.3),
                Shooter.INSTANCE.shoot(),
                Magazine.INSTANCE.incShotsFired(),
                Shooter.INSTANCE.idle()
        );
    }

    public Command shootSingle(Utils.ArtifactTypes color) {
        return new SequentialGroup(
                Magazine.INSTANCE.ColorShooting(color),
                Shooter.INSTANCE.spinUp(),
                new Delay(0.5),
                Shooter.INSTANCE.shoot(),
                Shooter.INSTANCE.idle()
        );
    }

    public Command intake() {

        Command command;

        if (Magazine.INSTANCE.getMode() == Magazine.MagazineMode.INTAKE) {
            command = Intake.INSTANCE.start;
        } else {
            command = new NullCommand();
        }

        return command;
    }

    public Command stopIntake() {
        return Intake.INSTANCE.idle;
    }

    public Command getMotif() {
        Magazine.INSTANCE.setMotif(Turret.INSTANCE.getMotif());
        return new NullCommand();
    }

    public void manualTurretPassAxis(double value) {
        Turret.INSTANCE.passAxis(value);
    }

    @Override
    public void periodic() {
        Magazine.INSTANCE.passTurretPos(Turret.INSTANCE.getTurretPos());
        Turret.INSTANCE.passRobotPose(followerTeleOp.getPose());
    }
}
