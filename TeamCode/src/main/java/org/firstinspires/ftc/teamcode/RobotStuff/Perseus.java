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
        Magazine.INSTANCE.setMode(mode);
        switch (mode) {
            case OUTTAKE:
            case OUTTAKE_MANUAL:
                Shooter.INSTANCE.idle();
                break;
            case INTAKE:
                Shooter.INSTANCE.spinDown();
        }
        return new NullCommand();
    }

    public Command Shoot() {

        Command command = new NullCommand();

        switch (Magazine.INSTANCE.getMode()) {
            case OUTTAKE:
                command = new SequentialGroup(
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
                command = Shooter.INSTANCE.shoot();
                break;
        }
        return command;
    }
}