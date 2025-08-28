package org.firstinspires.ftc.teamcode.enceladus.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DriveModes.WheelTestMode;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Wheel Test", group = OpModeGroups.UTILITY) // this is for making sure there is no resistance with the sides of the robot
//@Disabled
public class WheelTest extends NextFTCOpMode {
    public WheelTest() {super();}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    RobotConfig robotConfig;
    WheelTestMode wheelTestMode;
    @Override public void onInit() {
        robotConfig = new RobotConfig(this);
        wheelTestMode = new WheelTestMode(this, robotConfig);
    }
    @Override public void onStartButtonPressed() {
        wheelTestMode.Start();
    }
    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        wheelTestMode.updateDrive(deltaTimeNano);
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        robotConfig.gamepadUpdates();
    }
}