package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;
import java.util.Map;

/** Controls motors and servos that are not involved in moving the robot around the field.
 */
public class MechanismDriving {

    // Slide variables
    public static final int LOWERING_AMOUNT = 100;
    public static final Map<Robot.SlidesState, Integer> slidePositions = new HashMap<Robot.SlidesState, Integer>() {{
       put(Robot.SlidesState.RETRACTED, 0);
       put(Robot.SlidesState.LOW_LOWERED, slidePositions.get(Robot.SlidesState.LOW) - LOWERING_AMOUNT);
       put(Robot.SlidesState.LOW, 700);
       put(Robot.SlidesState.MEDIUM_LOWERED, slidePositions.get(Robot.SlidesState.MEDIUM) - LOWERING_AMOUNT);
       put(Robot.SlidesState.MEDIUM, 1850);
       put(Robot.SlidesState.HIGH_LOWERED, slidePositions.get(Robot.SlidesState.HIGH) - LOWERING_AMOUNT);
       put(Robot.SlidesState.HIGH, 3500);
    }};
    double slideRampDownDist=1000, maxSpeedCoefficient=0.8;
    public static final int EPSILON = 150;  // slide encoder position tolerance;

    // Compartment variables
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

    /** Sets slide motor powers to move in direction of desired position, if necessary.
     *
     * @return whether the slides are in the desired position.
     */
    public boolean updateSlides(Robot robot) {
        if (Robot.desiredSlidesState != Robot.SlidesState.UNREADY) {
            robot.desiredSlidePosition = slidePositions.get(robot.desiredSlidesState);

            double mainSpeed, reducedSpeed;//"ramp" the motor speeds down based on how far away from the destination the motors are
            mainSpeed = maxSpeedCoefficient * Range.clip(Math.abs(robot.desiredSlidePosition - robot.slides.getCurrentPosition())/slideRampDownDist, 0.1, 1);
            mainSpeed = Range.clip(mainSpeed, 0.4, 1);//limit the max speed to 1 and the min speed to 0.05

            // If the current position is less than desired position then move it up
            if (robot.desiredSlidePosition - robot.slides.getCurrentPosition() > EPSILON) {
                // Ensures that one motor does not go beyond the other too much
                robot.slides.setPower(mainSpeed);
            }

            // If the current position is above the desired position, move these downwards
            if (robot.slides.getCurrentPosition() - robot.desiredSlidePosition > EPSILON) {
                robot.slides.setPower(-mainSpeed);
            }

            // Stop motors when we have reached the desired position
            if (Math.abs(robot.slides.getCurrentPosition() - robot.desiredSlidePosition) < EPSILON) {
                robot.slides.setPower(0);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}

