package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;

import java.io.File;
import java.io.IOException;

@Autonomous(name = "When I Need A Plumber")
public class Locktite extends RoyallyFuckedUpMode {
    File iKnowAGuy;
    ObjectMapper theGuy;
    JsonNode funnyPaper;

    public Locktite() {
        super();
    }

    @Override
    public void onInit() {
        super.onInit();
        iKnowAGuy = new File("org/firstinspires/ftc/teamcode/RobotStuff/pedrojson/Data/test.json");
        theGuy = new ObjectMapper();
        try {
            funnyPaper = theGuy.readTree(iKnowAGuy);
            telemetry.addData("number", funnyPaper.path("number").asDouble());
            telemetry.update();
        } catch (IOException e) {
            throw new RuntimeException("fuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuck");
        }
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
