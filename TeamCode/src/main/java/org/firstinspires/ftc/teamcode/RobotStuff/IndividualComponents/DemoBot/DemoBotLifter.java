package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.gamepad.Button;
import com.rowanmcalpin.nextftc.ftc.gamepad.Control;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.DemoRobotConfig;

import java.util.Arrays;
import java.util.List;

public class DemoBotLifter extends Subsystem {

    public static final DemoBotLifter INSTANCE = new DemoBotLifter();
    private DemoBotLifter() { } // nftc boilerplate

    DemoRobotConfig robotConfig;

    Servo LeftServo;
    Servo RightServo;

    public List<Servo> servos;

    public void initSystem(DemoRobotConfig robotConfig) {
        this.robotConfig = robotConfig;
        this.servos = Arrays.asList(LeftServo, RightServo);
        initialize();
    }

    public enum Mappings {
        COLLECT,
        DEPOSIT,
        HOLD
    }

    Button COLLECT;
    Button DEPOSIT;
    Button HOLD;

    public void map(Control control, Mappings mapping) {
        switch (mapping) {
            case COLLECT:
                if (control instanceof Button) {
                    this.COLLECT = COLLECT.getClass().cast(control);
                    COLLECT.setPressedCommand(INSTANCE::collect);
                } else {
                    throw new IllegalArgumentException("COLLECT requires a " + COLLECT.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case DEPOSIT:
                if (control instanceof Button) {
                    this.DEPOSIT = DEPOSIT.getClass().cast(control);
                    DEPOSIT.setPressedCommand(INSTANCE::deposit);
                } else {
                    throw new IllegalArgumentException("DEPOSIT requires a " + DEPOSIT.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case HOLD:
                if (control instanceof Button) {
                    this.HOLD = HOLD.getClass().cast(control);
                    HOLD.setPressedCommand(INSTANCE::hold);
                } else {
                    throw new IllegalArgumentException("HOLD requires a " + HOLD.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
        }
    }

    public Command collect() {
        return new MultipleServosToPosition(
                servos,
                0.0,
                this
        );
    }

    public Command deposit() {
        return new MultipleServosToPosition(
                servos,
                0.91,
                this
        );
    }

    public Command hold() {
        return new MultipleServosToPosition(
                servos,
                0.15,
                this
        );
    }
}
