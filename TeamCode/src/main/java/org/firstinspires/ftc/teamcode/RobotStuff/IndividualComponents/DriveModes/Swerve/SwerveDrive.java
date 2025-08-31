package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents.DriveModes.Swerve;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Misc.DeltaTimer;

public class SwerveDrive {

    public static double MAX_POWER = 1;

    public DeltaTimer deltatime;

    public SwervePod[] pods = new SwervePod[4];

    public double[] rollingtime = new double[10];

    public int writing = 0;

    public SwerveDrive(RobotConfig robotConfig) {
        pods[0] = new SwervePod(robotConfig.SwFLDrive.motor, robotConfig.SwFLPivot.servo, robotConfig.SwFLEnc);
        pods[1] = new SwervePod(robotConfig.SwBLDrive.motor, robotConfig.SwBLPivot.servo, robotConfig.SwBLEnc);
        pods[2] = new SwervePod(robotConfig.SwFRDrive.motor, robotConfig.SwFRPivot.servo, robotConfig.SwFREnc);
        pods[3] = new SwervePod(robotConfig.SwBRDrive.motor, robotConfig.SwBRPivot.servo, robotConfig.SwBREnc);
        deltatime = new DeltaTimer();
    }

    public void init() {
        pods[0].init();
        pods[1].init();
        pods[2].init();
        pods[3].init();
    }

    public void initLoop() {
        pods[0].initLoop();
        pods[1].initLoop();
        pods[2].initLoop();
        pods[3].initLoop();
    }

    public void update(float forward, float strafe, float heading) {
        if (forward != 0 || strafe != 0 || heading != 0) {
            deltatime.reset();

            SwerveVector FLForwardVector = new SwerveVector(forward * MAX_POWER, Math.toRadians(0.0));
            SwerveVector BLForwardVector = new SwerveVector(forward * MAX_POWER, Math.toRadians(0.0));
            SwerveVector FRForwardVector = new SwerveVector(forward * MAX_POWER, Math.toRadians(0.0));
            SwerveVector BRForwardVector = new SwerveVector(forward * MAX_POWER, Math.toRadians(0.0));
            SwerveVector FLStrafeVector = new SwerveVector(strafe * MAX_POWER, Math.toRadians(90.0));
            SwerveVector BLStrafeVector = new SwerveVector(strafe * MAX_POWER, Math.toRadians(90.0));
            SwerveVector FRStrafeVector = new SwerveVector(strafe * MAX_POWER, Math.toRadians(90.0));
            SwerveVector BRStrafeVector = new SwerveVector(strafe * MAX_POWER, Math.toRadians(90.0));
            SwerveVector FLHeadingVector = new SwerveVector(heading * MAX_POWER, Math.toRadians(45.0));
            SwerveVector BLHeadingVector = new SwerveVector(heading * MAX_POWER, Math.toRadians(135.0));
            SwerveVector FRHeadingVector = new SwerveVector(heading * MAX_POWER, Math.toRadians(225.0));
            SwerveVector BRHeadingVector = new SwerveVector(heading * MAX_POWER, Math.toRadians(315.0));

            FLHeadingVector = new SwerveVector(1.0, FLForwardVector, 1.0, FLStrafeVector, 1.0, FLHeadingVector);
            BLHeadingVector = new SwerveVector(1.0, BLForwardVector, 1.0, BLStrafeVector, 1.0, BLHeadingVector);
            FRHeadingVector = new SwerveVector(1.0, FRForwardVector, 1.0, FRStrafeVector, 1.0, FRHeadingVector);
            BRHeadingVector = new SwerveVector(1.0, BRForwardVector, 1.0, BRStrafeVector, 1.0, BRHeadingVector);

            pods[0].update(FLHeadingVector);
            pods[1].update(BLHeadingVector);
            pods[2].update(FRHeadingVector);
            pods[3].update(BRHeadingVector);

            rollingtime[writing] = deltatime.getTimeMilli();
            writing ++;
            deltatime.reset();
        }
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addLine("Pod 0");
        telemetry.addData("   Power: ", pods[0].getAmp());
        telemetry.addData("   Direction: ", pods[0].getTheta());
        telemetry.addLine("Pod 1");
        telemetry.addData("   Power: ", pods[1].getAmp());
        telemetry.addData("   Direction: ", pods[1].getTheta());
        telemetry.addLine("Pod 2");
        telemetry.addData("   Power: ", pods[2].getAmp());
        telemetry.addData("   Direction: ", pods[2].getTheta());
        telemetry.addLine("Pod 3");
        telemetry.addData("   Power: ", pods[3].getAmp());
        telemetry.addData("   Direction: ", pods[3].getTheta());

        telemetry.addData("rolling deltatime: ", (rollingtime[0] + rollingtime[1] + rollingtime[2] + rollingtime[3] + rollingtime[4] + rollingtime[5] + rollingtime[6] + rollingtime[7] + rollingtime[8] + rollingtime[9]) / 10);
    }
}
