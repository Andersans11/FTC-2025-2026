package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets.CombinedPresets;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Intake;

public class Callbacks extends PedroJSON.main.Callback {

    public Callbacks() {

    }

    @Override
    public Runnable GetCallback(String identifier) {

        Runnable codeToRun = null;

        switch (identifier) {
            case "DepoTransfer":
                codeToRun = () -> {
                    CombinedPresets.INSTANCE.TransferPos().invoke();
                };
                break;
            case "DepoSamp":
                codeToRun = () -> {
                    CombinedPresets.INSTANCE.SampleScorePos().invoke();
                };
                break;
            case "DepoSpecScore":
                codeToRun = () -> {
                    CombinedPresets.INSTANCE.SpecimenScorePos().invoke();
                };
                break;
            case "DepoSpecCollect":
                codeToRun = () -> {
                    CombinedPresets.INSTANCE.SpecimenCollectPos().invoke();
                };
                break;
            case "HorizMin":
                codeToRun = () -> {
                    HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM).invoke();
                };
                break;
            case "HorizMax":
                codeToRun = () -> {
                    HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM).invoke();
                };
                break;
            case "IntakeStore":
                codeToRun = () -> {
                    Intake.INSTANCE.store().invoke();
                };
                break;
            case "IntakeBrr":
                codeToRun = () -> {
                    Intake.INSTANCE.intake().invoke();
                };
                break;
        }

        if (codeToRun == null) {
            throw (new NullPointerException("Command not found! Make sure all your commands are in the callback class."));
        }

        return codeToRun;
    }
}
