package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.NewMagazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.NewTurret;

@TeleOp(name = "Reset Encoders", group = Utils.UTILITY)
public class Reset_Encoders extends RoyallyFuckedUpMode {

    public Reset_Encoders() {
        addSubsystemComponents(
                new BetterSubsystemComponent(NewMagazine.INSTANCE),
                new BetterSubsystemComponent(NewTurret.INSTANCE)
        );
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }
}
