package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import kotlin.jvm.functions.Function0;

public class WheelTestMode extends DriveMotors {


    Function0<Float> forwardBackward = () -> (float) (config.playerOne.ForwardAxis.getValue() * config.sensitivities.getForwardSensitivity());
    Function0<Float> strafe = () -> 0.0f;
    Function0<Float> yaw = () -> 0.0f;

    MecanumDriverControlled vroom;

    public WheelTestMode(OpMode opMode, RobotConfig config) { // idk the name could be better
        super(opMode, config);
        this.vroom = new MecanumDriverControlled(driveMotors, forwardBackward, strafe, yaw, true);
    }


    @Override
    public void updateDrive(long deltaTimeNano) {
        vroom.update();
    } // only actually needed for holdHeading because of pid stuff, doesn't need to be called here

    @Override
    public void Start() {
        vroom.invoke();
    }
}