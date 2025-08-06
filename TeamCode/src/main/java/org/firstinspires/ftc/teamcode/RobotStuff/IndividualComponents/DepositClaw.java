package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

public class DepositClaw extends Subsystem {

    //TODO: Update presets from placeholder values

    public static final DepositClaw INSTANCE = new DepositClaw();
    private DepositClaw() { }

    Servo arm;
    Servo wrist;

    public Command ScoringPos() {
        return new SequentialGroup(
                new ServoToPosition(
                        arm,
                        0.0,
                        this
                ),
                new ServoToPosition(
                        wrist,
                        0.0,
                        this
                )
        );
    }

    public Command TransferPos() {
        return new SequentialGroup(
                new ServoToPosition(
                        arm,
                        0.0,
                        this
                ),
                new ServoToPosition(
                        wrist,
                        0.0,
                        this
                )
        );
    }

    public Command ObservePos() {
        return new SequentialGroup(
                new ServoToPosition(
                        arm,
                        0.0,
                        this
                ),
                new ServoToPosition(
                        wrist,
                        0.0,
                        this
                )
        );
    }

    public Command BucketPos() {
        return new SequentialGroup(
                new ServoToPosition(
                        arm,
                        0.0,
                        this
                ),
                new ServoToPosition(
                        wrist,
                        0.0,
                        this
                )
        );
    }

    @Override
    public void initialize() {
        arm = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "Arm Servo");
        wrist = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "Wrist Servo");
    }
}
