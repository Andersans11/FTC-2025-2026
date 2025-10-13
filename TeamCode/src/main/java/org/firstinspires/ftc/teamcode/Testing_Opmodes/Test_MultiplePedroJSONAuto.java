package org.firstinspires.ftc.teamcode.Testing_Opmodes;

import android.telecom.Call;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.FConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants.LConstants;
import org.firstinspires.ftc.teamcode.RobotStuff.PedroJSON.Callbacks;

import java.io.File;

import PedroJSON.main.PathLoader;
import dev.nextftc.ftc.NextFTCOpMode;

@Autonomous(name = "Test: PedroJSON Auto with Multiple PathLoaders")
public class Test_MultiplePedroJSONAuto extends NextFTCOpMode {
    PathLoader base, outcome1, outcome2;
    Follower follower;
    Callbacks callbacks;
    boolean doOutcome1;
    boolean doOutcome2;

    @Override
    public void onInit() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        callbacks = new Callbacks(this);

        base = new PathLoader(new File(), follower, this, callbacks);
        outcome1 = new PathLoader(new File(), follower, this, callbacks);
        outcome2 = new PathLoader(new File(), follower, this, callbacks);

        base.Parse();
        outcome1.Parse();
        outcome2.Parse();

        base.Init();
        outcome1.Init();
        outcome2.Init();
    }

    @Override
    public void onWaitForStart() {
        if (gamepad1.dpad_left) doOutcome1 = true;
        if (gamepad1.dpad_right) doOutcome2 = true;

        base.Start();
        outcome1.Start();
        outcome2.Start();
    }

    @Override
    public void onStartButtonPressed() {

    }
}
