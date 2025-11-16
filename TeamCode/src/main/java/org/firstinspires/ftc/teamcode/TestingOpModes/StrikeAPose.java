package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RCStrikeAPose;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;

@TeleOp(name = "strike a pose", group = Utils.TESTING)
public class StrikeAPose extends RoyallyFuckedUpMode {

    public StrikeAPose() {
        super();
        addSubsystemComponents(
                new BetterSubsystemComponent(RCStrikeAPose.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();

        P1.triangle().whenBecomesTrue(RCStrikeAPose.INSTANCE.updatePos());
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        RCStrikeAPose.INSTANCE.runTelemetry();
    }
}
