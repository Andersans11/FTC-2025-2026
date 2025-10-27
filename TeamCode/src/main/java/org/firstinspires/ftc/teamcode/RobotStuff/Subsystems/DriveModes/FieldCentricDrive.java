package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.units.Angle;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


public class FieldCentricDrive extends AbstractDriveMode {

    public static final FieldCentricDrive INSTANCE = new FieldCentricDrive();

    GoBildaPinpointDriver pinpoint;

    @Override
    public void initSystem() {
        super.initSystem();

        this.pinpoint = RobotConfig.Pinpoint;
    }

    @Override
    public void preStart() {
        CommandManager.INSTANCE.scheduleCommand(this.vroom());
    }
    public MecanumDriverControlled vroom() {
        return new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
                () -> (turnSupp.get() * Sensitivities.getTurnModifier()),
                new FieldCentric(
                        () -> (Angle.fromRad(pinpoint.getHeading(AngleUnit.RADIANS)))
                )
        );
    }


}
