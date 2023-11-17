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
    public static final Map<Robot.SlideState, Integer> slidePositions = new HashMap<Robot.SlideState, Integer>() {{
       put(Robot.SlideState.RETRACTED, 0);
       put(Robot.SlideState.LOW, 700);
       put(Robot.SlideState.MEDIUM, 1850);
       put(Robot.SlideState.HIGH, 3500);
    }};
    double slideRampDownDist=1000, maxSpeedCoefficient=0.8;
    public static final int EPSILON = 150;  // slide encoder position tolerance;

    // Compartment variables
    static final double COMPARTMENT_CLOSED_POS = 0;
    static final double COMPARTMENT_OPEN_POS = 0.25;
    static final double COMPARTMENT_SERVO_TIME = 500;

    // Plane spring variables
    static final double PLANE_SPRING_UNRELEASED_POS = 0;
    static final double PLANE_SPRING_RELEASED_POS = 0.25;
    static final double PLANE_SPRING_SERVO_TIME = 500;

    // Intake motor
    static final double INTAKE_MOTOR_SPEED = 1;


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

    /**
     * Block execution until left comparmtment is opened
     * @param robot
     */
    public void openLeftCompartment(Robot robot) {
        robot.desiredCompartmentLeftState = Robot.CompartmentState.Open;
        double startingTime = robot.elapsedTime.milliseconds();
        updateCompartmentLeft(robot);
        while (robot.elapsedTime.milliseconds() - startingTime < COMPARTMENT_SERVO_TIME) {}
    }
    /**
     * Block execution until left comparmtment is closed
     * @param robot
     */
    public void closeLeftCompartment(Robot robot) {
        robot.desiredCompartmentLeftState = Robot.CompartmentState.Closed;
        double startingTime = robot.elapsedTime.milliseconds();
        updateCompartmentLeft(robot);
        while (robot.elapsedTime.milliseconds() - startingTime < COMPARTMENT_SERVO_TIME) {}
    }

    /**
     * Block execution until right comparmtment is opened
     * @param robot
     */
    public void openRightCompartment(Robot robot) {
        robot.desiredCompartmentRightState = Robot.CompartmentState.Open;
        double startingTime = robot.elapsedTime.milliseconds();
        updateCompartmentRight(robot);
        while (robot.elapsedTime.milliseconds() - startingTime < COMPARTMENT_SERVO_TIME) {}
    }
    /**
     * Block execution until right comparmtment is closed
     * @param robot
     */
    public void closeRightCompartment(Robot robot) {
        robot.desiredCompartmentRightState = Robot.CompartmentState.Closed;
        double startingTime = robot.elapsedTime.milliseconds();
        updateCompartmentRight(robot);
        while (robot.elapsedTime.milliseconds() - startingTime < COMPARTMENT_SERVO_TIME) {}
    }

    /** Sets slide motor powers to move in direction of desired position, if necessary.
     *
     * @return whether the slides are in the desired position.
     */
    public boolean updateSlides(Robot robot) {
        if (robot.desiredSlideState != Robot.SlideState.UNREADY) {
            robot.desiredSlidePosition = slidePositions.get(robot.desiredSlideState);

            double mainSpeed; // "ramp" the motor speeds down based on how far away from the destination the motors are
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

    /**
     * Block execution until slides reach target state
     * @param robot
     * @param targetSlideState
     */
    public void moveSlides(Robot robot, Robot.SlideState targetSlideState) {
        if (targetSlideState == Robot.SlideState.UNREADY) { return; }
        robot.desiredSlideState = targetSlideState;
        while (updateSlides(robot) != true) {};
    }

    /**
     * Updates plane spring
     * @param robot
     */
    public void updatePlaneSpring(Robot robot) {
        robot.telemetry.addData("UPDATE PLANE SPRING MOTOR STATE", robot.desiredPlaneStringState);
        switch (robot.desiredPlaneSpringState) {
            case UNRELEASED:
                robot.planeSpring.setPosition(PLANE_SPRING_UNRELEASED_POS);
                break;
            case RELEASED:
                robot.planeSpring.setPosition(PLANE_SPRING_RELEASED_POS);
                break;
        }
        robot.telemetry.addData("SET PLANE SPRING MOTOR POSITION", robot.planeSpring.getPosition());
    }

    /**
     * Block execution until right comparmtment is opened
     * @param robot
     */
    public void releasePlaneSpring(Robot robot) {
        robot.desiredPlaneSpringState = Robot.PlaneSpringState.RELEASED;
        double startingTime = robot.elapsedTime.milliseconds();
        updatePlaneSpring(robot);
        while (robot.elapsedTime.milliseconds() - startingTime < PLANE_SPRING_SERVO_TIME) {}
    }

    /**
     * Block execution until plane spring is unreleased. NOTE: *NO GUARANTEE THAT UNRELEASING A RELEASED SPRING WILL WORK PROPERLY*
     * @param robot
     */
    public void unreleasePlaneSpring(Robot robot) {
        robot.desiredPlaneSpringState = Robot.PlaneSpringState.UNRELEASED;
        double startingTime = robot.elapsedTime.milliseconds();
        updatePlaneSpring(robot);
        while (robot.elapsedTime.milliseconds() - startingTime < PLANE_SPRING_SERVO_TIME) {}
    }

    /**
     * Updates intake motor.
     * @param robot
     */
    public void updateIntakeMotor(Robot robot) {
        robot.telemetry.addData("UPDATE INTAKE MOTOR STATE", robot.desiredIntakeMotorState);
        switch (robot.desiredIntakeMotorState) {
            case OFF:
                robot.intakeMotor.setPower(0);
                break;
            case ON:
                robot.intakeMotor.setPower(INTAKE_MOTOR_SPEED);
                break;
        }
        robot.telemetry.addData("SET INTAKE MOTOR POWER", robot.intakeMotor.getPower());
    }

    /**
     * Turns on intake motor.
     * @param robot
     */
    public turnOnIntakeMotor(Robot robot) {
        robot.desiredIntakeMotorState = Robot.IntakeMotorState.ON;
        updateIntakeMotor();
    }

    /**
     * Turns off intake motor.
     * @param robot
     */
    public turnOffIntakeMotor(Robt robot) {
        robot.desiredIntakeMotorState = Robot.IntakeMotorState.OFF;
        updateIntakeMotor();
    }

}

