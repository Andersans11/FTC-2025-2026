package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.SetPower;

import org.firstinspires.ftc.robotcontroller.external.samples.RobotTeleopPOV_Linear;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs.RobotConfig;

import java.util.ArrayList;

public class Intake extends Subsystem {

    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    public Servo LeftI, RightI;
    public ArrayList<Servo> Servos;
    RobotConfig robotConfig;
    public MotorEx intakeMotor;

    public Command store(){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, 0, this),
                new SetPower(intakeMotor, 0.1, this)
        );
    }
    public Command intake(){
        return new SequentialGroup(
                new MultipleServosToPosition(Servos, (double) 1/6, this),
                new SetPower(intakeMotor, 1, this)
        );
    }

    public void initSystem(RobotConfig robotConfig) {
        this.robotConfig = robotConfig;
        initialize();
    }

    @Override
    public void initialize() {
        LeftI = robotConfig.LeftIntake.servo;
        RightI = robotConfig.RightIntake.servo;
        intakeMotor = robotConfig.IntakeMotor.motor;
        Servos.add(LeftI);
        Servos.add(RightI);
    }
}
