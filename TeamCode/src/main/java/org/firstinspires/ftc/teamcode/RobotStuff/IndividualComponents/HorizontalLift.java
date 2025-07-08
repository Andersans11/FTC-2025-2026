package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
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
        setLimits(0, 405);
        this.robotConfig = robotConfig;
        this.leftAxon = robotConfig.LeftHorizontal.servo;
        this.rightAxon = robotConfig.RightHorizontal.servo;
        this.servos = Arrays.asList(leftAxon, rightAxon);
        initialize();
    }

    public Command moveLift(Pair<Float, Float> joystickValues) {
        return setTargetPosition(
                (joystickValues.component2()*mult)+targetPosmm
        );
    }

    public Command zero() {
        return setTargetPosition(LiftPreset.MINIMUM);
    }
    public Command max() {
        return setTargetPosition(LiftPreset.MAXIMUM);
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
    public double targetPos; // target pos for servo, so servo power

    public double targetPosmm; // target pos in milliemeters
    public double oldPos;

    public double mult = 1.0; //oh dear we're playing balatro again

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

    public double extensionToServoPower(double requestedExtension) {
        requestedExtension += 203.60435196;
        return angleDifference(requestedExtension) / Math.PI;
    }

    final double AB = 18.08;
    final double AD = 325.0;
    final double CD = 284.16890302;

    /*
    AB is the vertical difference between the servo rotation point and the
    point where the linkage attaches the the slide

    AD is the length of the long linkage

    CD is the length of the short linkage
    */

    double angleCAD;
    double angleBCA;

    public double angleDifference(double BC) {
        // Compute AC Pythagorean Theorem
        double AC = Math.sqrt(AB * AB + BC * BC); // AC is the Hypotenuse

        // Use Law of Cosines to find angle CAD in triangle ACD
        double cosCAD = (AD * AD + AC * AC - CD * CD) / (2 * AD * AC);
        angleCAD = Math.acos(cosCAD);

        // Use Law of Cosines to find angle BCA in triangle ABC
        double cosBCA = (BC * BC + AC * AC - AB * AB) / (2 * BC * AC);
        angleBCA = Math.acos(cosBCA);

        // Return the difference
        return angleCAD - angleBCA;
    }

    public Command setTargetPosition(double requestedPos) { // set target pos via input value

        targetPosmm = requestedPos;
        requestedPos = extensionToServoPower(requestedPos);
        targetPos = fixTarget(requestedPos);

        return new NullCommand();
    }

    enum LiftPreset {
        MINIMUM,
        MAXIMUM
    }
    public Command setTargetPosition(LiftPreset Preset) { // set target pos via preset value

        switch (Preset) {
            case MINIMUM:
                targetPos = 0.304524444; // servo power
                targetPosmm = 0;
                break;

            case MAXIMUM:
                targetPos = 0.0;
                targetPosmm = 405;
                break;
        }
        return new NullCommand();
    }

    @NonNull
    @Override
    public Command getDefaultCommand() {

        if (targetPos != oldPos) { //Move servos if the target position has changed
            oldPos = targetPos;
            return new MultipleServosToPosition(
                    servos,
                    targetPos,
                    this
            );
        } else {
            return new NullCommand();
        }
    }
}
