package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.FilletCreator;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

import dev.nextftc.ftc.NextFTCOpMode;


@Configurable
@TeleOp(name = "Tangent Test")
public class TangentTest extends RRoboticsOpMode {

    public TangentTest() {
        super();
    }

    Pose pose1 = new Pose(0, 0);
    Pose pose2 = new Pose(0, 24);
    Pose pose3 = new Pose(24, 24);
    Follower follower;
    FilletCreator filletCreator;

    @Override
    public void Init() {
        BezierLine[] lines = new BezierLine[]{
                new BezierLine(pose1, pose2),
                new BezierLine(pose2, pose3)
        };

        double[] sizes = new double[] {12};
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, Math.toRadians(90)));
        filletCreator = new FilletCreator(lines, sizes);
    }

    @Override
    public void Start() {
        follower.followPath(filletCreator.getPathChain(follower));
    }

    @Override
    public void Update() {
        follower.update();
        addData("pose", "(" + follower.getPose().getX() + ", " + follower.getPose().getY() + ", " + Math.toDegrees(follower.getPose().getHeading()) + ")");
    }
}
