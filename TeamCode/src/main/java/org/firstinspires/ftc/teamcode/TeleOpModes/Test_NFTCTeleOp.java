package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.individual_components.VerticalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.misc.Stopwatch;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;

@TeleOp(name = "Lift Teleop", group = OpModeGroups.DONOTUSE)
@Disabled
public class Test_NFTCTeleOp extends NextFTCOpMode {

    public Test_NFTCTeleOp() {
        super(VerticalLift.INSTANCE);
    }
    private final ElapsedTime deltaTimer = new ElapsedTime();
    Stopwatch StopWatch = new Stopwatch();
    double deltaTime;
    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
        VerticalLift.INSTANCE.initialize();
        VerticalLift.INSTANCE.configure(robotConfig);
    }

    @Override
    public void onStartButtonPressed() {
        deltaTime = 0;

        robotCentricDrive.Start();

        robotConfig.playerOne.DpadUp.setPressedCommand(VerticalLift.INSTANCE::toHigh);
        robotConfig.playerOne.DpadDown.setPressedCommand(VerticalLift.INSTANCE::toLow);
        robotConfig.playerOne.DpadLeft.setPressedCommand(VerticalLift.INSTANCE::toMiddle);
        robotConfig.playerOne.Y.setPressedCommand(VerticalLift.INSTANCE::resetEncoders);
    }

    @Override
    public void onUpdate() {
        StopWatch.reset();

        deltaTime = deltaTimer.seconds();
        telemetry.addData("deltaTime", deltaTime);
        deltaTimer.reset();

        telemetry.addData("Lift current pos:", VerticalLift.INSTANCE.ticksToInches(VerticalLift.INSTANCE.motors.getLeader().getCurrentPosition(), 751.8));

        telemetry.update();

        robotConfig.gamepadUpdates();
    }
}
