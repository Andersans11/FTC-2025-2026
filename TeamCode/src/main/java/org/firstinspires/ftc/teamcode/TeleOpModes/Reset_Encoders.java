package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robocol.RobocolConfig;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Reset Encoders", group = Utils.UTILITY)
public class Reset_Encoders extends RRoboticsOpMode {

    public Reset_Encoders() {}

    GoBildaPinpointDriver pinpoint;

    @Override
    public void onInit() {
        super.onInit();
        pinpoint = RobotConfig.Pinpoint;
        for (DcMotor motor: hardwareMap.dcMotor){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        stop();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
        new SequentialGroupFixed(
                new InstantCommand(() -> pinpoint.resetPosAndIMU()),
                new Delay(0.5),
                new InstantCommand(() -> pinpoint.recalibrateIMU()),
                new Delay(1),
                new InstantCommand(() -> addLine("done!"))
        ).schedule();
    }
}
