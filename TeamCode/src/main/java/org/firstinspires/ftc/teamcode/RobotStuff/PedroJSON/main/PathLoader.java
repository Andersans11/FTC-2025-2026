package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PathLoader {

    File file;
    Point start;
    Point end;
    Point control1;
    Point control2;
    ArrayList<PathChain> pathDir = new ArrayList<>();
    Follower follower;
    PathBuilder builder;
    Callbacks callbacks;
    OpMode opmode;
    int pathState = 0;

    private Timer pathTimer;


    public PathLoader(File file, Follower follower, OpMode opMode) {
        this.file = file;
        this.follower = follower;
        builder = follower.pathBuilder();
        this.opmode = opMode;
        callbacks = new Callbacks(opMode);
    }

    public PathLoader(String filePath, Follower follower, OpMode opMode) {
        this.file = new File(filePath);
        this.follower = follower;
        builder = follower.pathBuilder();
        this.opmode = opMode;
        callbacks = new Callbacks(opMode);
    }

    public void GatherSubsystems() { //Add any dependencies you need for callbacks here.

    }

    public void Parse() {

        ObjectMapper objectMapper = new ObjectMapper();
        
        try {

            JsonNode rootNode = objectMapper.readTree(file);


            for (int chainNumber = 1; chainNumber <= rootNode.path("pathchains").size(); chainNumber++) {


                for (int pathNumber = 1; pathNumber <= rootNode.path("pathchains").get(chainNumber - 1).path("paths").size(); pathNumber++) {

                    Path path = null;

                    JsonNode pathNode = rootNode.path("pathchains").get(chainNumber - 1).path("paths").get(pathNumber - 1);

                    start = new Point(pathNode.path("start").path("x").asDouble(), pathNode.path("start").path("y").asDouble(), Point.CARTESIAN);

                    end = new Point(pathNode.path("end").path("x").asDouble(), pathNode.path("end").path("y").asDouble(), Point.CARTESIAN);

                    switch (pathNode.path("type").asText()) {
                        case "QUADRATIC":

                            control1 = new Point(pathNode.path("control1").path("x").asDouble(), pathNode.path("control1").path("y").asDouble(), Point.CARTESIAN);
                            path = new Path(new BezierCurve(start, control1, end));
                            break;
                        case "CUBIC":

                            control1 = new Point(pathNode.path("control1").path("x").asDouble(), pathNode.path("control1").path("y").asDouble(), Point.CARTESIAN);
                            control2 = new Point(pathNode.path("control2").path("x").asDouble(), pathNode.path("control2").path("y").asDouble(), Point.CARTESIAN);
                            path = new Path(new BezierCurve(start, control1, control2, end));
                            break;
                        case "LINEAR":

                            path = new Path(new BezierLine(start, end));
                            break;
                    }

                    switch (pathNode.path("interpolation").path("type").asText()) {
                        case "CONSTANT":
                            path.setConstantHeadingInterpolation(Math.toRadians(pathNode.path("interpolation").path("heading1").asDouble()));
                            break;
                        case "LINEAR":
                            path.setLinearHeadingInterpolation(Math.toRadians(pathNode.path("interpolation").path("heading2").asDouble()), Math.toRadians(pathNode.path("interpolation").path("heading1").asDouble()));
                            break;
                        case "TANGENT":
                            break;
                    }

                    builder.addPath(path);

                    for (int callbackNumber = 1; callbackNumber <= pathNode.path("callbacks").size(); callbackNumber++) {


                        JsonNode callbackNode = pathNode.path("callbacks").get(callbackNumber - 1);

                        switch (callbackNode.path("type").asText()) {
                            case "PARAMETRIC":
                                builder.addParametricCallback(callbackNode.path("t").asDouble(), callbacks.GetCallback(callbackNode.path("runnable").asText()));
                                break;
                            case "TEMPORAL":
                                builder.addTemporalCallback(callbackNode.path("t").asDouble(), callbacks.GetCallback(callbackNode.path("runnable").asText()));
                                break;
                        }
                    }
                }
                pathDir.add(builder.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Write() {
        System.out.println("Number of PathChains: " + pathDir.size());
        for (int chainNumber = 1; chainNumber <= pathDir.size(); chainNumber++) {
            PathChain currentChain = pathDir.get(chainNumber - 1);
            System.out.println("  PathChain " + chainNumber + ":");
            System.out.println("    Number of Paths: " + currentChain.size());
        }
    }

    public void Init() {
        if (pathDir != null) {
            opmode.telemetry.addLine("Routine loaded.");
        } else {
            throw new NullPointerException("Error! Routine failed to parse.");
        }

        //Add code to initialize your subsystems here. You can also add a starting position for you subsystems here as well.
    }

    public void Start() {
        pathState = 0;
        pathTimer = new Timer();
    }

    public void Update() {
        if(!follower.isBusy()) {
            follower.followPath(pathDir.get(pathState));
            pathTimer.resetTimer();
            pathState++;
        }
    }
}