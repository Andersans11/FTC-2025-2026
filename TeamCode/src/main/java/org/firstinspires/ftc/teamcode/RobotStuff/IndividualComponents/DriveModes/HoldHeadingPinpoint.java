package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.RobotStuff.PIDStuff.YawPID;

import kotlin.jvm.functions.Function0;

@Config
public class HoldHeadingPinpoint extends DriveMotors {

    public static double kP = 0; //TODO: TUNE
    public static double kI = 0;
    public static double kD = 0;

    double targetRad;

    long extDTN;

    GoBildaPinpointDriver pinpoint;

    YawPID HeadingPID;

    boolean useNormTurn;
    double targetHeading;
    boolean hasExcessVelocity;
    boolean hasVelocity;


    Function0<Float> forwardBackward = () -> (float) (config.playerOne.ForwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    Function0<Float> strafe = () -> (float) (config.playerOne.StrafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    Function0<Float> yaw = () -> { // funny lambda
        if (useNormTurn) {
            updateHeading();
            return (float) (config.playerOne.TurnAxis.getValue() * config.sensitivities.getTurningSensitivity());
        } else {
            return (float) HeadingPID.lockYaw(
                    targetRad,
                    pinpoint.getHeading(AngleUnit.RADIANS),
                    extDTN
            );
        }
    };
    /*
    for new coders:
    useNormTurn acts as a switch that turns on when the turn stick is moved,
    but only turns off after the robot stop turn to account for excess velocity

    otherwise the robot stops turning,
    then go back to the position it was at after the turn stick went back to zero

    it doesn't sound like much but in practice,
    it will keep turning about 20ish degrees and then
    go back to where it was after the turn stick went back to zero

    basically useNormTurn tells the robot whether to update the heading or not
    so it won't update when the driver is strafing across the board and the pid will keep the heading
    */

    MecanumDriverControlled vroom;

    public HoldHeadingPinpoint(OpMode opMode, RobotConfig config) {
        super(opMode, config);
        HeadingPID = new YawPID(opMode.telemetry, config, "HeadingPID");
        pinpoint = opMode.hardwareMap.get(GoBildaPinpointDriver.class, "sensor");
        this.vroom = new MecanumDriverControlled(driveMotors, forwardBackward, strafe, yaw, true);
    }

    double getHeadingDeg() {
        return pinpoint.getHeading(AngleUnit.DEGREES);
    }

    public void telemetryAngleVelocity() {
        if (useNormTurn) {opMode.telemetry.addLine("Mode: Using raw turn values");}
        else {opMode.telemetry.addLine("Mode: Holding heading using IMU");}
        opMode.telemetry.addLine("================================");
        opMode.telemetry.addData("current heading", getHeadingDeg());
        opMode.telemetry.addData("target heading", targetHeading);
    }

    @Override
    public void updateDrive(long deltaTimeNano) {

        telemetryAngleVelocity();

        double yawVelocity = pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS);

        extDTN = deltaTimeNano; // don't delete this

        //funny boolean shit
        hasExcessVelocity = (!config.playerOne.TurnAxis.getState() && useNormTurn); // if the stick is not pressed but is still using normTurn (excess velocity after done turning)
        hasVelocity = (yawVelocity > 0.1 || yawVelocity < -0.1); // if the robot is turning at all
        targetRad = Math.toRadians(targetHeading); // passed into the pid so the robot turns 5 degrees instead of 355 to get to 360

        if (config.playerOne.TurnAxis.getState()) {useNormTurn = true;} // always use normTurn if turn stick is being moved
        if (hasExcessVelocity && !hasVelocity) {useNormTurn = false;} // basically normTurn is true until the robot stops moving (hasVelocity is false)

        HeadingPID.setCoefficients(kP, kI, kD);

        vroom.update();
    }

    public void updateHeading() {
        targetHeading = getHeadingDeg();
    }

    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.SlowDown.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }


    @Override
    public void Start() {vroom.invoke();}
}

