package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DifferenceArrayList;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.RobotStuff.PIDStuff.YawPID;

import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;

import java.util.function.Supplier;


@Config
public class HoldHeadingPID extends DriveMotors {

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
    /*
    for new coders:
    useNormTurn acts as a switch that turns on when the turn stick is moved,
    but only turns off after the robot stop turn to account for excess velocity

    otherwise the robot stop turning
    then go back to the position it was at after the turn stick went back to zero

    it doesn't sound like much but in practice
    it will keep turning about 20ish degree and then
    go back to where it was after the turn stick went back to zero

    basically useNormTurn tells the robot whether to update the heading or not
    so it won't update when the driver is strafing across the board and the pid will keep the heading
    */

    MecanumDriverControlled vroom;

    public HoldHeadingPID(NextFTCOpMode opMode) {
        super(opMode);

        HeadingPID = new YawPID(opMode.telemetry, "HeadingPID");
        HeadingPID.setSecondary(true);

        pinpoint = opMode.hardwareMap.get(GoBildaPinpointDriver.class, "sensor");
        targetRad = getHeadingRad(); // on init, take heading and set to that so that robot doesn't go to zero

        this.vroom = new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (forwardSupp.get() * Sensitivities.getForwardModifier()),
                () -> (strafeSupp.get() * Sensitivities.getStrafeModifier()),
                pidYaw
        );
    }

    double getHeadingRad() {
        return pinpoint.getHeading(AngleUnit.RADIANS);
    }

    public void runTelemetry() {
        opMode.telemetry.addLine("================================");
        opMode.telemetry.addData("current heading (radians)", getHeadingRad());
        opMode.telemetry.addData("target heading (radians)", targetRad);

        if (DEBUGMODE) {
            differences.add((float) Math.abs(targetRad - getHeadingRad()));

            opMode.telemetry.addData("target - current difference", differences.lastAdded());
            opMode.telemetry.addData("average difference", differences.getCollectiveAverage());
            opMode.telemetry.addData("maximum difference", differences.getMax());
            opMode.telemetry.addData("minimum difference", differences.getMin());
        }
    }

    @Override
    public void update(long deltaTimeNano) {

        extDTN = deltaTimeNano; // for pid

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
    public void schedule() {
        vroom.schedule();
    } // use in onStartButtonPressed()
}
