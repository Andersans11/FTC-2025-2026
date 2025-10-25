package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.MagazineTest;

@TeleOp(name = "Test: Manual Magazine")
public class TestMagazineManual extends RoyallyFuckedUpMode {
    public TestMagazineManual() {
        super();
        addComponents(
                new BetterSubsystemComponent(RobotCentricDrive.INSTANCE),
                new BetterSubsystemComponent(MagazineTest.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        driveTrainBinds();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

        MagazineTest.INSTANCE.setPower(0.5);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
