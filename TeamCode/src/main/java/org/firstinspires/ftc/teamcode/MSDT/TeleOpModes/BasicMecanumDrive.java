package org.firstinspires.ftc.teamcode.MSDT.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Basic Mecanum Drive")
public class BasicMecanumDrive extends NextFTCOpMode {

    public BasicMecanumDrive() {
        addComponents(
                BindingsComponent.INSTANCE
        );
    }

    private final MotorEx frontLeftMotor = new MotorEx("Front Left Drive").reversed();
    private final MotorEx frontRightMotor = new MotorEx("Front Right Drive");
    private final MotorEx backLeftMotor = new MotorEx("Back Left Drive").reversed();
    private final MotorEx backRightMotor = new MotorEx("Back Right Drive");

    @Override
    public void onStartButtonPressed() {
        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
        driverControlled.schedule();
    }
}
