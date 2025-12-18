package org.firstinspires.ftc.teamcode.RobotStuff.pedrojson;

import org.firstinspires.ftc.teamcode.RobotStuff.Artemis;

import dev.nextftc.ftc.NextFTCOpMode;

public class Callbacks extends PedroJSON.main.Callback {

    public Callbacks(NextFTCOpMode opMode) {
        super();
    }

    @Override
    public Runnable GetCallback(String identifier) {

        Runnable codeToRun = null;

        switch (identifier) {
            case "shootMotif":
                codeToRun = () -> Artemis.INSTANCE.shootMotif().schedule();
                break;
            case "intake":
                codeToRun = () -> Artemis.INSTANCE.intake().schedule();
                break;
            case "idle":
                codeToRun = () -> Artemis.INSTANCE.stopIntake().schedule();
                break;
        }

        if (codeToRun == null) {
            throw (new NullPointerException("Command not found! Make sure all your commands are in the callback class."));
        }

        return codeToRun;
    }
}
