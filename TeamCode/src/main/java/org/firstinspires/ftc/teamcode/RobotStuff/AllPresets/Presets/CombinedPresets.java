package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;

import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DepositClaw;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DepositClawManual;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.VerticalLiftManual;

public class CombinedPresets extends Subsystem {
    public static final CombinedPresets INSTANCE = new CombinedPresets();
    private CombinedPresets() { } // nftc boilerplate

    boolean isRetracted = true;

    public boolean clawPos = true;

    public Command minimum() {
        isRetracted = true;
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM);
    }
    public Command minimum(Float f) {
        isRetracted = true;
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM);
    }

    public Command maximum() {
        isRetracted = false;
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM);
    }
    public Command maximum(Float f) {
        isRetracted = false;
        return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM);
    }

    //TODO: "0.0"s need to be changed

    public Command SampleScorePos() {
        return new ParallelGroup(
                VerticalLiftManual.INSTANCE.SetPosition(61.8),
                DepositClawManual.INSTANCE.SetPosition(215, 225)
        );
    }

    public Command TransferPos() {
        if (isRetracted) {
            return new ParallelGroup( //True Transfer Position
                    VerticalLiftManual.INSTANCE.SetPosition(0.0),
                    DepositClawManual.INSTANCE.SetPosition(0.0, 0)
            );
        } else {
            return new ParallelGroup( //Waiting Transfer Position; Make room for the intake
                    VerticalLiftManual.INSTANCE.SetPosition(0.0),
                    DepositClawManual.INSTANCE.SetPosition(0.0, 0)
            );
        }
    }

    public Command SpecimenScorePos() {
        return new ParallelGroup(
                VerticalLiftManual.INSTANCE.SetPosition(0.0),
                DepositClawManual.INSTANCE.SetPosition(90, 35)
        );
    }

    public Command SpecimenCollectPos() {
        return new ParallelGroup(
                VerticalLiftManual.INSTANCE.SetPosition(0),
                DepositClawManual.INSTANCE.SetPosition(270, 315)
        );
    }

    public Command Claw(float Float) {
        if (clawPos) {
            clawPos = false;
            return DepositClawManual.INSTANCE.Claw(true);
        } else {
            clawPos = true;
            return DepositClawManual.INSTANCE.Claw(false);
        }
    }
}
