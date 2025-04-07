package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;
import com.rowanmcalpin.nextftc.pedro.FollowPath;

import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.DriveMotors;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.Test_NFTC;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.LiftBase;
import org.firstinspires.ftc.teamcode.example.java.Lift;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@TeleOp(name = "Test: NFTC TeleOp")
public class Test_NFTCAuto extends NextFTCOpMode {

    public Test_NFTCAuto() {
        super(LiftBase.INSTANCE);
    }

    Path path = new Path(new BezierCurve(new Point(0, 0, Point.CARTESIAN), new Point(10, 0, Point.CARTESIAN)));

    @Override
    public void onInit() {
        LiftBase.INSTANCE.initialize();
    }

    public Command firstRoutine() {
        return new SequentialGroup(
                Lift.INSTANCE.toHigh(),
                new ParallelGroup(
                        new FollowPath(path),
                        new SequentialGroup(
                                new Delay(0.5),
                                Lift.INSTANCE.toLow()
                        )
                )
        );
    }

    @Override
    public void onStartButtonPressed() {
        firstRoutine().invoke();
    }
}
