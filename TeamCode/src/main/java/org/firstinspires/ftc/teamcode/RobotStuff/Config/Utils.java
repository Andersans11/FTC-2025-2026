package org.firstinspires.ftc.teamcode.RobotStuff.Config;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import java.util.ArrayList;

public class Utils {
    public static final String PRIORITY_PRIORITY = "aaa";
    public static final String PRIORITY = "aba";
    public static final String UTILITY = "aca";
    public static final String WORKING = "acb";
    public static final String TUNE = "acc";
    public static final String TESTING = "baa";
    public static final String UNUSABLE_DUETOHARDWARE = "caa";
    public static final String UNUSABLE_DUETOCODE = "cba";
    public static final String BORKEN = "zzz";

    public enum ArtifactTypes {
        GREEN,
        PURPLE,
        NONE
    }

    public static final ArtifactTypes[] GPPGPP = new ArtifactTypes[]{
            ArtifactTypes.GREEN,
            ArtifactTypes.PURPLE,
            ArtifactTypes.PURPLE,
            ArtifactTypes.GREEN,
            ArtifactTypes.PURPLE,
            ArtifactTypes.PURPLE
    };

    public static final ArtifactTypes[] PGPPGP = new ArtifactTypes[]{
            ArtifactTypes.PURPLE,
            ArtifactTypes.GREEN,
            ArtifactTypes.PURPLE,
            ArtifactTypes.PURPLE,
            ArtifactTypes.GREEN,
            ArtifactTypes.PURPLE
    };

    public static final ArtifactTypes[] PPGPPG = new ArtifactTypes[]{
            ArtifactTypes.PURPLE,
            ArtifactTypes.PURPLE,
            ArtifactTypes.GREEN,
            ArtifactTypes.PURPLE,
            ArtifactTypes.PURPLE,
            ArtifactTypes.GREEN
    };

    public static final int RED_GOAL = 1;
    public static final int BLUE_GOAL = 2;
    public static final int MOTIF_GPP = 3;
    public static final int MOTIF_PGP = 4;
    public static final int MOTIF_PPG = 5;
    
    public static double headingThreshold = 0.25;

    public static BezierCurve tangentFromHeadings(BezierLine line, double startHeading, double endHeading) {
        Vector vector1 = new Vector(12, Math.toRadians(startHeading)).plus(line.getPose(0).getAsVector());
        Pose control1 = new Pose(vector1.getXComponent(), vector1.getYComponent());
        Vector vector2 = new Vector(-12, Math.toRadians(endHeading)).plus(line.getPose(1).getAsVector());
        Pose control2 = new Pose(vector2.getXComponent(), vector2.getYComponent());
        return new BezierCurve(line.getPose(0), control1, control2, line.getPose(1));
    }

    public static BezierCurve tangentFromHeadings(BezierLine line, double startHeading, double endHeading, double power1, double power2) {
        Vector vector1 = new Vector(power1, Math.toRadians(startHeading)).plus(line.getPose(0).getAsVector());
        Pose control1 = new Pose(vector1.getXComponent(), vector1.getYComponent());
        Vector vector2 = new Vector(-power2, Math.toRadians(endHeading)).plus(line.getPose(1).getAsVector());
        Pose control2 = new Pose(vector2.getXComponent(), vector2.getYComponent());
        return new BezierCurve(line.getPose(0), control1, control2, line.getPose(1));
    }

    public static BezierCurve reverseTangentFromHeadings(BezierLine line, double startHeading, double endHeading) {
        Vector vector1 = new Vector(-12, Math.toRadians(startHeading)).plus(line.getPose(0).getAsVector());
        Pose control1 = new Pose(vector1.getXComponent(), vector1.getYComponent());
        Vector vector2 = new Vector(12, Math.toRadians(endHeading)).plus(line.getPose(1).getAsVector());
        Pose control2 = new Pose(vector2.getXComponent(), vector2.getYComponent());
        return new BezierCurve(line.getPose(0), control1, control2, line.getPose(1));
    }

    public static BezierCurve reverseTangentFromHeadings(BezierLine line, double startHeading, double endHeading, double power1, double power2) {
        Vector vector1 = new Vector(-power1, Math.toRadians(startHeading)).plus(line.getPose(0).getAsVector());
        Pose control1 = new Pose(vector1.getXComponent(), vector1.getYComponent());
        Vector vector2 = new Vector(power2, Math.toRadians(endHeading)).plus(line.getPose(1).getAsVector());
        Pose control2 = new Pose(vector2.getXComponent(), vector2.getYComponent());
        return new BezierCurve(line.getPose(0), control1, control2, line.getPose(1));
    }
}

