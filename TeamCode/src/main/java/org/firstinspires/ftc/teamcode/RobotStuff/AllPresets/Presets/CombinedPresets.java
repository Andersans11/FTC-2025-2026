package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;

import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DepositClawManual;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.VerticalLiftManual;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.VerticalLiftPID;

@Config
public class CombinedPresets extends Subsystem {
    public static final CombinedPresets INSTANCE = new CombinedPresets();
    private CombinedPresets() { } // nftc boilerplate

    boolean isRetracted = true;
    boolean intakeSeqPressedState = false;
    boolean intakeSeqReleasedState = false;

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

    public Command HLiftChangePos() {
        if (isRetracted) {
            return maximum();
        } else {
            return minimum();
        }
    }

    //TODO: "0.0"s need to be changed

    public Command SampleScorePos() {
        return new ParallelGroup(
                VerticalLiftPID.INSTANCE.SetPosition(61.8),
                DepositClawManual.INSTANCE.SetPosition(215, 225)
        );
    }

    public static double VLiftTransferRPos = 0.0;
    public static double VLiftTransferPos = 0.0;
    public static double DepoArmTransferRPos = 0.0;
    public static double DepoArmTransferPos = 0.0;
    public static double DepoWristTransferRPos = 0.0;
    public static double DepoWristTransferPos = 0.0;
    public Command TransferPos() {
        if (isRetracted) {
            return new ParallelGroup( //True Transfer Position
                    VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferRPos),
                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferRPos, DepoWristTransferRPos)
            );
        } else {
            return new ParallelGroup( //Waiting Transfer Position; Make room for the intake
                    VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferPos),
                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferPos, DepoWristTransferPos)
            );
        }
    }

    public static double VLiftSpecScore = 0.0;
    public Command SpecimenScorePos() {
        return new ParallelGroup(
                VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecScore),
                DepositClawManual.INSTANCE.SetPosition(90, 35)
        );
    }
    public static double VLiftSpecCollect = 0.0;
    public Command SpecimenCollectPos() {
        return new ParallelGroup(
                VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecCollect),
                DepositClawManual.INSTANCE.SetPosition(270, 315)
        );
    }

    public Command Claw(float Float) {
        if (clawPos) {
            clawPos = false;
            return new ParallelGroup(
                    DepositClawManual.INSTANCE.Claw(true),
                    Intake.INSTANCE.OpenStopper()
                );
        } else {
            clawPos = true;
            return new ParallelGroup(
                    DepositClawManual.INSTANCE.Claw(false),
                    Intake.INSTANCE.CloseStopper()
            );
        }
    }



    public Command intakeSequencePressedCommand(Float f) {
        if (!intakeSeqPressedState) {
            intakeSeqPressedState = true;
            isRetracted = false;
            return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MAXIMUM);
        } else {
            intakeSeqPressedState = false;
            return Intake.INSTANCE.store();
        }
    }

    public Command intakeSequenceReleasedCommand(Float f) {
        if (!intakeSeqReleasedState) {
            intakeSeqReleasedState = true;
            return Intake.INSTANCE.intake();
        } else {
            intakeSeqReleasedState = false;
            isRetracted = true;
            return HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM);
        }
    }

    public Command intakeSequenceOuttakePressedCommand(Float f) {
        if (intakeSeqReleasedState) {
            return Intake.INSTANCE.outtake();
        } else {
            return new NullCommand();
        }
    }

    public Command intakeSequenceOuttakeReleasedCommand(Float f) {
        if (intakeSeqReleasedState) {
            return Intake.INSTANCE.intake();
        } else {
            return new NullCommand();
        }
    }
}
