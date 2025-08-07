package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;

import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Intake;

public class IntakePresets extends Subsystem {

    public static final IntakePresets INSTANCE = new IntakePresets()    ;
    private IntakePresets() { } // nftc boilerplate

    public Command dropAndIntake(Float f) {
        return new SequentialGroup(
                Intake.INSTANCE.setPower(Intake.IntakePowers.INTAKE),
                Intake.INSTANCE.setTargetPosition(Intake.IntakePositions.INTAKE)
        );
    }
    public Command store(Float f) {
        return new SequentialGroup(
                Intake.INSTANCE.setTargetPosition(Intake.IntakePositions.STORE),
                Intake.INSTANCE.setPower(Intake.IntakePowers.IDLE)
        );
    }

    public Command outtake(Float f) {
        return new SequentialGroup(
                Intake.INSTANCE.setTargetPosition(Intake.IntakePositions.INTAKE),
                Intake.INSTANCE.setPower(Intake.IntakePowers.OUTTAKE)
        );
    }
}
