package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;

import java.util.ArrayList;

public class FilletCreator {

    public ArrayList<Path> paths;

    /**
     * Transforms an array of BezierLines into an Arraylist of Paths with easing curves between them.
     * @param lines an array of all the Lines in the sequence.
     * @param sizes an array of the desired radii of each fillet, corresponding to the curve at its index. Should be one element smaller than curves.
     */
    public FilletCreator(BezierLine[] lines, double[] sizes) {
        paths = new ArrayList<>();
        Path currentPath = new Path(lines[0]);
        for(int i = 1; i < lines.length; i++) {
            Vector pathVector = currentPath.getPose(1).getAsVector().minus(currentPath.getPose(0).getAsVector());
            pathVector.setMagnitude(pathVector.getMagnitude() - sizes[i - 1]);
            pathVector = pathVector.plus(currentPath.getPose(0).getAsVector());
            paths.add(new Path(new BezierLine(currentPath.getPose(0), new Pose(pathVector.getXComponent(), pathVector.getYComponent()))));
            currentPath = new Path(lines[i]);
            pathVector = currentPath.getPose(0).getAsVector().minus(currentPath.getPose(1).getAsVector());
            pathVector.setMagnitude(pathVector.getMagnitude() - sizes[i - 1]);
            pathVector = pathVector.plus(currentPath.getPose(1).getAsVector());
            currentPath = new Path(new BezierLine(new Pose(pathVector.getXComponent(), pathVector.getYComponent()), currentPath.getPose(1)));
            paths.add(new Path(new BezierCurve(paths.get((i - 1) * 2).getPose(1), lines[i - 1].getPose(1), currentPath.getPose(0))));
        }
        paths.add(currentPath);
    }

    public PathChain getPathChain(Follower follower) {
        PathBuilder pathBuilder = follower.pathBuilder();
        for (int i = 0; i < paths.size(); i++) {
            pathBuilder.addPath(paths.get(i));
        }
        return pathBuilder.build();
    }
}
