package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.firstinspires.ftc.robotcontroller.external.samples.RobotTeleopPOV_Linear;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import java.util.ArrayList;

public class Intake extends IntakeInternal {

    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    public void initSystem(RobotConfig robotConfig) {
        this.leftServo = robotConfig.LeftIntake.servo;
        this.rightServo = robotConfig.RightIntake.servo;
        this.intakeMotor = robotConfig.IntakeMotor.motor;

        this.servos.add(leftServo);
        this.servos.add(rightServo);
    }

    public void Start() {
        targetPos = 0.0;
    }
    public static double intakePos = (double) 1/6;
    public static double storePos = 0.0;
    public static double intakePower = 1;
    public static double idlePower = 0.4;
    public static double outtakePower = -1;


    public enum IntakePositions {
        INTAKE,
        STORE
    }
    public enum IntakePowers {
        IDLE,
        INTAKE,
        OUTTAKE,
    }

    public Command setTargetPosition(IntakePositions Preset) { // set target pos via preset value
        switch (Preset) {
            case STORE:
                targetPos = storePos;  break;

            case INTAKE:
                targetPos = intakePos;

        }
        return new NullCommand();
    }

    public Command setPower(IntakePowers Power) {
        Command returnCommand;
        switch (Power) {
            case INTAKE:
                returnCommand = new SetPower(
                        intakeMotor,
                        intakePower,
                        this
                ); break;
            case IDLE:
                returnCommand = new SetPower(
                    intakeMotor,
                    idlePower,
                    this
                ); break;
            case OUTTAKE:
                returnCommand =  new SetPower(
                        intakeMotor,
                        outtakePower,
                        this
                ); break;
            default:
                returnCommand = new NullCommand();
        }
        return returnCommand;
    }
}

abstract class IntakeInternal extends Subsystem {

    public Servo leftServo, rightServo;
    MotorEx intakeMotor;

    ArrayList<Servo> servos = new ArrayList<>();

    double targetPos = 0.0;
    double oldPos = 0.1; // triggers default command

    @NonNull
    @Override
    public Command getDefaultCommand() {
        if (targetPos != oldPos) {
            oldPos = targetPos;
            return new MultipleServosToPosition(
                    servos,
                    targetPos,
                    this
            );
        } else {
            return new NullCommand();
        }
    }

}