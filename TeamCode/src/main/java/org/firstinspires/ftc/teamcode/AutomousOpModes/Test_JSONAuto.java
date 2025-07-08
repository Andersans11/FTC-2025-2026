package org.firstinspires.ftc.teamcode.AutomousOpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.FConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.LConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main.PathLoader;

import java.io.File;

@Autonomous(name = "Test: JSON Auto")
public class Test_JSONAuto extends OpMode {
    File routine = new File("TeamCode\\src\\main\\java\\org\\firstinspires\\ftc\\teamcode\\RobotStuff\\PedroJSON\\data\\path_ex3.json");
    Follower follower;
    PathLoader pathLoader;

    @Override
    public void init() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        pathLoader = new PathLoader(routine, follower, this);
        pathLoader.GatherSubsystems(); //Add any dependencies you need for callbacks here.

        pathLoader.Parse();
        pathLoader.Init();
    }

    @Override
    public void start() {
        pathLoader.Start();
    }

    @Override
    public void loop() {
        pathLoader.Update();
    }
}
