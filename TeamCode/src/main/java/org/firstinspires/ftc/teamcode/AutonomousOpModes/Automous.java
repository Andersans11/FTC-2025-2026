package org.firstinspires.ftc.teamcode.AutonomousOpModes;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.pedrojson.Callbacks;

import PedroJSON.main.PathLoader;

@Autonomous(name = "Automous")
public class Automous extends RoyallyFuckedUpMode {
    PathLoader loader;
    Follower follower;
    Callbacks callbacks;

    int outcomeState = 0;

    public Automous() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(Perseus.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        follower = Constants.createFollower(hardwareMap);
        callbacks = new Callbacks(this);

        loader = new PathLoader("org/firstinspires/ftc/teamcode/RobotStuff/pedrojson/Data/Automous.json", follower, this, callbacks, 1);

        loader.Parse();
        Turret.INSTANCE.setPosition(90);
    }

    @Override
    public void onWaitForStart() {
        super.onWaitForStart();

        Turret.INSTANCE.periodic();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        switch (outcomeState) {
            case 0:
                loader.Update();
                if (loader.isComplete()) {
                    outcomeState = 1;
                }
                break;
            case 1:
                // maybe add a breaking function or smth idk
                break;
        }
    }
}
