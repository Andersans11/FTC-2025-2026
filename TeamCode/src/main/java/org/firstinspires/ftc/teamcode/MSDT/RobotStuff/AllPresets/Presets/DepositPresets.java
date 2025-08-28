package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.AllPresets.Presets;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DepositClaw;

public class DepositPresets extends Subsystem {

    public static final DepositPresets INSTANCE = new DepositPresets()    ;
    private DepositPresets() { } // nftc boilerplate

    public Command score() {
        return DepositClaw.INSTANCE.setTargetPosition(DepositClaw.DepositClawPresets.CHAMBER);
    }

    public Command transfer() {
        return DepositClaw.INSTANCE.setTargetPosition(DepositClaw.DepositClawPresets.TRANSFER);
    }
    public Command oz() {
        return DepositClaw.INSTANCE.setTargetPosition(DepositClaw.DepositClawPresets.OZ);
    }
    public Command bucket() {
        return DepositClaw.INSTANCE.setTargetPosition(DepositClaw.DepositClawPresets.BUCKET);
    }

}
