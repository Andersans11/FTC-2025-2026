package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON;

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

public class Callbacks extends PedroJSON.main.Callbacks {

    public Callbacks(NextFTCOpMode opMode) {
        super(opMode);
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
