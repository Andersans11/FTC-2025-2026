package org.firstinspires.ftc.teamcode.MSDT.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.AllPresets.Presets.CombinedPresets;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DepositClawManual;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DriveModes.HoldHeading;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.Intake;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.VerticalLiftPID;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DeltaTimer;

@TeleOp(name = "hehe active intake go brrr", group = OpModeGroups.WORKING)
//@Disabled
public class TheEverythingMode extends NextFTCOpMode {

    public TheEverythingMode() {
        super(HorizontalLift.INSTANCE, CombinedPresets.INSTANCE, Intake.INSTANCE, DepositClawManual.INSTANCE, VerticalLiftPID.INSTANCE);
    }
    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;

    RobotConfig robotConfig;
    HoldHeading lockYaw;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        lockYaw = new HoldHeading(this, robotConfig);
        HorizontalLift.INSTANCE.initSystem(robotConfig);
        Intake.INSTANCE.initSystem(robotConfig);
        DepositClawManual.INSTANCE.initSystem(robotConfig);
        VerticalLiftPID.INSTANCE.initSystem(robotConfig);
    }

    @Override
    public void onStartButtonPressed() {
        lockYaw.Start();

        robotConfig.playerOne.RightBumper.setPressedCommand(CombinedPresets.INSTANCE::HLiftChangePos);

        robotConfig.playerOne.RightTrigger.setPressedCommand(Intake.INSTANCE::intake);
        robotConfig.playerOne.RightTrigger.setReleasedCommand(Intake.INSTANCE::store);

        robotConfig.playerOne.DpadDown.setPressedCommand(Intake.INSTANCE::outtake);

        robotConfig.playerOne.A.setPressedCommand(CombinedPresets.INSTANCE::TransferPos);
        robotConfig.playerOne.B.setPressedCommand(CombinedPresets.INSTANCE::SpecimenCollectPos);
        robotConfig.playerOne.X.setPressedCommand(CombinedPresets.INSTANCE::SpecimenScorePos);
        robotConfig.playerOne.Y.setPressedCommand(CombinedPresets.INSTANCE::SampleScorePos);

        robotConfig.playerOne.LeftBumper.setPressedCommand(CombinedPresets.INSTANCE::Claw);

        robotConfig.playerTwo.DpadUp.setPressedCommand(this::LiftUp);
        robotConfig.playerTwo.DpadDown.setPressedCommand(this::LiftDown);
    }

    public Command LiftUp() {
        return VerticalLiftPID.INSTANCE.SetPosition(50.0);
    }

    public Command LiftDown() {
        return VerticalLiftPID.INSTANCE.SetPosition(0.0);
    }


    @Override
    public void onUpdate() {

        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addLine("This Royal Robotics OpMode was brought to you by Polymaker!");
        telemetry.addLine("Use code royalrobotics at checkout for 22% off!");
        telemetry.addData("deltaTime", deltaTimeNano / Math.pow(10.0, 9));
        lockYaw.updateDrive(deltaTimeNano);
        telemetry.addData("V-Lift Position:", VerticalLiftPID.INSTANCE.CurrentPosition());

        telemetry.update();

        robotConfig.gamepadUpdates();
    }
}


