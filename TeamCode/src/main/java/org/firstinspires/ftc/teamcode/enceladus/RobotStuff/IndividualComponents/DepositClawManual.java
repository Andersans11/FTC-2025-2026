package org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;

public class DepositClawManual extends Subsystem {
    public static final DepositClawManual INSTANCE = new DepositClawManual();
    private DepositClawManual() { } // nftc boilerplate\

    private enum ClawPos {
        CURRENT
    }

    public Servo wristServo, armServo, clawServo;

    double currentWristPos;
    double currentArmPos;

    public void initSystem(RobotConfig robotConfig) {
        wristServo = robotConfig.DepositWrist.servo;
        armServo = robotConfig.DepositArm.servo;
        clawServo = robotConfig.DepositClaw.servo;
    }

    double DegreesToPowerArm(double angleDegrees) {
        return angleDegrees / 270;
    }

    double DegreesToPowerWrist(double angleDegrees) {
        return angleDegrees / 355;
    }

    /**
     * Arguments are in Degrees.
     * For the Arm, 0 degrees is 10 degrees forward and 270 is 80 degrees backward.
     * For the wrist, 0 degrees is straight down and 355 is 5 degrees backward
     **/
    public Command SetPosition(double armPos, double wristPos) {
        return new ParallelGroup(
                new ServoToPosition(armServo, DegreesToPowerArm(armPos), this),
                new ServoToPosition(wristServo, DegreesToPowerWrist(wristPos), this)
        );
    }

    public Command Claw(boolean pos) {
        if (pos) {
            return new ServoToPosition(clawServo, 1, this);
        } else {
            return new ServoToPosition(clawServo, 0, this);
        }
    }
}
