package org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;

import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DepositClawManual;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Intake;
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

        Command a = null;

        switch (currentPosition) {
            case SPECSCORE:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(55),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(125, 135),
                                new Delay(0.2),
                                DepositClawManual.INSTANCE.SetPosition(215, 255)
                        )
                );
                break;
            case SPECCOLLECT:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(55),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(215, 255)
                        )
                );
                break;
            case TRANSFER:
                a = new ParallelGroup(
                    VerticalLiftPID.INSTANCE.SetPosition(55),
                    new SequentialGroup(
                            DepositClawManual.INSTANCE.SetPosition(80, 90),
                            new Delay(0.2),
                            DepositClawManual.INSTANCE.SetPosition(170, 180),
                            new Delay(0.3),
                            DepositClawManual.INSTANCE.SetPosition(215, 255)
                    )
                );
                break;
            case SAMPSCORE:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(55),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(215, 255)
                        )
                );
        }
        currentPosition = Position.SAMPSCORE;
        return a;
    }

    public static double VLiftTransferRPos = 6;
    public static double VLiftTransferPos = 12;
    public static double DepoArmTransferRPos = 0.0;
    public static double DepoArmTransferPos = 0.0;
    public static double DepoWristTransferRPos = 0.0;
    public static double DepoWristTransferPos = 0.0;
    public Command TransferPos() {
        Command a = null;

        switch (currentPosition) {
            case SAMPSCORE:
                if (isRetracted) {
                    a = new ParallelGroup( //True Transfer Position
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferRPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(125, 135),
                                    new Delay(0.2),
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferRPos, DepoWristTransferRPos),
                                    new Delay(0.5),
                                    this.Claw()
                            )
                    );
                } else {
                    a = new ParallelGroup( //Waiting Transfer Position; Make room for the intake
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(125, 135),
                                    new Delay(0.2),
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferPos, DepoWristTransferPos)

                            )
                    );
                }
                break;
            case SPECSCORE:
                if (isRetracted) {
                    a = new ParallelGroup( //True Transfer Position
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferRPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferRPos, DepoWristTransferRPos),
                                    new Delay(0.5),
                                    this.Claw()
                            )
                    );
                } else {
                    a = new ParallelGroup( //Waiting Transfer Position; Make room for the intake
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferPos, DepoWristTransferPos)
                            )
                    );
                }
                break;
            case SPECCOLLECT:
                if (isRetracted) {
                    a = new ParallelGroup( //True Transfer Position
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferRPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(180, 190),
                                    new Delay(0.25),
                                    DepositClawManual.INSTANCE.SetPosition(80, 90),
                                    new Delay(0.25),
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferRPos, DepoWristTransferRPos),
                                    new Delay(0.5),
                                    this.Claw()
                            )
                    );
                } else {
                    a = new ParallelGroup( //Waiting Transfer Position; Make room for the intake
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(180, 190),
                                    new Delay(0.25),
                                    DepositClawManual.INSTANCE.SetPosition(80, 90),
                                    new Delay(0.25),
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferPos, DepoWristTransferPos)
                            )
                    );
                }
                break;
            case TRANSFER:
                if (isRetracted) {
                    a = new ParallelGroup( //True Transfer Position
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferRPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferRPos, DepoWristTransferRPos),
                                    new Delay(0.5),
                                    this.Claw()
                            )


                    );
                } else {
                    a = new ParallelGroup( //Waiting Transfer Position; Make room for the intake
                            VerticalLiftPID.INSTANCE.SetPosition(VLiftTransferPos),
                            new SequentialGroup(
                                    DepositClawManual.INSTANCE.SetPosition(DepoArmTransferPos, DepoWristTransferPos)
                            )
                    );
                }
        }
        currentPosition = Position.TRANSFER;
        return a;
    }

    public static double VLiftSpecScore = 32.0;

    public enum Position {
        SPECSCORE,
        SPECCOLLECT,
        SAMPSCORE,
        TRANSFER
    }

    public Position currentPosition = Position.TRANSFER;
    public Command SpecimenScorePos() {
        Command a = null;

        switch (currentPosition) {
            case SPECCOLLECT:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecScore),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(170, 180),
                                new Delay(0.2),
                                DepositClawManual.INSTANCE.SetPosition(80, 60)
                        )
                );
                break;
            case TRANSFER:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecScore),
                        DepositClawManual.INSTANCE.SetPosition(80, 60)
                );
                break;
            case SAMPSCORE:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecScore),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(170, 180),
                                new Delay(0.2),
                                DepositClawManual.INSTANCE.SetPosition(80, 60)
                        )
                );
                break;
            case SPECSCORE:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecScore),
                        DepositClawManual.INSTANCE.SetPosition(80, 60)
                );
                break;
        }
        currentPosition = Position.SPECSCORE;
        return a;
    }

    public static double aa = 340;
    public static double VLiftSpecCollect = 0.0;
    public Command SpecimenCollectPos() {
        
        Command a = null;
        
        switch (currentPosition) {
            case TRANSFER:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecCollect),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(90, 100),
                                new Delay(0.3),
                                DepositClawManual.INSTANCE.SetPosition(270, 280),
                                new Delay(0.2),
                                DepositClawManual.INSTANCE.SetPosition(270, aa)
                        )
                );
                break;
            case SAMPSCORE:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecCollect),
                        DepositClawManual.INSTANCE.SetPosition(270, aa)
                );
                break;
            case SPECSCORE:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecCollect),
                        new SequentialGroup(
                                DepositClawManual.INSTANCE.SetPosition(125, 135),
                                new Delay(0.2),
                                DepositClawManual.INSTANCE.SetPosition(215, 225),
                                new Delay(0.3),
                                DepositClawManual.INSTANCE.SetPosition(270, aa)
                        )
                );
                break;
            case SPECCOLLECT:
                a = new ParallelGroup(
                        VerticalLiftPID.INSTANCE.SetPosition(VLiftSpecCollect),
                        DepositClawManual.INSTANCE.SetPosition(270, aa)
                );
                break;
        }
        currentPosition = Position.SPECCOLLECT;
        return a;
    }

    public Command Claw() {
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
