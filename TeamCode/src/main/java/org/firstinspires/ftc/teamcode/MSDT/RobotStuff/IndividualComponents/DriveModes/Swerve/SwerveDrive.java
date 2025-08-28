package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DriveModes.Swerve;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Config.Subconfigs.RobotConfig;
import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Misc.DeltaTimer;
import org.firstinspires.ftc.teamcode.MSDT.RobotStuff.Misc.VectorStuff;

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

            // once we have definite values for these, convert them to radians and use those values instead of calling Math.toRadians
            Vector2D forwardVector = VectorStuff.VectorFromPolar(forward * MAX_POWER, Math.toRadians(0.0));
            Vector2D strafeVector = VectorStuff.VectorFromPolar(strafe * MAX_POWER, Math.toRadians(90.0));
            Vector2D FLHeadingVector = VectorStuff.VectorFromPolar(heading * MAX_POWER, Math.toRadians(45.0));
            Vector2D BLHeadingVector = VectorStuff.VectorFromPolar(heading * MAX_POWER, Math.toRadians(135.0));
            Vector2D FRHeadingVector = VectorStuff.VectorFromPolar(heading * MAX_POWER, Math.toRadians(225.0));
            Vector2D BRHeadingVector = VectorStuff.VectorFromPolar(heading * MAX_POWER, Math.toRadians(315.0));

            FLHeadingVector = new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, FLHeadingVector);
            BLHeadingVector = new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, BLHeadingVector);
            FRHeadingVector = new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, FRHeadingVector);
            BRHeadingVector = new Vector2D(1.0, forwardVector, 1.0, strafeVector, 1.0, BRHeadingVector);

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
