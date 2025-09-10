package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick;
import com.rowanmcalpin.nextftc.ftc.gamepad.JoystickAxis;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

/* A Class to store motors in the config. pull motors from this class when making drives or subsystems. */

public abstract class DriveMotors {

    OpMode opMode;
    RobotConfig config;
    JoystickAxis forwardSupp;
    JoystickAxis strafeSupp;
    JoystickAxis turnSupp;

    public DriveMotors(OpMode opMode, RobotConfig config) {
        this.opMode = opMode;
        this.config = config;

        this.forwardSupp = config.playerOne.ForwardAxis;
        this.strafeSupp = config.playerOne.StrafeAxis;
        this.turnSupp = config.playerOne.TurnAxis;

        driveMotors = new Controllable[] {config.FLDrive.motor, config.FRDrive.motor, config.BLDrive.motor, config.BRDrive.motor};
    }
    public Controllable[] driveMotors;
    //Drive motors
    // oh no way i thought it would be the lift motors

    public float getSensitivityMod() {
        float SensitivityModifier = config.sensitivities.getDriveSensitivity();
        if (config.playerOne.LeftTrigger.getState()){SensitivityModifier = config.sensitivities.getSlowDownModifier();}
        return SensitivityModifier;
    }

    public abstract void updateDrive(long deltaTimeNano);

    public abstract void Start();
}
