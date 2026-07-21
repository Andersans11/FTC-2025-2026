package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.FilletCreator;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;


@Configurable
@TeleOp(name = "Tangent Test Readout")
public class TangentTestReadout extends RRoboticsOpMode {

    public TangentTestReadout() {
        super();
    }


    Pose pose1 = new Pose(0, 0);
    Pose pose2 = new Pose(12, 24);
    Path path;
    Follower follower;

    @Override
    public void Init() {
        follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void Start() {
        path = new Path(Utils.tangentFromHeadings(new BezierLine(pose1, pose2), 0, 45));
    }

    @Override
    public void Update() {
        addData("start", "(" + path.getFirstControlPoint().getX() + ", " + path.getFirstControlPoint().getY() + ")");
        addData("start", "(" + path.getSecondControlPoint().getX() + ", " + path.getSecondControlPoint().getY() + ")");
        addData("start", "(" + path.getSecondToLastControlPoint().getX() + ", " + path.getSecondToLastControlPoint().getY() + ")");
        addData("start", "(" + path.getLastControlPoint().getX() + ", " + path.getLastControlPoint().getY() + ")");
    }
}
