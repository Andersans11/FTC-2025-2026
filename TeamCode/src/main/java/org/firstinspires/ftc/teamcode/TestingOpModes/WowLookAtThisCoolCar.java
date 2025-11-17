package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RCMyFollowersGaveMeACar;

@TeleOp(name = "vroom", group = Utils.OpModeGroups.TESTING)
public class WowLookAtThisCoolCar extends RoyallyFuckedUpMode {

    public WowLookAtThisCoolCar() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RCMyFollowersGaveMeACar.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(RCMyFollowersGaveMeACar.INSTANCE.setHoldPos());
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
