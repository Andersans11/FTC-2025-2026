package org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
public class Constants {



    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(12.247)
            .forwardZeroPowerAcceleration(-28.4571)
            .lateralZeroPowerAcceleration(-70.8437)
            .useSecondaryHeadingPIDF(true)
            .centripetalScaling(0.0)
            .headingPIDFCoefficients(new PIDFCoefficients(2.5,0,0.1,0))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(2,0,0.1,0))
            .predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0.1, 0.16096089, 0.00152637));

    public static MecanumConstants driveConstants = new MecanumConstants()
            .leftFrontMotorName("BR")
            .leftRearMotorName("FR")
            .rightFrontMotorName("BL")
            .rightRearMotorName("FL")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .maxPower(1)
            .xVelocity(80.2034)
            .yVelocity(62.8311);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .hardwareMapName("PINPOINT")
            .forwardPodY(6.88976)
            .strafePodX(0)
            .distanceUnit(DistanceUnit.INCH)
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            250,
            0.25,
            1
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
