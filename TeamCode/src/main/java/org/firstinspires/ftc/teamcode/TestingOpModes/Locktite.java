package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Autonomous(name = "When I Need A Plumber")
public class Locktite extends RoyallyFuckedUpMode {
    JSONObject funnyPaper;

    public Locktite() {
        super();
    }

    @Override
    public void onInit() {
        super.onInit();
        
        try {
            // Access file from assets folder using hardwareMap context
            InputStream inputStream = hardwareMap.appContext.getAssets().open("test.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            
            // Read the entire file as text
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            reader.close();
            
            // Parse with Android's native JSON parser
            funnyPaper = new JSONObject(jsonText.toString());
            
            addData("number", funnyPaper.getDouble("number"));
            addData("Status", "File loaded successfully!");
            telemetry.update();
            
        } catch (Exception e) {
            // Handle error gracefully without crashing
            addData("ERROR", "File read failed");
            addData("Exception", e.getClass().getSimpleName());
            addData("Message", e.getMessage() != null ? e.getMessage() : "No message");
            telemetry.update();
            e.printStackTrace();
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
