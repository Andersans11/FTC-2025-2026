package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.Pedro.Constants;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.commands.utility.InstantCommand;


@Configurable
public class RCMyFollowersGaveMeACar extends AbstractDriveMode {

    public static final RCMyFollowersGaveMeACar INSTANCE = new RCMyFollowersGaveMeACar();
    Follower follower;
    boolean isHolding = false;


    @Override
    public void initSystem() {
        follower = Constants.createFollower(RobotConfig.getHardwareMap());
        follower.setStartingPose(follower.getPose());
        follower.update();
    }

    @Override
    public void preStart() {
        CommandManager.INSTANCE.scheduleCommand(this.vroom());
    }


    public Command vroom() {
        return new InstantCommand(() -> follower.update());
    }

    public Command setHoldPos() {
        return new InstantCommand(() -> {
            if (!isHolding) {
                follower.holdPoint(follower.getPose());
            } else {
                follower.startTeleOpDrive();
            }
        });
    }
}
