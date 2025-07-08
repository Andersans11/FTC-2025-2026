package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main.PathLoader;

public class test {
    public static void main(String[] args) {
        // Try different possible paths for the JSON file
        String[] possiblePaths = {
                "TeamCode\\src\\main\\java\\org\\firstinspires\\ftc\\teamcode\\RobotStuff\\PedroJSON\\data\\path_ex2.json"
        };
        
        File file = null;
        for (String path : possiblePaths) {
            File testFile = new File(path);
            System.out.println("Trying path: " + testFile.getAbsolutePath());
            if (testFile.exists()) {
                file = testFile;
                System.out.println("Found file at: " + path);
                break;
            }
        }
        
        if (file == null) {
            System.out.println("Could not find path_ex.json file!");
            return;
        }
        
        PathLoaderText pathLoader = new PathLoaderText(file);

        pathLoader.Parse();
    }
}
