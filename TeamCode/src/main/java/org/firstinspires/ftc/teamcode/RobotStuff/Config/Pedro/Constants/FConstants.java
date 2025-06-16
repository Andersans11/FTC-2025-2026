package org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.PINPOINT;

        FollowerConstants.leftFrontMotorName = RobotConfig.FLDrive.name;
        FollowerConstants.leftRearMotorName = RobotConfig.BLDrive.name;
        FollowerConstants.rightFrontMotorName = RobotConfig.FRDrive.name;
        FollowerConstants.rightRearMotorName = RobotConfig.BRDrive.name;

        FollowerConstants.leftFrontMotorDirection = RobotConfig.FLDrive.direction;
        FollowerConstants.leftRearMotorDirection = RobotConfig.BLDrive.direction;
        FollowerConstants.rightFrontMotorDirection = RobotConfig.FRDrive.direction;
        FollowerConstants.rightRearMotorDirection = RobotConfig.BRDrive.direction;

        FollowerConstants.mass = 12.25;

        FollowerConstants.xMovement = 58.9265;
        FollowerConstants.yMovement = 48.8137;

        FollowerConstants.forwardZeroPowerAcceleration = -37.8655;
        FollowerConstants.lateralZeroPowerAcceleration = -93.6652;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.15,0,0.01,0);
        FollowerConstants.useSecondaryTranslationalPID = true;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.15,0,0.007,0); // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(2,0,0.1,0);
        FollowerConstants.useSecondaryHeadingPID = true;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(2.5,0,0.05,0); // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.01,0,0.003,0,0);
        FollowerConstants.useSecondaryDrivePID = true;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.01,0,0.0003,0,0); // Not being used, @see useSecondaryDrivePID

        FollowerConstants.zeroPowerAccelerationMultiplier = 4.5;
        FollowerConstants.centripetalScaling = 0.00015;

        FollowerConstants.pathEndTimeoutConstraint = 500;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;
    }
}
