package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.HoldHeadingPinpoint;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

import java.util.concurrent.TimeUnit;


@TeleOp(name = "LockYaw Pinpoint", group = OpModeGroups.DONOTUSE) // pid go brrr
@Disabled
public class LockYawPinpoint extends NextFTCOpMode {

    public LockYawPinpoint() {
        super();
    }

    DeltaTimer deltaTimer = new DeltaTimer();

    RobotConfig robotConfig;
    HoldHeadingPinpoint holdHeadingPinpoint;

    long deltaTimeNano;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        holdHeadingPinpoint = new HoldHeadingPinpoint(this, robotConfig);

        setUseBulkReading(true);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }
    @Override
    public void onStartButtonPressed() {
        holdHeadingPinpoint.Start();
    }

    @Override
    public void onUpdate() {

        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        holdHeadingPinpoint.updateDrive(deltaTimeNano);
        robotConfig.gamepadUpdates();

        telemetry.update();
    }
}
