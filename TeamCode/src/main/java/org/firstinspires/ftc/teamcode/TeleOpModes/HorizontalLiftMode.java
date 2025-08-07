package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import org.firstinspires.ftc.teamcode.RobotStuff.AllPresets.Presets.HorizontalLiftPresets;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.OpModeGroups;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.HorizontalLift;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.Intake;
import org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.VerticalLiftManual;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

@TeleOp(name = "Horizontal + Intake", group = OpModeGroups.TESTING)
//@Disabled
public class HorizontalLiftMode extends NextFTCOpMode {

    public HorizontalLiftMode() {
        super(HorizontalLift.INSTANCE, HorizontalLiftPresets.INSTANCE, Intake.INSTANCE, VerticalLiftManual.INSTANCE);
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
        VerticalLiftManual.INSTANCE.initSystem(robotConfig);
    }

    @Override
    public void onStartButtonPressed() {
        robotCentricDrive.Start();

        robotConfig.playerOne.DpadUp.setPressedCommand(HorizontalLiftPresets.INSTANCE::maximum);
        robotConfig.playerOne.DpadDown.setPressedCommand(HorizontalLiftPresets.INSTANCE::minimum);

        robotConfig.playerOne.RightTrigger.setPressedCommand(Intake.INSTANCE::intake);
        robotConfig.playerOne.RightTrigger.setReleasedCommand(Intake.INSTANCE::store);

        robotConfig.playerTwo.DpadUp.setPressedCommand(VerticalLiftManual.INSTANCE::MoveUp);
        robotConfig.playerTwo.DpadDown.setPressedCommand(VerticalLiftManual.INSTANCE::MoveDown);

        robotConfig.playerTwo.DpadUp.setReleasedCommand(VerticalLiftManual.INSTANCE::Stop);
        robotConfig.playerTwo.DpadDown.setReleasedCommand(VerticalLiftManual.INSTANCE::Stop);
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


