package org.firstinspires.ftc.teamcode.enceladus.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DepositClawManual;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.VerticalLiftManual;

public class VerticalLiftPresets extends Subsystem {

    public static final VerticalLiftPresets INSTANCE = new VerticalLiftPresets();
    private VerticalLiftPresets() { } // nftc boilerplate

    //TODO: "0.0"s need to be changed

    public Command SampleScoringPos() {
        return new ParallelGroup(
                VerticalLiftManual.INSTANCE.SetPosition(61.8),
                DepositClawManual.INSTANCE.SetPosition(215, 225)
        );
    }

    public Command TransferPos() {
        return new ParallelGroup(
                VerticalLiftManual.INSTANCE.SetPosition(0.0),
                DepositClawManual.INSTANCE.SetPosition(0.0, 0)
        );
    }

    public Command TransferPosWaiting() {
        return new ParallelGroup(
                VerticalLiftManual.INSTANCE.SetPosition(0.0),
                DepositClawManual.INSTANCE.SetPosition(0.0, 0)
        );
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


}
