package org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.pidStuff.CustomPID;

import kotlin.jvm.functions.Function0;

@Config
public class HoldHeading extends DriveMotors {

    public static double kP = 2; //TODO: TUNE
    public static double kI = 0;
    public static double kD = 0.1;

    public static double secondarykP = 2.5; // TODO: TUNE
    public static double secondarykI = 0;
    public static double secondarykD = 0.05;
    public static double threshold = Math.PI / 20;

    double targetRad;

    long extDTN;

    CustomPID HeadingPID;
    IMU imu;

    boolean useNormTurn;
    double targetHeading;
    double yawVelocity;
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
                    imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS),
                    extDTN
            );
        }
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

    public HoldHeading(OpMode opMode, RobotConfig config) {
        super(opMode, config);
        HeadingPID = new CustomPID(opMode.telemetry, config, "HeadingPID");
        HeadingPID.setSecondary(true);
        imu = opMode.hardwareMap.get(IMU.class, "imu"); // ASSIGN IMU BEFORE RUNNING THIS CODE
        this.vroom = new MecanumDriverControlled(driveMotors, forwardBackward, strafe, yaw, true);
    }

    double getHeadingDeg() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
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

        AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.RADIANS);

        extDTN = deltaTimeNano; // to be passed into yaw()
        yawVelocity = angularVelocity.zRotationRate; // speed at which the robot is turning

        //funny boolean shit
        hasExcessVelocity = (!config.playerOne.TurnAxis.getState() && useNormTurn); // if the stick is not pressed but is still using normTurn (excess velocity after done turning)
        hasVelocity = (yawVelocity > 0.1 || yawVelocity < -0.1); // if the robot is turning at all
        targetRad = Math.toRadians(targetHeading); // passed into the pid so the robot turns 5 degrees instead of 355 to get to 360

        if (config.playerOne.TurnAxis.getState()) {useNormTurn = true;} // always use normTurn if turn stick is not zero
        if (hasExcessVelocity && !hasVelocity) {useNormTurn = false;}

        HeadingPID.setCoefficients(kP, kI, kD);
        HeadingPID.setSecondaryCoefficients(secondarykP, secondarykI, secondarykD);
        HeadingPID.setThreshold(threshold);

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
    public void Start() {vroom.invoke();} // use in onStartButtonPressed()
}

