package org.firstinspires.ftc.teamcode.AutomousOpModes;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.*;

import java.io.File;

import PedroJSON.main.PathLoader;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@Autonomous(name="ye")
@Disabled
public class Test_NFTCAuto extends NextFTCOpMode {

    public Test_NFTCAuto() {
        super();
    }

    RobotConfig robotConfig = new RobotConfig(this);


    File routine = new File("TeamCode\\src\\main\\java\\org\\firstinspires\\ftc\\teamcode\\RobotStuff\\PedroJSON\\data\\path_ex3.json");
    Follower follower;
    PathLoader pathLoader;

    @Override
    public void onInit() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        pathLoader = new PathLoader(routine, follower, this);
        pathLoader.GatherSubsystems(); //Add any dependencies you need for callbacks here.

        pathLoader.Parse();
        pathLoader.Init();
    }

    @Override
    public void onStartButtonPressed() {
        pathLoader.Start();
    }

    @Override
    public void onUpdate() {
        pathLoader.Update();
    }
}
