package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;
import java.util.Map;

/** Controls motors and servos that are not involved in moving the robot around the field.
 */
public class MechanismDriving {
    static final double COMPARTMENT_CLOSED_POS = 0;
    static final double COMPARTMENT_OPEN_POS = 0.25;

    /**
     * Update left compartment
     * @param robot
     */
    public void updateCompartmentLeft(Robot robot) {
        robot.telemetry.addData("UPDATE COMPARTMENT LEFT SERVO STATE", robot.desiredCompartmentLeftState);
        switch (robot.desiredCompartmentLeftState) {
            case CLOSED:
                robot.compartment_left.setPosition(COMPARTMENT_CLOSED_POS);
                break;
            case OPEN:
                robot.compartment_left.setPosition(COMPARTMENT_OPEN_POS);
                break;
        }
        robot.telemetry.addData("SET COMPARTMENT LEFT SERVO POSITION", robot.compartment_left.getPosition());
    }

    /**
     * Update right compartment
     * @param robot
     */
    public void updateCompartmentRight(Robot robot) {
        robot.telemetry.addData("UPDATE COMPARTMENT RIGHT SERVO STATE", robot.desiredCompartmentRightState);
        switch (robot.desiredCompartmentRightState) {
            case CLOSED:
                robot.compartment_right.setPosition(COMPARTMENT_CLOSED_POS);
                break;
            case OPEN:
                robot.compartment_right.setPosition(COMPARTMENT_OPEN_POS);
                break;
        }
        robot.telemetry.addData("SET COMPARTMENT RIGHT SERVO POSITION", robot.compartment_right.getPosition());
    }

    /**
     * Update both compartments
     * @param robot
     */
    public void updateCompartments(Robot robot) {
        updateCompartmentRight(robot);
        updateCompartmentLeft(robot);
    }

}

