package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.PIDDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Stayputnik;

@TeleOp(name = "Fancy Mecanum Drive", group = Utils.WORKING)
public class StupidMecanumDrive extends RRoboticsOpMode { // jurnlgsjtgf

    public StupidMecanumDrive() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(PIDDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
    }


    @Override
    public void onStartButtonPressed() {
        P1.leftTrigger().atLeast(0.1).whenTrue(() -> PIDDrive.INSTANCE.slowMode = true);
        P1.leftTrigger().atLeast(0.1).whenFalse(() -> PIDDrive.INSTANCE.slowMode = false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
