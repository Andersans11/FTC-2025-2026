package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

@TeleOp(name = "Basic Mecanum Drive", group = Utils.WORKING)
public class BasicMecanumDrive extends RoyallyFuckedUpMode {

    public BasicMecanumDrive() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
