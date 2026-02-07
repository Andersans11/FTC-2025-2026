package org.firstinspires.ftc.teamcode.RobotStuff.pedrojson;

import org.firstinspires.ftc.teamcode.RobotStuff.Selene;

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
                codeToRun = () -> Selene.INSTANCE.shootMotif().schedule();
                break;
            case "intake":
                codeToRun = () -> Selene.INSTANCE.intake().schedule();
                break;
            case "idle":
                codeToRun = () -> Selene.INSTANCE.stopIntake().schedule();
                break;
        }

        if (codeToRun == null) {
            throw (new NullPointerException("Command not found! Make sure all your commands are in the callback class."));
        }

        return codeToRun;
    }
}
