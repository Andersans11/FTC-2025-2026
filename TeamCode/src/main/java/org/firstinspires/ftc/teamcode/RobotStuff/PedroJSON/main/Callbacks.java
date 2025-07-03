package org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.main;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Callbacks {

    OpMode opmode;

    public Callbacks(OpMode opmode) { //Feel free to add any dependencies for your subsystems into this class. be sure to add them to the constructor as well!
        this.opmode = opmode; // Opmode is added here as it is a common argument needed for subsystems. This can be removed if you never use it.
    }

    public Runnable GetCallback(String identifier) {

        Runnable codeToRun = () -> {
            //Maybe add a default command idk
        };

        switch (identifier) {
            case "verticalLiftTo10": //Example case
                codeToRun = () -> {
                    // Code goes here
                };
                break;
            case "closeClaw": //Example case
                codeToRun = () -> {
                    // Code goes here
                };
                break;
            case "horizontalLiftTo25": //Example case
                codeToRun = () -> {
                    // Code goes here
                };
                break;
        }

        return codeToRun;
    }

}
