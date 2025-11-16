package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.DriveModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Sensitivities;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


@Configurable
public class RCStrikeAPose extends AbstractDriveMode {

    public static final RCStrikeAPose INSTANCE = new RCStrikeAPose();
    GoBildaPinpointDriver pinpoint;
    ControlSystem xController;
    ControlSystem yController;
    ControlSystem yawController;

    public static double kPx = 0.0;
    public static double kIx = 0.0;
    public static double kDx = 0.0;
    public static double kPy = 0.0;
    public static double kIy = 0.0;
    public static double kDy = 0.0;
    public static double kPh = 0.0;
    public static double kIh = 0.0;
    public static double kDh = 0.0;
    public static double goToX = 0;
    public static double goToY = 0;
    public static double goToHeading = 0;

    @Override
    public void initSystem() {
        xController = ControlSystem.builder()
                .posPid(kPx, kIx, kDx).build();
        yController = ControlSystem.builder()
                .posPid(kPy, kIy, kDy).build();
        yawController = ControlSystem.builder()
                .angular(AngleType.RADIANS,
                        feedback -> feedback.posPid(kPh, kIh, kDh)
                )
                .build();
    }

    @Override
    public void preStart() {
        xController.setGoal(new KineticState(goToX));
        yController.setGoal(new KineticState(goToY));
        yawController.setGoal(new KineticState(goToHeading));

        CommandManager.INSTANCE.scheduleCommand(this.vroom());
    }

    public MecanumDriverControlled vroom() {
        return new MecanumDriverControlled(
                FL, FR, BL, BR,
                () -> (yController.calculate()),
                () -> (xController.calculate()),
                () -> (yawController.calculate())
        );
    }

    public Command updatePos() {
        return new InstantCommand(() -> {
            xController.setGoal(new KineticState(goToX));
            yController.setGoal(new KineticState(goToY));
            yawController.setGoal(new KineticState(goToHeading));
        });
    }

    public void runTelemetry() {
        telemetry.addData("pose x (mm)", pinpoint.getPosX(DistanceUnit.MM));
        telemetry.addData("pose y (mm)", pinpoint.getPosY(DistanceUnit.MM));
        telemetry.addData("heading (rad)", pinpoint.getHeading(AngleUnit.RADIANS));
    }
}
