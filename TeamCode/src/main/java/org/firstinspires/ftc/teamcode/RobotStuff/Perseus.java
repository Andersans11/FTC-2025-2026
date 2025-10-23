package org.firstinspires.ftc.teamcode.RobotStuff;

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

    private Perseus() {
        super(
                Magazine.INSTANCE,
                Turret.INSTANCE,
                Intake.INSTANCE,
                Shooter.INSTANCE
        );
    }

    public Command setMode(Magazine.MagazineMode mode) {

        Command command = new NullCommand();

        switch (mode) {
            case OUTTAKE_MOTIF:
            case OUTTAKE_MANUAL:
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

        Command shoot = new NullCommand();

        switch (Magazine.INSTANCE.getMode()) {
            case OUTTAKE_MOTIF:
                shoot = new SequentialGroup(
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
                break;
            case OUTTAKE_MANUAL:
                shoot = Shooter.INSTANCE.shoot();
                break;
        }
        return new SequentialGroup(
                modeSwitch,
                shoot
        );
    }

    public Command shootSingle(int slot) {
        return new SequentialGroup(
                Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE_MANUAL),
                Magazine.INSTANCE.setActiveSlot(slot),
                new Delay(0.5),
                Shooter.INSTANCE.shoot(),
                Magazine.INSTANCE.setMode(Magazine.MagazineMode.OUTTAKE)
        );
    }

    public Command intake() {

        Command command;

        if (Magazine.INSTANCE.getMode() != Magazine.MagazineMode.INTAKE) {
            command = new NullCommand(
                    Magazine.INSTANCE.setMode(Magazine.MagazineMode.INTAKE),
                    new Delay(0.5),
                    Intake.INSTANCE.start()
            );
        } else {
            command = Intake.INSTANCE.start();
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
