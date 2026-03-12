package org.firstinspires.ftc.teamcode.RobotStuff.Misc;

import java.util.stream.Collectors;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.CommandGroup;

public class SequentialGroupFixed extends CommandGroup {
    public SequentialGroupFixed(Command... commands) {
        super(commands);
        named(String.format("SequentialGroupFixed(%s)", getChildren().stream().map(Command::name).collect(Collectors.joining(", "))));
    }

    @Override
    public boolean isDone() {
        return getChildren().isEmpty();
    }

    public void add(Command command) {
        getChildren().add(command);
    }

    @Override
    public void start() {
        getChildren().first().start();
    }

    @Override
    public void update() {
        if (getChildren().isEmpty()) {
            return;
        }

        getChildren().first().update();

        if (!getChildren().first().isDone()) return;

        getChildren().removeFirst().stop(false);

        if (!getChildren().isEmpty()) getChildren().first().start();
    }

    @Override
    public void stop(boolean interrupted) {
        if (!getChildren().isEmpty()) getChildren().first().stop(interrupted);

        super.stop(interrupted);
    }
}
