package org.firstinspires.ftc.teamcode.enceladus.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.firstinspires.ftc.teamcode.enceladus.RobotStuff.Config.Subconfigs.RobotConfig;

import java.util.ArrayList;

public class Intake extends Subsystem {

    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    public Servo LeftI, RightI, stopper;
    public ArrayList<Servo> Servos = new ArrayList<>();
    public MotorEx intakeMotor;

    public Command store(float Float){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, 0, this),
                new SetPower(intakeMotor, 0.4, this)
        );
    }

    public Command idle(float Float){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, 0, this),
                new SetPower(intakeMotor, 0.0, this)
        );
    }

    public Command store(){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, 0, this),
                new SetPower(intakeMotor, 0.4, this)
        );
    }

    public Command intake(float Float){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, (double) 1/7, this),
                new SetPower(intakeMotor, 1, this)
        );
    }

    public Command intake() {
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, (double) 1/7, this),
                new SetPower(intakeMotor, 1, this)
        );
    }

    public Command outtake(){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, (double) 1/7, this),
                new SetPower(intakeMotor, -1, this)
        );
    }

    public void initSystem(RobotConfig robotConfig) {
        LeftI = robotConfig.LeftIntake.servo;
        RightI = robotConfig.RightIntake.servo;
        intakeMotor = robotConfig.IntakeMotor.motor;
        stopper = robotConfig.Stopper.servo;
        Servos.add(LeftI);
        Servos.add(RightI);
    }

    public Command OpenStopper() {
        return new ServoToPosition(
                stopper,
                0,
                this
        );
    }

    public Command CloseStopper() {
        return new ServoToPosition(
                stopper,
                0.5,
                this
        );
    }
}