package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DemoBot;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.DifferentialArcadeDriverControlled;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.DemoRobotConfig;

public class DemoBotDrive {

    DifferentialArcadeDriverControlled vroom;

    DemoRobotConfig config;

    NextFTCOpMode opMode;

    public DemoBotDrive(NextFTCOpMode opMode, DemoRobotConfig config) {
        this.opMode = opMode;
        this.config = config;
        this.vroom = new DifferentialArcadeDriverControlled(config.LeftWheel.motor, config.RightWheel.motor, config.playerOne.DriveStick, config.playerOne.RightStick);
    }

    public void Start() {
        vroom.invoke();
    }

    public void updateDrive(long deltaTimeNano) {
        vroom.update();
    }
}

