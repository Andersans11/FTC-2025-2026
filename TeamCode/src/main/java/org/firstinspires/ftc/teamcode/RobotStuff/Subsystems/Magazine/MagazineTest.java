package org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.Magazine;

import org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs.RTPAxon;
import org.firstinspires.ftc.teamcode.RobotStuff.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.RobotStuff.Subsystems.IBetterSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;

public class MagazineTest implements IBetterSubsystem {

    public static final MagazineTest INSTANCE = new MagazineTest();
    RTPAxon[] servos;

    @Override
    public void hardware() {
        this.servos = new RTPAxon[] {
                new RTPAxon(RobotConfig.CarouselCR1),
                new RTPAxon(RobotConfig.CarouselCR2),
                new RTPAxon(RobotConfig.CarouselCR3)
        };
    }

    @Override
    public void binds() {}

    @Override
    public void initialize() {}

    @Override
    public void commands() {}

    public Command setPower(double value) {
        servos[0].setPower(value);
        servos[1].setPower(value);
        servos[2].setPower(value);

        return new NullCommand();
    }

    @Override
    public void periodic() {}
}
