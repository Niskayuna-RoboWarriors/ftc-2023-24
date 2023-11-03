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

    // Plane spring variables
    static final double PLANE_SPRING_UNRELEASED_POS = 0;
    static final double PLANE_SPRING_RELEASED_POS = 0.25;
    // Fuzzy motor
    static final double FUZZY_MOTOR_SPEED = 1;


    /**
     * Update left compartment
     * @param robot
     */
    public void updateCompartmentLeft(Robot robot) {
        robot.telemetry.addData("UPDATE COMPARTMENT LEFT SERVO STATE", robot.desiredCompartmentLeftState);
        switch (robot.desiredCompartmentLeftState) {
            case CLOSED:
                robot.compartmentLeft.setPosition(COMPARTMENT_CLOSED_POS);
                break;
            case OPEN:
                robot.compartmentLeft.setPosition(COMPARTMENT_OPEN_POS);
                break;
        }
        robot.telemetry.addData("SET COMPARTMENT LEFT SERVO POSITION", robot.compartmentLeft.getPosition());
    }

    /**
     * Update right compartment
     * @param robot
     */
    public void updateCompartmentRight(Robot robot) {
        robot.telemetry.addData("UPDATE COMPARTMENT RIGHT SERVO STATE", robot.desiredCompartmentRightState);
        switch (robot.desiredCompartmentRightState) {
            case CLOSED:
                robot.compartmentRight.setPosition(COMPARTMENT_CLOSED_POS);
                break;
            case OPEN:
                robot.compartmentRight.setPosition(COMPARTMENT_OPEN_POS);
                break;
        }
        robot.telemetry.addData("SET COMPARTMENT RIGHT SERVO POSITION", robot.compartmentRight.getPosition());
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

            double mainSpeed;//"ramp" the motor speeds down based on how far away from the destination the motors are
            mainSpeed = maxSpeedCoefficient * Range.clip(Math.abs(robot.desiredSlidePosition - robot.slides.getCurrentPosition())/slideRampDownDist, 0.1, 1);
            mainSpeed = Range.clip(mainSpeed, 0.4, 1);//limit the max speed to 1 and the min speed to 0.05

            // If the current position is less than desired position then move it up
            if (robot.desiredSlidePosition - robot.slides.getCurrentPosition() > EPSILON) {
                // Ensures that one motor does not go beyond the other too much
                robot.slides.setPo wer(mainSpeed);
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

    /**
     * Updates plane spring
     * @param robot
     */
    public void updatePlaneSpring(Robot robot) {
        robot.telemetry.addData("UPDATE PLANE SPRING MOTOR STATE");
        switch (robot.desiredPlaneStringState) {
            case UNRELEASED:
                robot.planeSpring.setPosition(PLANE_SPRING_UNRELEASED_POS);
            case RELEASED:
                robot.planeSpring.setPosition(PLANE_SPRING_RELEASED_POS);
        }
        robot.telemetry.addData("SET PLANE SPRING MOTOR POSITION", robot.planeSpring.getPosition());
    }

    /**
     * Updates fuzzy motor
     * @param robot
     */
    public void updateFuzzyMotor(Robot robot) {
        robot.telemtry.addData("UPDATE FUZZY MOTOR STATE");
        switch (robot.desiredFuzzyMotorState) {
            case OFF:
                robot.fuzzyMotor.setPower(0);
            case ON:
                robot.fuzzyMotor.setPower(FUZZY_MOTOR_SPEED);
        }
        robot.telemetry.addData("SET FUZZY MOTOR POWER", robot.fuzzyMotor.getPower());
    }

}

