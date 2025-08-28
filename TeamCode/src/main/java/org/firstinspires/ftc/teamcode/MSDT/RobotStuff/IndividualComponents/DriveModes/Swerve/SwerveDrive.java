package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes.Swerve;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Config.Subconfigs.RobotConfig;

public class SwerveDrive extends Subsystem {

    VectorStuff vectorStuff = new VectorStuff();

    public SwervePod[] pods = new SwervePod[4];

    public static final SwerveDrive INSTANCE = new SwerveDrive();

    private SwerveDrive() { }

    public void initSystem(RobotConfig robotConfig) {
        pods[0] = new SwervePod(robotConfig.SwFLDrive.motor, robotConfig.SwFLPivot.servo, robotConfig.SwFLEnc);
        pods[1] = new SwervePod(robotConfig.SwBLDrive.motor, robotConfig.SwBLPivot.servo, robotConfig.SwBLEnc);
        pods[2] = new SwervePod(robotConfig.SwFRDrive.motor, robotConfig.SwFRPivot.servo, robotConfig.SwFREnc);
        pods[3] = new SwervePod(robotConfig.SwBRDrive.motor, robotConfig.SwBRPivot.servo, robotConfig.SwBREnc);
    }
    public Command update(float forward, float strafe, float heading) {
        Vector2D forwardVector = vectorStuff.VectorFromPolar(forward, Math.toRadians(0.0));
        Vector2D strafeVector = vectorStuff.VectorFromPolar(strafe, Math.toRadians(90.0));
        Vector2D FLHeadingVector = vectorStuff.VectorFromPolar(heading, Math.toRadians(45.0));
        Vector2D BLHeadingVector = vectorStuff.VectorFromPolar(heading, Math.toRadians(135.0));
        Vector2D FRHeadingVector = vectorStuff.VectorFromPolar(heading, Math.toRadians(225.0));
        Vector2D BRHeadingVector = vectorStuff.VectorFromPolar(heading, Math.toRadians(315.0));

        return new ParallelGroup(
                pods[0].update(new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, FLHeadingVector)),
                pods[1].update(new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, BLHeadingVector)),
                pods[2].update(new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, FRHeadingVector)),
                pods[3].update(new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, BRHeadingVector))
        );
    }
}
