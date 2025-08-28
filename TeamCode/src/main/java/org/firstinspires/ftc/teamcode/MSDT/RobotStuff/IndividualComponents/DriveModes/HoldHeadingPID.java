package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DifferenceArrayList;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.PIDStuff.YawPID;

import kotlin.jvm.functions.Function0;

@Config
public class HoldHeadingPID extends DriveMotors {

    public static double kP = 2; //TODO: TUNE
    public static double kI = 0;
    public static double kD = 0.1;

    public static double secondarykP = 2.5; // TODO: TUNE
    public static double secondarykI = 0;
    public static double secondarykD = 0.05;
    public static double threshold = Math.PI / 20;

    public static boolean DEBUGMODE = false;
    public static boolean CLEARDIFF = false;

    GoBildaPinpointDriver pinpoint;
    YawPID HeadingPID;

    DifferenceArrayList differences = new DifferenceArrayList();

    double targetRad;
    double targetHeading;
    long extDTN;

    Function0<Float> forwardBackward = () -> (float) (config.playerOne.ForwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    Function0<Float> strafe = () -> (float) (config.playerOne.StrafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    Function0<Float> yaw = () -> {
        updateHeading(config.playerOne.TurnAxis.getValue() * config.sensitivities.getPIDturningSensitivity());
        return (float) HeadingPID.lockYaw(targetRad,  pinpoint.getHeading(AngleUnit.RADIANS), extDTN);
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

    public HoldHeadingPID(OpMode opMode, RobotConfig config) {
        super(opMode, config);
        HeadingPID = new YawPID(opMode.telemetry, config, "HeadingPID");
        HeadingPID.setSecondary(true);
        pinpoint = opMode.hardwareMap.get(GoBildaPinpointDriver.class, "sensor");
        setHeading(pinpoint.getHeading(AngleUnit.DEGREES)); // on init, take heading and set to that so that robot doesn't go to zero
        this.vroom = new MecanumDriverControlled(driveMotors, forwardBackward, strafe, yaw, true);
    }

    double getHeadingDeg() {
        return pinpoint.getHeading(AngleUnit.DEGREES);
    }

    public void runTelemetry() {
        opMode.telemetry.addLine("================================");
        opMode.telemetry.addData("current heading", getHeadingDeg());
        opMode.telemetry.addData("target heading", targetHeading);

        if (DEBUGMODE) {
            differences.add((float) Math.abs(targetHeading - getHeadingDeg()));

            opMode.telemetry.addData("target heading (radians)", targetRad);
            opMode.telemetry.addData("target - current difference", differences.lastAdded());
            opMode.telemetry.addData("average difference", differences.getCollectiveAverage());
            opMode.telemetry.addData("maximum difference", differences.getMax());
            opMode.telemetry.addData("minimum difference", differences.getMin());
        }
    }

    @Override
    public void updateDrive(long deltaTimeNano) {

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

    public void updateHeading(double modifier) {
        targetHeading = getHeadingDeg() + modifier;
        targetRad = Math.toRadians(targetHeading);
    }

    public void updateHeading() { // if robot turns without joystick except no
        targetHeading = getHeadingDeg();
        targetRad = Math.toRadians(targetHeading);
    }

    public void setHeading(double set) {
        targetHeading = set;
        targetRad = Math.toRadians(targetHeading);
    }

    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.LeftTrigger.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }


    @Override
    public void Start() {vroom.invoke();} // use in onStartButtonPressed()
}

