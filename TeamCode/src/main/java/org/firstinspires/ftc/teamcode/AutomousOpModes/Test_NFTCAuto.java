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

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.FConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.LConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.VerticalLift;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@Autonomous(name="ye")
@Disabled
public class Test_NFTCAuto extends NextFTCOpMode {

    public Test_NFTCAuto() {
        super(VerticalLift.INSTANCE);
    }

    RobotConfig robotConfig = new RobotConfig(this);


    Path path = new Path(new BezierCurve(new Point(0, 0, Point.CARTESIAN), new Point(10, 0, Point.CARTESIAN)));

    @Override
    public void onInit() {
        VerticalLift.INSTANCE.initialize();
        VerticalLift.INSTANCE.configure(robotConfig);
        Constants.setConstants(FConstants.class, LConstants.class);
    }

    public Command firstRoutine() {
        return new SequentialGroup(
                VerticalLift.INSTANCE.toHigh(),
                new ParallelGroup(
                        new FollowPath(path),
                        new SequentialGroup(
                                new Delay(0.5),
                                VerticalLift.INSTANCE.toLow()
                        )
                )
        );
    }

    @Override
    public void onStartButtonPressed() {
        firstRoutine().invoke();
    }
}
