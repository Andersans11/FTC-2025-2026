package org.firstinspires.ftc.teamcode.RobotStuff.Config.Subconfigs;

public abstract class BaseRobotState {

    public BaseRobotState(String name) {

    }

    public abstract void activate();

    /*
    in a extending state class, this would rebind commands.

    for example, if the state we are entering (calling state.activate())
    is the DepositSampleToBucket state, the joystick that might control vertical lift movement
    now moves the lift between two set positions of high and low basket

    same for a DepositSpecToChamber state, vertical lift joystick now moves between high and low chamber positions.

    If there are two buttons that control high / low DEPOSIT AREA, these buttons might move to high / low basket in the DepositSampleToBucket state,
    but would move to high / low chamber in DepositSpecToChamberState.
     */

}
