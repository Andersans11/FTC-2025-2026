package org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.pidStuff.CustomPID;

import kotlin.jvm.functions.Function0;

@Config
public class HoldHeadingPID extends DriveMotors {

    public static double kP = 2; //TODO: TUNE
    public static double kI = 0;
    public static double kD = 0.1;

    public static double secondarykP = 2.5; // TODO: TUNE
    public static double secondarykI = 0;
    public static double secondarykD = 0.05;
    public static double threshold = 0.075;

    public static double turnSpeed = 1;

    double targetRad;

    CustomPID HeadingPID;
    IMU imu;

    boolean useNormTurn;
    double targetHeading;
    double yawVelocity;
    boolean hasExcessVelocity;
    boolean hasVelocity;

    Function0<Float> forwardBackward = () -> (float) (config.playerOne.ForwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    Function0<Float> strafe = () -> (float) (config.playerOne.StrafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    Function0<Float> yaw = () -> {
        updateHeading(config.playerOne.TurnAxis.getValue() * turnSpeed);
        return (float) HeadingPID.lockYawPedro(targetRad, imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
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
        HeadingPID = new CustomPID(opMode.telemetry, config, "HeadingPID");
        HeadingPID.setSecondary(false);
        imu = opMode.hardwareMap.get(IMU.class, "imu"); // ASSIGN IMU BEFORE RUNNING THIS CODE
        setHeading(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
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
        opMode.telemetry.addData("target heading (radians)", targetRad);
    }

    @Override
    public void updateDrive(double deltaTime) {

        telemetryAngleVelocity();

        HeadingPID.setCoefficients(kP, kI, kD);
//        HeadingPID.setSecondaryCoefficients(secondarykP, secondarykI, secondarykD);
//        HeadingPID.setThreshold(threshold);

        vroom.update();
    }

    public void updateHeading() {
        targetHeading = getHeadingDeg();
        targetRad = Math.toRadians(targetHeading);
    }

    public void updateHeading(double modifier) {
        targetHeading = getHeadingDeg() + modifier;
        targetRad = Math.toRadians(targetHeading);
    }

    public void setHeading(double set) {
        targetHeading = set;
        targetRad = Math.toRadians(targetHeading);
    }

    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.SlowDown.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }


    @Override
    public void Start() {vroom.invoke();} // use in onStartButtonPressed()
}

