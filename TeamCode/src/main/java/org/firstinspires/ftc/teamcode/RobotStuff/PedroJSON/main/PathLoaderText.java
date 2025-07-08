package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PathLoaderText {

    File file;


    public PathLoaderText(File file) {
        this.file = file;
    }

    public PathLoaderText(String filePath) {
        this.file = new File(filePath);
    }

    public void Parse() {

        ObjectMapper objectMapper = new ObjectMapper();
        
        try {

            JsonNode rootNode = objectMapper.readTree(file);
            System.out.println("Successfully loaded JSON file!");


            System.out.println("Number of pathchains: " + rootNode.path("pathChains").size());
                
            for (int chainNumber = 1; chainNumber <= rootNode.path("pathChains").size(); chainNumber++) {


                System.out.println("Pathchain " + chainNumber + ":");
                
                System.out.println("  Number of paths: " + rootNode.path("pathChains").get(chainNumber - 1).path("paths").size());

                for (int pathNumber = 1; pathNumber <= rootNode.path("pathChains").get(chainNumber - 1).path("paths").size(); pathNumber++) {


                    JsonNode pathNode = rootNode.path("pathChains").get(chainNumber - 1).path("paths").get(pathNumber - 1);

                    System.out.println("  Path " + pathNumber + ":");
                    System.out.println("    ID: " + pathNode.path("id").asText());
                    System.out.println("    Type: " + pathNode.path("type").asText());


                    System.out.println("    Start: (" + pathNode.path("start").path("x").asDouble() + ", " + pathNode.path("start").path("y").asDouble() + ")");

                    System.out.println("    End: (" + pathNode.path("end").path("x").asDouble() + ", " + pathNode.path("end").path("y").asDouble() + ")");

                    switch (pathNode.path("type").asText()) {
                        case "QUADRATIC":

                            System.out.println("    Control: (" + pathNode.path("control1").path("x").asDouble() + ", " + pathNode.path("control1").path("y").asDouble() + ")");

                            break;
                        case "CUBIC":

                            System.out.println("    Control1: (" + pathNode.path("control1").path("x").asDouble() + ", " + pathNode.path("control1").path("y").asDouble() + ")");
                            System.out.println("    Control2: (" + pathNode.path("control2").path("x").asDouble() + ", " + pathNode.path("control2").path("y").asDouble() + ")");

                            break;
                        case "LINEAR":
                            break;
                    }


                    System.out.println("    Interpolation: " + pathNode.path("interpolation").path("type").asText());

                    switch (pathNode.path("interpolation").path("type").asText()) {
                        case "CONSTANT":

                            System.out.println("      Heading: " + pathNode.path("interpolation").path("heading1").asDouble());
                            break;
                        case "LINEAR":
                            System.out.println("      Heading Start: " + pathNode.path("interpolation").path("heading1").asDouble());
                            System.out.println("      Heading End: " + pathNode.path("interpolation").path("heading2").asDouble());
                            break;
                        case "TANGENT":
                            break;
                    }

                    System.out.println("    Number of Callbacks: " + pathNode.path("callbacks").size());

                    for (int callbacks = 1; callbacks <= pathNode.path("callbacks").size(); callbacks++) {


                        JsonNode callbackNode = pathNode.path("callbacks").get(callbacks - 1);

                        System.out.println("      Callback " + callbacks + ":");

                        System.out.println("        Type: " + callbackNode.path("type").asText());

                        System.out.println("          t: " + callbackNode.path("t").asDouble());

                        System.out.println("        identifier: " + callbackNode.path("runnable").asText());

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}