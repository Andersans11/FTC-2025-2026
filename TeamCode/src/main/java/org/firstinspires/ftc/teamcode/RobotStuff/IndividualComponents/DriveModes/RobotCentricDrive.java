package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.DriveMotors;

import kotlin.jvm.functions.Function0;

public class RobotCentricDrive extends DriveMotors {

    IMU imu;

    Function0<Float> forwardBackward = () -> (float) (config.playerOne.ForwardAxis.getValue() * config.sensitivities.getForwardSensitivity() * getSensitivityMod());
    Function0<Float> strafe = () -> (float) (config.playerOne.StrafeAxis.getValue() * config.sensitivities.getStrafingSensitivity() * getSensitivityMod());
    Function0<Float> yaw = () -> (float) (config.playerOne.TurnAxis.getValue() * config.sensitivities.getTurningSensitivity() * getSensitivityMod());

    MecanumDriverControlled vroom;


    public RobotCentricDrive(OpMode opMode, RobotConfig config) { // idk the name could be better
        super(opMode, config);
        imu = opMode.hardwareMap.get(IMU.class, "imu");
        this.vroom = new MecanumDriverControlled(driveMotors, forwardBackward, strafe, yaw, true, imu);
    }


    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.LeftTrigger.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }


    @Override
    public void updateDrive(long deltaTimeNano) {
        vroom.update();
    } // only actually needed for holdHeading because of pid stuff, doesn't need to be called here

    @Override
    public void Start() {vroom.invoke();}
}
