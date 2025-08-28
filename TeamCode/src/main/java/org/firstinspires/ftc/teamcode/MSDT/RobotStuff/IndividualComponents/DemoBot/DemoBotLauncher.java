package org.firstinspires.ftc.teamcode.MSDT.RobotStuff.IndividualComponents.DemoBot;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.gamepad.Control;
import com.rowanmcalpin.nextftc.ftc.gamepad.Trigger;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.DemoRobotConfig;


public class DemoBotLauncher extends Subsystem {
    public static final DemoBotLauncher INSTANCE = new DemoBotLauncher();
    private DemoBotLauncher() { } // nftc boilerplate

    DemoRobotConfig robotConfig;

    MotorEx motor;

    public void initSystem(DemoRobotConfig robotConfig) {
        this.robotConfig = robotConfig;
        this.motor = robotConfig.LauncherMotor.motor;
        initialize();
    }

    public enum Mappings {
        SHOOT,
        OUTTAKE
    }

    Trigger SHOOT;
    Trigger OUTTAKE;

    public void map(Control control, Mappings mapping) {
        switch (mapping) {
            case SHOOT:
                if (control instanceof Trigger) {
                    this.SHOOT = SHOOT.getClass().cast(control);
                    SHOOT.setPressedCommand(INSTANCE::shoot);
                } else {
                    throw new IllegalArgumentException("SHOOT requires a " + SHOOT.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
            case OUTTAKE:
                if (control instanceof Trigger) {
                    this.OUTTAKE = OUTTAKE.getClass().cast(control);
                    OUTTAKE.setPressedCommand(INSTANCE::outtake);
                } else {
                    throw new IllegalArgumentException("OUTTAKE requires a " + OUTTAKE.getClass().getSimpleName() + ", but received a " + control.getClass().getSimpleName());
                }
                break;
        }
    }

    public Command shoot(float input) {
        return new SetPower(
                motor,
                1 * input,
                this
        );
    }

    public Command outtake(float input) {
        return new SetPower(
                motor,
                -1 * input,
                this
        );
    }

}
