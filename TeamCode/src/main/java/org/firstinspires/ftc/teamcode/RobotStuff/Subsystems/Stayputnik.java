package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.IRRoboticsSubsystem;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class Stayputnik implements IRRoboticsSubsystem {

    public static final Stayputnik INSTANCE = new Stayputnik();

    ControlSystem FLCont, FRCont, BLCont, BRCont;
    public static double kP = 0.001;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kS = 0.0;
    public static double kV = 0.00042;
    public static double kA = 0.0;
    public static double powerToVel = 500;
    MotorEx[] driveMotors;

    @Override
    public void preStart() {}

    @Override
    public void initSystem() {
        driveMotors = new MotorEx[] {
                RobotConfig.FLDrive.getMotor(),
                RobotConfig.FRDrive.getMotor(),
                RobotConfig.BLDrive.getMotor(),
                RobotConfig.BRDrive.getMotor()
        };

        FLCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
        FRCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
        BLCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
        BRCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
    }

    public void resetPID() {
        FLCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
        FRCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
        BLCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
        BRCont = ControlSystem.builder()
                .velPid(kP, kI, kD)
                .basicFF(kV, kA, kS)
                .build();
    }

    public Command setTarget(double targetVel) {
        return new InstantCommand(() -> {
            FLCont.setGoal(new KineticState(0.0, targetVel));
            FRCont.setGoal(new KineticState(0.0, targetVel));
            BLCont.setGoal(new KineticState(0.0, targetVel));
            BRCont.setGoal(new KineticState(0.0, targetVel));

        });
    }

    public Command setVector(double lateral, double forward, double rotation) {
        final double x = lateral * powerToVel;
        final double y = forward * powerToVel;
        final double rx = rotation * powerToVel;
        return new InstantCommand(() -> {
            FLCont.setGoal(new KineticState(0.0, y + x + rx));
            FRCont.setGoal(new KineticState(0.0, y - x + rx));
            BLCont.setGoal(new KineticState(0.0, y - x - rx));
            BRCont.setGoal(new KineticState(0.0, y + x - rx));

        });
    }

    @Override
    public void periodic() {
        driveMotors[0].setPower(FLCont.calculate());
        driveMotors[1].setPower(FRCont.calculate());
        driveMotors[2].setPower(BLCont.calculate());
        driveMotors[3].setPower(BRCont.calculate());
    }
}