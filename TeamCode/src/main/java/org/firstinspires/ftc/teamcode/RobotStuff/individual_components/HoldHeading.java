package org.firstinspires.ftc.teamcode.RobotStuff.individual_components;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.pidStuff.CustomPID;

import kotlin.jvm.functions.Function0;

@Config
public class HoldHeading extends DriveMotors {

    public static double kP = 0; //TODO: TUNE
    public static double kI = 0;
    public static double kD = 0;

    double targetRad;

    double extDT;

    CustomPID HeadingPID;
    IMU imu;

    boolean useNormTurn;
    double targetHeading;
    double yawVelocity;
    boolean hasExcessVelocity;
    boolean hasVelocity;

    MecanumDriverControlled vroom = new MecanumDriverControlled(driveMotors, forwardBackward(), strafe(), yaw(), true, imu);

    public HoldHeading(OpMode opMode, RobotConfig config) {
        super(opMode, config);
        HeadingPID = new CustomPID(opMode.telemetry, config, "HeadingPID");
        imu = opMode.hardwareMap.get(IMU.class, "imu");
    }

    double getHeadingDeg() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void telemetryAngleVelocity() {
        opMode.telemetry.addData("Heading", getHeadingDeg());
        opMode.telemetry.addData("TargetHeading", targetHeading);
        opMode.telemetry.addData("angleVelX", imu.getRobotAngularVelocity(AngleUnit.DEGREES).xRotationRate);
        opMode.telemetry.addData("angleVelY", imu.getRobotAngularVelocity(AngleUnit.DEGREES).yRotationRate);
        opMode.telemetry.addData("angleVelZ", imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate);
    }

    @Override
    public void updateDrive(double deltaTime) { //TODO: USE NEXTFTC MECANUMDRIVERCONTROLLED WHEN THIS WORKS

        telemetryAngleVelocity();

        AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.RADIANS);

        extDT = deltaTime;
        yawVelocity = angularVelocity.zRotationRate;
        hasExcessVelocity = (!config.playerOne.turnAxis.getState() && useNormTurn);
        hasVelocity = (yawVelocity > 0.1 || yawVelocity < -0.1);
        targetRad = Math.toRadians(targetHeading);

        HeadingPID.setCoefficients(kP, kI, kD);

        opMode.telemetry.addData("using raw turn values:", useNormTurn);

        vroom.update();
    }

    public void updateHeading() {
        targetHeading = getHeadingDeg();
    }

    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.slowDown.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }

    public Function0<Float> forwardBackward() {
        return () -> (float) (config.playerOne.forwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    }

    public Function0<Float> strafe() {
        return () -> (float) (config.playerOne.strafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    }

    public Function0<Float> yaw() {
        double normTurn = config.playerOne.turnAxis.getValue() * config.sensitivities.getTurningSensitivity() * getSensitivityMod();
        double PIDturn = HeadingPID.lockYaw(targetRad, imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS), extDT);
        double yaw;

        if (config.playerOne.turnAxis.getState()) {useNormTurn = true;}
        if (hasExcessVelocity && !hasVelocity) {useNormTurn = false;}

        if (useNormTurn) {yaw = normTurn; updateHeading();} else {yaw = PIDturn;}
        return () -> (float) (yaw);
    }
}

