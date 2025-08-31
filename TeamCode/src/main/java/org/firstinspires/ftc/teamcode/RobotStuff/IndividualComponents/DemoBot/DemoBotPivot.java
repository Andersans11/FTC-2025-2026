package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.gamepad.Button;
import com.rowanmcalpin.nextftc.ftc.gamepad.Control;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.DemoRobotConfig;

public class DemoBotPivot extends Subsystem {
    public static final DemoBotPivot INSTANCE = new DemoBotPivot();
    private DemoBotPivot() { }

    DemoRobotConfig robotConfig;

    double currentPos;
    Servo servo;

    public void initSystem(DemoRobotConfig robotConfig) {
        this.robotConfig = robotConfig;
        this.servo = robotConfig.PivotServo.servo;
        this.currentPos = 0.0;
        initialize();
    }

    public enum Mappings {
        UP,
        DOWN,
        NORMAL
    }

    Button UP;
    Button DOWN;
    Button NORMAL;

    public void map(Control control, Mappings mapping) {
        switch (mapping) {
            case UP:
                if (control instanceof Button) {
                    this.UP = UP.getClass().cast(control);
                    UP.setPressedCommand(INSTANCE::up);
                } else {
                    throw new IllegalArgumentException("UP requires a " + UP.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case DOWN:
                if (control instanceof Button) {
                    this.DOWN = DOWN.getClass().cast(control);
                    DOWN.setPressedCommand(INSTANCE::down);
                } else {
                    throw new IllegalArgumentException("DOWN requires a " + DOWN.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case NORMAL:
                if (control instanceof Button) {
                    this.NORMAL = NORMAL.getClass().cast(control);
                    NORMAL.setPressedCommand(INSTANCE::normal);
                } else {
                    throw new IllegalArgumentException("NORMAL requires a " + NORMAL.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
        }
    }

    public Command setTargetPos(double targetPos) {
        this.currentPos = targetPos;

        return new ServoToPosition(
                servo,
                currentPos,
                this
        );
    }

    public Command normal() {
        return setTargetPos(0.0);
    }


    public Command up() {
        currentPos += 0.01;
        return new ServoToPosition(
                servo,
                currentPos,
                this
        );
    }

    public Command down() {
        currentPos -= 0.01;
        return new ServoToPosition(
                servo,
                currentPos,
                this
        );
    }

    @NonNull
    @Override
    public Command getDefaultCommand() {
        return new ServoToPosition(
                servo,
                currentPos,
                this
        );
    }

}
