package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import kotlin.Pair;

public class DepositClaw extends DepositClawInternal {

    // TODO: Update presets from placeholder values

    public static final DepositClaw INSTANCE = new DepositClaw();
    private DepositClaw() { }

    public void initSystem(RobotConfig robotConfig) {
        this.arm = robotConfig.LeftHorizontal.servo;
        this.wrist = robotConfig.RightHorizontal.servo;
    }

    public static double armChamberPos = 0.0;
    public static double armTransferPos = 0.0;
    public static double armOZPos = 0.0;
    public static double armBucketPos = 0.0;

    public static double wristChamberPos = 0.0;
    public static double wristTransferPos = 0.0;
    public static double wristOZPos = 0.0;
    public static double wristBucketPos = 0.0;

    public enum DepositClawPresets {
        CHAMBER,
        TRANSFER,
        OZ,
        BUCKET
    }

    public Command setTargetPosition(DepositClawPresets Preset) { // set target pos via preset value
        switch (Preset) {
            case CHAMBER:
                targetPos = new Pair<>(armChamberPos, wristChamberPos);     break;
            case TRANSFER:
                targetPos = new Pair<>(armTransferPos, wristTransferPos);   break;
            case OZ:
                targetPos = new Pair<>(armOZPos, wristOZPos);               break;
            case BUCKET:
                targetPos = new Pair<>(armBucketPos, wristBucketPos);       break;
        }
        return new NullCommand();
    }
}

abstract class DepositClawInternal extends Subsystem {

    Servo arm, wrist;

    Pair<Double, Double> targetPos = new Pair<>(0.0, 0.0); // Pair<>(arm position, wrist position)
    Pair<Double, Double> oldPos = new Pair<>(0.1, 0.1); // triggers the default command , then both are 0.0


    @NonNull
    @Override
    public Command getDefaultCommand() {

        if (!targetPos.equals(oldPos)) {
            oldPos = targetPos;
            return new SequentialGroup(
                new ServoToPosition(
                    arm,
                    targetPos.component1(),
                    this
                ),
                new ServoToPosition(
                    wrist,
                    targetPos.component2(),
                    this
                )
            );
        } else {
            return new NullCommand();
        }
    }
}