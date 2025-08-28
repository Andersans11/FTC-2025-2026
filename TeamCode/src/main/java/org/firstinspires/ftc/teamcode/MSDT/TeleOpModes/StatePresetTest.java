package org.firstinspires.ftc.teamcode.MSDT.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.AllPresets.Presets.CombinedPresets;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.AllPresets.Presets.HorizontalLiftPresets;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents.Intake;
import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Misc.DeltaTimer;

@TeleOp(name = "State Preset Test", group = OpModeGroups.TESTING)
//@Disabled
public class StatePresetTest extends NextFTCOpMode {

    public StatePresetTest() {
        super(HorizontalLift.INSTANCE, CombinedPresets.INSTANCE, Intake.INSTANCE, HorizontalLiftPresets.INSTANCE);
    }
    DeltaTimer deltaTimer = new DeltaTimer();
    long deltaTimeNano;

    RobotConfig robotConfig;
    RobotCentricDrive robotCentricDrive;

    @Override
    public void onInit() {
        robotConfig = new RobotConfig(this);
        robotCentricDrive = new RobotCentricDrive(this, robotConfig);
        HorizontalLift.INSTANCE.initSystem(robotConfig);
        Intake.INSTANCE.initSystem(robotConfig);
    }

    @Override
    public void onStartButtonPressed() {
        robotCentricDrive.Start();

        robotConfig.playerTwo.DpadUp.setPressedCommand(CombinedPresets.INSTANCE::maximum);
        robotConfig.playerTwo.DpadDown.setPressedCommand(CombinedPresets.INSTANCE::minimum);

        robotConfig.playerTwo.LeftTrigger.setPressedCommand(CombinedPresets.INSTANCE::intakeSequencePressedCommand);
        robotConfig.playerTwo.LeftTrigger.setReleasedCommand(CombinedPresets.INSTANCE::intakeSequenceReleasedCommand);

        robotConfig.playerTwo.RightTrigger.setPressedCommand(CombinedPresets.INSTANCE::intakeSequenceOuttakePressedCommand);
        robotConfig.playerTwo.RightTrigger.setReleasedCommand(CombinedPresets.INSTANCE::intakeSequenceOuttakeReleasedCommand);
    }



    @Override
    public void onUpdate() {

        deltaTimeNano = deltaTimer.getDelta();
        telemetry.addData("deltaTime", deltaTimeNano / Math.pow(10.0, 9));
        telemetry.addData("l servo pos", HorizontalLift.INSTANCE.leftServo.getPosition());
        telemetry.addData("r servo pos", HorizontalLift.INSTANCE.rightServo.getPosition());
        telemetry.addData("intake Servo Pos: ", Intake.INSTANCE.LeftI.getPosition());
        robotCentricDrive.updateDrive(deltaTimeNano);

        telemetry.update();

        robotConfig.gamepadUpdates();
    }
}


