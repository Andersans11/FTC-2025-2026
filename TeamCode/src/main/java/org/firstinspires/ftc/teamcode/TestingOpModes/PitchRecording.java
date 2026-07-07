package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotStuff.Misc.SequentialGroupFixed;
import org.firstinspires.ftc.teamcode.RobotStuff.Selene;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsOpMode;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Utils;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RRoboticsSubsystemComponent;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes.RobotCentricDrive;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Shooter;

import dev.nextftc.core.commands.utility.InstantCommand;

@Configurable
@TeleOp(name = "Pitch Recording", group = Utils.PRIORITY_PRIORITY)
public class PitchRecording extends RRoboticsOpMode {

    public PitchRecording() {
        super();
        addSubsystemComponents(
                new RRoboticsSubsystemComponent(RobotCentricDrive.INSTANCE),
                new RRoboticsSubsystemComponent(Selene.INSTANCE)
        );
    }

    public static double shootPower = 700;
    public static double hoodPos = 62.5;
    public static double turretPos = 0;
    double oldHoodPos = 1;

    @Override
    public void onInit() {
        super.onInit();
        Selene.INSTANCE.initFollower(hardwareMap);

        P1.dpadUp().whenBecomesTrue(Turret.INSTANCE.setBlueAlliance());
        P1.dpadDown().whenBecomesTrue(Turret.INSTANCE.setRedAlliance());

        P2.dpadDown().whenBecomesTrue(Selene.INSTANCE.resetFollower());

        P1.square().whenBecomesTrue(new SequentialGroupFixed(
                Selene.INSTANCE.start(),
                Selene.INSTANCE.intake(),
                Shooter.INSTANCE.StopperOpen(),
                new InstantCommand(() -> Turret.INSTANCE.mode = Turret.TurretMode.TESTING)
        ));

        Selene.INSTANCE.indMode = Selene.IndicatorMode.INIT_DONE;
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        addData("speed", Shooter.INSTANCE.shooters.getVelocity());

        addData("distance", Selene.INSTANCE.currentPose.distanceFrom(Turret.INSTANCE.targetPose));

        addData("shootPower", Shooter.INSTANCE.controller.getGoal());

        addData("Hood", RobotConfig.HoodServo.getServo().getPosition());

        addData("mode", Turret.INSTANCE.mode);

        if (Shooter.INSTANCE.controller.getGoal().getVelocity() != shootPower) Shooter.INSTANCE.setGoal(shootPower).schedule();
        if (oldHoodPos != hoodPos) {
            Shooter.INSTANCE.setHoodPos(Turret.INSTANCE.angleToServoPower(hoodPos)).schedule();
            oldHoodPos = hoodPos;
        }
        if (Turret.INSTANCE.targetYaw != turretPos) Turret.INSTANCE.targetYaw = turretPos;
    }
}
