package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


public class RobotCentricDrive extends AbstractDriveMode {

    public static final RobotCentricDrive INSTANCE = new RobotCentricDrive();
    MecanumDriverControlled vroom;

    @Override
    public void preStart() {
        CommandManager.INSTANCE.scheduleCommand(this.vroom());
    }

    public MecanumDriverControlled vroom() {
        return new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
                () -> (turnSupp.get() * Sensitivities.getTurnModifier())
        );
    }
}
