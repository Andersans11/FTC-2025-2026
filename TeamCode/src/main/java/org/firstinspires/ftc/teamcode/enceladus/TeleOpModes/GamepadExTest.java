package org.firstinspires.ftc.teamcode.enceladus.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DeltaTimer;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Gamepad Test", group = OpModeGroups.UTILITY)
//@Disabled
public class GamepadExTest extends NextFTCOpMode {

    public GamepadExTest() {super();}

    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;
    RobotConfig robotConfig;

    @Override public void onInit() {
        robotConfig = new RobotConfig(this);
    }

    @Override public void onUpdate() {
        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addData("deltaTime", TimeUnit.SECONDS.convert(deltaTimeNano, TimeUnit.NANOSECONDS));

        telemetry.addData("A button", robotConfig.playerOne.A.getState());
        telemetry.addData("B button", robotConfig.playerOne.B.getState());
        telemetry.addData("X button", robotConfig.playerOne.X.getState());
        telemetry.addData("Y button", robotConfig.playerOne.Y.getState());
        telemetry.addData("Triangle button", robotConfig.playerOne.Triangle.getState());
        telemetry.addData("Square button", robotConfig.playerOne.Square.getState());
        telemetry.addData("Cross button", robotConfig.playerOne.Cross.getState());
        telemetry.addData("Circle button", robotConfig.playerOne.Circle.getState());

        robotConfig.gamepadUpdates();
    }
}
