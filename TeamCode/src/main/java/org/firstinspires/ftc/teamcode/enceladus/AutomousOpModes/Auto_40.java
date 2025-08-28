package org.firstinspires.ftc.teamcode.enceladus.AutomousOpModes;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.AllPresets.Presets.CombinedPresets;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Pedro.Constants.FConstants;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Pedro.Constants.LConstants;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DepositClawManual;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.Intake;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.VerticalLiftPID;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.PedroJSON.Callbacks;

import java.io.File;

import PedroJSON.main.PathLoader;

/*
A simple OpMode to test NFTC drive controls.
It pulls motors from the Motors Class and assigns them to a mecanum drive command.
*/

@Autonomous(name="4+0")
public class Auto_40 extends NextFTCOpMode {

    public Auto_40() {
        super(HorizontalLift.INSTANCE, CombinedPresets.INSTANCE, Intake.INSTANCE, DepositClawManual.INSTANCE, VerticalLiftPID.INSTANCE);
    }

    RobotConfig robotConfig = new RobotConfig(this);


    File routine = new File("TeamCode\\src\\main\\java\\org\\firstinspires\\ftc\\teamcode\\RobotStuff\\PedroJSON\\data\\4+0.json");
    Follower follower;
    PathLoader pathLoader;

    @Override
    public void onInit() {
        HorizontalLift.INSTANCE.initSystem(robotConfig);
        Intake.INSTANCE.initSystem(robotConfig);
        DepositClawManual.INSTANCE.initSystem(robotConfig);
        VerticalLiftPID.INSTANCE.initSystem(robotConfig);
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        Callbacks callbacks = new Callbacks();
        pathLoader = new PathLoader(routine, follower, this, callbacks);

        pathLoader.Parse();
        pathLoader.Init();

        CombinedPresets.INSTANCE.SpecimenCollectPos().invoke();
        Intake.INSTANCE.store().invoke();
        HorizontalLift.INSTANCE.setTargetPosition(HorizontalLift.LiftPreset.MINIMUM).invoke();
    }

    @Override
    public void onStartButtonPressed() {
        pathLoader.Start();
    }

    @Override
    public void onUpdate() {
        pathLoader.Update();
    }
}
