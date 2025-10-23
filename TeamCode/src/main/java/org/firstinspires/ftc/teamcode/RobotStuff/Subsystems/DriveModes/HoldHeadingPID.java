package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;


import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DifferenceArrayList;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.RobotStuff.PIDStuff.YawPID;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

import java.util.function.Supplier;


@Configurable
public class HoldHeadingPID extends DriveMotors {

    public static final HoldHeadingPID INSTANCE = new HoldHeadingPID();

    public static double kP = 0; //TODO: TUNE
    public static double kI = 0;
    public static double kD = 0;

    public static double secondarykP = 0; // TODO: TUNE
    public static double secondarykI = 0;
    public static double secondarykD = 0;
    public static double threshold = Math.PI / 20;

    public static boolean DEBUGMODE = false;
    public static boolean CLEARDIFF = false;

    GoBildaPinpointDriver pinpoint;
    YawPID HeadingPID;

    DifferenceArrayList differences = new DifferenceArrayList();

    double targetRad;
    long extDTN;


    Supplier<Double> pidYaw = () -> {
        targetRad = getHeadingRad() + (turnSupp.get() * Sensitivities.getPIDTurnModifier());
        return HeadingPID.lockYaw(targetRad,  pinpoint.getHeading(AngleUnit.RADIANS), extDTN);
    };

    MecanumDriverControlled vroom;

    @Override
    public void initialize() {

        HeadingPID = new YawPID(telemetry, "HeadingPID");
        HeadingPID.setSecondary(true);
    }

    double getHeadingRad() {
        return pinpoint.getHeading(AngleUnit.RADIANS);
    }

    public void runTelemetry() {
        telemetry.addLine("================================");
        telemetry.addData("current heading (radians)", getHeadingRad());
        telemetry.addData("target heading (radians)", targetRad);

        if (DEBUGMODE) {
            differences.add((float) Math.abs(targetRad - getHeadingRad()));

            telemetry.addData("target - current difference", differences.lastAdded());
            telemetry.addData("average difference", differences.getCollectiveAverage());
            telemetry.addData("maximum difference", differences.getMax());
            telemetry.addData("minimum difference", differences.getMin());
        }
    }



    @Override
    public void periodic() {
        extDTN = RobotConfig.getDelta(); // for pid

        runTelemetry(); // runs telemetry logic

        if (CLEARDIFF) {
            differences.clear();
            CLEARDIFF = false; // user sets to true, resets diff and goes to false
        }

        HeadingPID.setCoefficients(kP, kI, kD);
        HeadingPID.setSecondaryCoefficients(secondarykP, secondarykI, secondarykD);
        HeadingPID.setThreshold(threshold);

        vroom.update();
    }

    @Override
    public void hardware() {
        pinpoint = RobotConfig.Pinpoint;
        targetRad = getHeadingRad(); // on init, take heading and set to that so that robot doesn't go to zero
    }

    @Override
    public void commands() {
        this.vroom = new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
                pidYaw
        );

        this.vroom.schedule();
    }
}
