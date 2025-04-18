package org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import kotlin.jvm.functions.Function0;

public class TESTRobotCentricDrive extends DriveMotors {

    IMU imu;

    MecanumDriverControlled vroom;


    public TESTRobotCentricDrive(OpMode opMode, RobotConfig config) { // idk the name could be better
        super(opMode, config);
        imu = opMode.hardwareMap.get(IMU.class, "imu");
        this.vroom = new MecanumDriverControlled(driveMotors, FB(), S(), T(), true, imu);
    }


    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.SlowDown.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }

    public Function0<Float> FB() {
        return () -> (float) (config.playerOne.ForwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    }

    public Function0<Float> S() {
        return () -> (float) (config.playerOne.StrafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    }

    public Function0<Float> T() {
        return () -> (float) (config.playerOne.TurnAxis.getValue() * config.sensitivities.getTurningSensitivity() * getSensitivityMod());
    }


    @Override
    public void updateDrive(double deltaTime) {
        vroom.update();
    } // only actually needed for holdHeading because of pid stuff, doesn't need to be called here

    @Override
    public void Start() {vroom.invoke();}
}
