package org.firstinspires.ftc.teamcode.RobotStuff.IndividualComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

public class Intake extends Subsystem {

    public static final Intake INSTANCE = new Intake();
    private Intake() { }
    public Servo LeftI, RightI;
    public String LS = "Left Intake";
    public String RS = "Right Intake";

    public ServoToPosition store(){
        return new ServoToPosition(LeftI, 0, this);
    }
    public ServoToPosition intake(){
        return new ServoToPosition(LeftI, 1, this);
    }


    @Override
    public void initialize() {
        LeftI = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, LS);
        RightI = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, RS);
    }
}
