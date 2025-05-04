package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.gamepad.Button;
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;
import com.rowanmcalpin.nextftc.ftc.gamepad.Control;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import java.util.Arrays;
import java.util.List;

import kotlin.Pair;

public class HorizontalLift extends HorizontalLiftInternal {
    public static final HorizontalLift INSTANCE = new HorizontalLift();
    private HorizontalLift() { } // nftc boilerplate

    public void initLift(RobotConfig robotConfig) {
        setLimits(0.0 ,1.0);
        this.robotConfig = robotConfig;
        this.leftAxon = robotConfig.LeftHorizontal.servo;
        this.rightAxon = robotConfig.RightHorizontal.servo;
        this.servos = Arrays.asList(leftAxon, rightAxon);
    }

    public Command moveLift(Pair<Float, Float> joystickValues) {
        return setTargetPosition(joystickValues.component2());
    }

    public Command zero() {
        return setTargetPosition(0.0);
    }


    public enum Mappings {
        MOVE,
        TO_ZERO
    }

    Joystick MOVE;
    Button TO_ZERO;

    public void map(Control control, Mappings mapping) {
        switch (mapping) {
            case MOVE:
                if (control instanceof Joystick) {
                    this.MOVE = MOVE.getClass().cast(control);
                    MOVE.setDisplacedCommand(INSTANCE::moveLift);
                } else {
                    throw new IllegalArgumentException("MOVE requires a " + MOVE.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case TO_ZERO:
                if (control instanceof Button) {
                    this.TO_ZERO = TO_ZERO.getClass().cast(control);
                    TO_ZERO.setPressedCommand(INSTANCE::zero);
                } else {
                    throw new IllegalArgumentException("TO_ZERO requires a " + TO_ZERO.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
        }
    }

}

abstract class HorizontalLiftInternal extends Subsystem {

    public Servo leftAxon;
    public Servo rightAxon;

    public List<Servo> servos;

    public RobotConfig robotConfig;
    public double upperLimit;
    public double lowerLimit;
    public double targetPos;
    public double oldPos;

    public void setLimits(double lower, double upper) { // use in init
        upperLimit = upper;
        lowerLimit = lower;
    }

    public void fixTargetPos() {
        if (targetPos < lowerLimit) {
            targetPos = lowerLimit;
        }
        if (targetPos > upperLimit) {
            targetPos = upperLimit;
        }
    }

    public Command setTargetPosition(double requestedPos) {

        fixTargetPos();

        oldPos = targetPos;
        targetPos = requestedPos;
        return new MultipleServosToPosition(
                servos,
                targetPos,
                this
        );
    }

    @NonNull
    @Override
    public Command getDefaultCommand() {
        return new MultipleServosToPosition(
                servos,
                targetPos,
                this
        );
    }
}
