package org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import kotlin.jvm.functions.Function0;

public class FieldCentricDrive extends DriveMotors {

    IMU imu;

    Function0<Float> forwardBackward = () -> (float) (config.playerOne.forwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    Function0<Float> strafe = () -> (float) (config.playerOne.strafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    Function0<Float> yaw = () -> (float) (config.playerOne.turnAxis.getValue() * config.sensitivities.getTurningSensitivity() * getSensitivityMod());

    MecanumDriverControlled vroom;


    public FieldCentricDrive(OpMode opMode, RobotConfig config) { // idk the name could be better
        super(opMode, config);
        imu = opMode.hardwareMap.get(IMU.class, "imu");
        this.vroom = new MecanumDriverControlled(driveMotors, forwardBackward, strafe, yaw, false, imu);
    }


    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.slowDown.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }


    @Override
    public void updateDrive(double deltaTime) {
        vroom.update();
    }

    @Override
    public void Start(){vroom.invoke();}
}
