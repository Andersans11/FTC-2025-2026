package org.firstinspires.ftc.teamcode.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RoyallyFuckedUpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.RobotStuff.Perseus;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.BetterSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine.Magazine;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;

import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Reset Encoders")
public class Reset_Encoders extends NextFTCOpMode {
    Magazine magazine;
    Turret turret;

    @Override
    public void onInit() {
        magazine = new Magazine();
        turret = new Turret();

        RobotConfig.initConfig(this, new DeltaTimer());

        magazine.hardware();
        turret.hardware();

        magazine.resetEncoders();
        turret.resetEncoders();
    }
}
