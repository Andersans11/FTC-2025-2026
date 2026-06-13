package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemGroup;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Stayputnik;

public class PIDDrive extends RRoboticsSubsystemGroup {

    public boolean slowMode = false;
    private PIDDrive() {
        super(
            Stayputnik.INSTANCE
        );
    }

    public static final PIDDrive INSTANCE = new PIDDrive();

    @Override
    public void periodic() {
        Stayputnik.INSTANCE.setVector(
                RobotConfig.player1().leftStickX().get() * Sensitivities.strafeModifier.get(),
                RobotConfig.player1().leftStickY().negate().get() * Sensitivities.forwardModifier.get(),
                RobotConfig.player1().rightStickX().negate().get() * Sensitivities.turnModifier.get()
        );

        if (slowMode) {Sensitivities.driveModifier = Sensitivities.slowmodeModifier;
            Sensitivities.turningSensitivity = 1.25f;
        } else {
            Sensitivities.driveModifier = Sensitivities.defaultDriveModifier;
            Sensitivities.turningSensitivity = 1f;
        }
    }
}
