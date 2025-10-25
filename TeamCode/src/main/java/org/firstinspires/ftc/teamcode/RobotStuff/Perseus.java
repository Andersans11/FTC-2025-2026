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

    @Override
    public void initialize() {
        follower = Constants.createFollower(RobotConfig.getHardwareMap());
        followerTeleOp = Constants.createFollower(RobotConfig.getHardwareMap());
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

        Command shoot = new SequentialGroup(
                        Shooter.INSTANCE.shoot(),
                        Magazine.INSTANCE.incShotsFired(),
                        new Delay(0.3),
                        Shooter.INSTANCE.shoot(),
                        Magazine.INSTANCE.incShotsFired(),
                        new Delay(0.3),
                        Shooter.INSTANCE.shoot(),
                        Magazine.INSTANCE.incShotsFired(),
                        new Delay(0.3)
                );
        return new SequentialGroup(
                modeSwitch,
                shoot
        );
    }

    public Command shootSingle(int slot) {
        return new SequentialGroup(
                Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE),
                Magazine.INSTANCE.setActiveSlot(slot),
                new Delay(0.5),
                Shooter.INSTANCE.shoot(),
                Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE)
        );
    }

    public Command intake() {

        Command command = Intake.INSTANCE.start();

        if (Magazine.INSTANCE.getMode() != Magazine.MagazineMode.INTAKE) {
            command = new SequentialGroup(
                    Magazine.INSTANCE.setMode(Magazine.MagazineMode.INTAKE).requires(Magazine.INSTANCE),
                    new Delay(0.5),
                    Intake.INSTANCE.start().requires(Intake.INSTANCE)
            );
        }

        return command;
    }

    public Command stopIntake() {
        return Intake.INSTANCE.idle();
    }

    public Command getMotif() {
        Magazine.INSTANCE.setMotif(Turret.INSTANCE.getMotif());
        return new NullCommand();
    }
}
