package org.firstinspires.ftc.teamcode.RobotStuff.pedrojson;

import dev.nextftc.ftc.NextFTCOpMode;

public class Callbacks extends PedroJSON.main.Callback {

    public Callbacks(NextFTCOpMode opMode) {
        super();
    }

    @Override
    public Runnable GetCallback(String identifier) {

        Runnable codeToRun = null;

        switch (identifier) {
            case "example":
                codeToRun = () -> {
                    // command here
                };
                break;
        }

        if (codeToRun == null) {
            throw (new NullPointerException("Command not found! Make sure all your commands are in the callback class."));
        }

        return codeToRun;
    }
}
