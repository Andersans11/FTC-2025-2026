package org.firstinspires.ftc.teamcode.AutomousOpModes;

import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.pedro.FollowPath;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.FConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.LConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.VerticalLift;

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


    Path path = new Path(new BezierCurve(new Point(0, 0, Point.CARTESIAN), new Point(10, 0, Point.CARTESIAN)));

    @Override
    public void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
    }

    public Command firstRoutine() {
        return new FollowPath(path);
    }

    @Override
    public void onStartButtonPressed() {
        firstRoutine().invoke();
    }
}
