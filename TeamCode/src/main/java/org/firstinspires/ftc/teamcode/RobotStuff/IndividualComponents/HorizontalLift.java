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

    public void initSystem(RobotConfig robotConfig) {
        setLimits(0, 160);
        this.robotConfig = robotConfig;
        this.leftAxon = robotConfig.LeftHorizontal.servo;
        this.rightAxon = robotConfig.RightHorizontal.servo;
        this.servos = Arrays.asList(leftAxon, rightAxon);
        initialize();
    }

    public Command moveLift(Pair<Float, Float> joystickValues) {
        return setTargetPosition(
                extensionToServoPower(joystickValues.component2()) + targetPos
        );
    }

    public Command zero() {
        return setTargetPosition(0);
    }
    public Command max() {
        return setTargetPosition(160);
    }


    public enum Mappings {
        MOVE,
        TO_ZERO,
        TO_MAX
    }

    Joystick MOVE;
    Button TO_ZERO;
    Button TO_MAX;

    public void map(Control control, Mappings mapping) {
        switch (mapping) {
            case MOVE:
                if (control instanceof Joystick) {
                    this.MOVE = MOVE.getClass().cast(control); // button, joystick, etc classes extend control
                    MOVE.setDisplacedCommand(INSTANCE::moveLift); // so it will only accept one of those classes, but it needs to be specifically cast to the right input type
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
            case TO_MAX:
                if (control instanceof Button) {
                    this.TO_MAX = TO_MAX.getClass().cast(control);
                    TO_MAX.setPressedCommand(INSTANCE::max);
                } else {
                    throw new IllegalArgumentException("TO_MAX requires a " + TO_MAX.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
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

    public final double PD = 40.08; // distance from top of slide stack to pivot point
    public final double AD = 22; // distance from linkage attachment point on slide to top of slide stack
    public final double LL = 320; // length of long linkage
    public final double SL = 304.1898371; // length of short linkage
    public final double DIFF = PD - AD; // we can get rid of the rectangle in the formed trapezoid

    public void setLimits(double lower, double upper) { // use in init
        upperLimit = extensionToServoPower(upper);
        lowerLimit = extensionToServoPower(lower);
    }

    public double fixTarget(double target) {
        if (target < lowerLimit) {
            target = lowerLimit;
        }
        if (target > upperLimit) {
            target = upperLimit;
        }

        return target;
    }
    public double pythagoreanTheorem(double a, double b) {
        a = Math.pow(a, 2);
        b = Math.pow(b, 2);
        return Math.sqrt(a + b);
    }

    public double extensionToServoPower(double requestedExtension) {
        requestedExtension += 300; //account for slide length
        double LA = pythagoreanTheorem(requestedExtension, DIFF);
        double RA = Math.atan(requestedExtension / DIFF); //inv_cos_in = ((LA**2 + L**2 - S**2) / (2 * LA * L))
        double arcCosInput = ((Math.pow(LA, 2) + Math.pow(LL, 2) - Math.pow(SL, 2)) / (2 * LA * LL));
        double RL = Math.acos(arcCosInput);
        double RT = RA + RL;
        RT -= extensionBuffer();
        RT = Math.abs(RT);
        return Math.toDegrees(RT) / 180;  // input of 160 (max) results in negative when subtracted, should have a value bigger than zero
    }// convert at return for accuracy

    private double extensionBuffer() { // make the servo's zero position at minimum extension (300 bc of slide length)
        double LA = pythagoreanTheorem(300, DIFF);
        double RA = Math.atan(300 / DIFF);
        double arcCosInput = ((Math.pow(LA, 2) + Math.pow(LL, 2) - Math.pow(SL, 2)) / (2 * LA * LL));
        double RL = Math.acos(arcCosInput);
        return RA + RL;
    }

    public Command setTargetPosition(double requestedPos) {

        requestedPos = extensionToServoPower(requestedPos);

        oldPos = targetPos;
        targetPos = fixTarget(requestedPos);

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
