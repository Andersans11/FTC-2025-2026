package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.gamepad.Button;
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;
import com.rowanmcalpin.nextftc.ftc.gamepad.Control;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.TeleOpModes.EverythingMode;

import java.util.ArrayList;

import kotlin.Pair;

public class HorizontalLift extends HorizontalLiftInternal {
    public static final HorizontalLift INSTANCE = new HorizontalLift();
    private HorizontalLift() { } // nftc boilerplate

    public void initSystem(RobotConfig robotConfig, NextFTCOpMode opMode) {
        setLimits(0, 352.43);
        this.opMode = opMode;
        this.robotConfig = robotConfig;
        this.leftServo = robotConfig.LeftHorizontal.servo;
        this.rightServo = robotConfig.RightHorizontal.servo;
        this.servos.add(leftServo);
        this.servos.add(rightServo);
        initialize();
    }

    public Command moveLift(Pair<Float, Float> joystickValues) {
        return setTargetPosition(
                (joystickValues.component2() * mult) + targetPosmm
        );
    }

    public enum LiftPreset {
        MINIMUM,
        MAXIMUM,
        FLUSH
    }
    public Command setTargetPosition(LiftPreset Preset) { // set target pos via preset value

        switch (Preset) {
            case MINIMUM:
                targetPos = 0.2375; // servo power
                targetPosmm = 0;
                break;

            case MAXIMUM:
                targetPos = 0.0;
                targetPosmm = 352.43;
                break;

            case FLUSH:
                targetPos = 0.2; // temporary value
                targetPosmm = 320; // also temporary
        }
        return new NullCommand();
    }
}

abstract class HorizontalLiftInternal extends Subsystem {

    public Servo leftServo;
    public Servo rightServo;

    public NextFTCOpMode opMode;

    public ArrayList<Servo> servos = new ArrayList<>();


    public RobotConfig robotConfig;
    public double upperLimit;
    public double lowerLimit;

    public double targetPos; // target pos for servo, so servo power
    public double targetPosmm; // target pos in millimeters
    public double oldPos;

    public static double mult = 0.1; //oh dear we're playing balatro again

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
        requestedExtension += 283.57774992;
        return angleDifference(requestedExtension) / Math.PI;
    }

    final double AB = 17.44280643;
    final double AD = 340.26716033;
    final double CD = 292.39386045;

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
