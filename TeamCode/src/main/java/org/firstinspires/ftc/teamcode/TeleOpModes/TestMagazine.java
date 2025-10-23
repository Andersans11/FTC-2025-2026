package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;

@TeleOp(name = "Test Magazine", group = Utils.TESTING)
public class TestMagazine extends RoyallyFuckedUpMode {

    public TestMagazine() {
        super();
        addComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(Magazine.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        RobotConfig.bind(P1.a(), "MAGAZINE_SLOT1");
        RobotConfig.bind(P1.b(), "MAGAZINE_SLOT2");
        RobotConfig.bind(P1.x(), "MAGAZINE_SLOT3");

        driveTrainBinds();
    }

    @Override
    public void onStartButtonPressed() {}

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
