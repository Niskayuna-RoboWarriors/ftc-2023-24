package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;


/** Keeps track of the robot's desired path and makes it follow it accurately.
 */
public class NavigationTeleOp extends BaseNavigation{
    public enum rotationDirection {CLOCKWISE, COUNTERCLOCKWISE};


    //Makes actions of the Robot that can be used anywhere within the folder.


    //Movements within the robot Autonomous Mode
    public final double STRAFE_ACCELERATION = 0.1; //Number indicated Inches per second squared
    public final double ROTATE_ACCELERATION = 0.1; //Number indicated Radians per second squared
    public final double SPEED_FACTOR = 0.7; //Speed of the robot when all motors are set to full power
    static final double STRAFE_RAMP_DISTANCE = 4; //Number indicated Inches
    //static final double ROTATION_RAMP_DISTANCE = Math.PI / 2; //NOT BEING USED
    static final double MIN_STRAFE_POWER = 0.3; //Sets the strafe power to 3/10th power
    static final double MAX_STRAFE_POWER = 0.5; //Sets the strafe power to 5/10th power
    static final double STRAFE_CORRECITON_POWER = 0.3; //idk what this means
    static final double STRAFE_SLOW = 0.1; //idk what this means
    static final double MAX_ROTATION_POWER = 0.3; //sets the rotation power to 3/10th, power
    static final double MIN_ROTATION_POWER = 0.03; //sets the rotation power to 3/100th power (why?)
    static final double ROTATION_CORRECTION_POWER = 0.04; //idk what this means

    // Accepted amounts of deviation between the robot's desired position and actual position.
    static final double EPSILON_ANGLE = 0.35;
    //   static final int NUM_CHECK_FRAMES = 5; //The number of frames to wait after a rotate or travelLinear call in order to check for movement from momentum.

    //Distances between where the robot extends/retracts the linear slides and where it opens the claw.
    static final double ROTATION_TIME = 1050; //???
    static final double FLOAT_EPSILON = 0.001; //????
    // The number of frames to wait after a rotate or travelLinear call in order to check for movement from momentum.
    static final int NUM_CHECK_FRAMES = 5;
    static final double JOYSTICK_DEAD_ZONE_SIZE = 0.08; //Sets the joystick deadzone to 0.08.
    static final double EPSILON_LOC = 10;


    //**TELEOP CONSTANTS**
    static final double MOVEMENT_MAX_POWER = 1; //Sets the maximum power to full power. (Full power is between 0 - 1)
    static final double STRAIGHT_MOVEMENT_POWER = 1;
    static final double ROTATION_POWER = 1; //Sets the maximum rotation power 1/2 full power
    static final double REDUCED_ROTATION_POWER = 0.2; //Lets the minimum rotation power to 1/5th full power
    static final double SLOW_MOVEMENT_SCALE_FACTOR = 0.3; //idk what this means
    static final double MEDIUM_MOVEMENT_SCALE_FACTOR = 0.6; //idk what this means


    //**INSTANCE ATTRIBUTES**//
        static public double[] wheel_speeds = {1, 1, 0.5, 1}; //Front Right, Rear Right, Front Left, Rear Left.
    public double strafePower; //This is for Tele-Op ONLY.

    /*
     First position in this ArrayList is the first position that robot is planning to go to.
     This condition must be maintained (positions should be deleted as the robot travels)
     NOTE: a position is both a location and a rotation.
     NOTE: this can be changed to a stack later if appropriate (not necessary for speed, just correctness).
     */
    public ArrayList<Position> path; //List of positions that the robot will go into WHEN IT IS IN AUTOMOTOUS MODE.
    public int pathIndex; //Index of the path array list.

    /**
     */
    public NavigationTeleOp() {
    }

    /** Updates the strafe power according to movement mode and gamepad 1 left trigger.
     * |Teleop| |Non Blocking|
     * @param gamepads
     * @param robot
     */
    public void updateStrafePower(GamepadWrapper gamepads, Robot robot) {

        AnalogValues analogValues = gamepads.getAnalogValues();
        //Limits the output values between 0 - 1. 0 = no power, 1 = full power
        double distance = Range.clip(Math.hypot(analogValues.gamepad1LeftStickX, analogValues.gamepad1LeftStickY), 0, 1);


        if (distance <= JOYSTICK_DEAD_ZONE_SIZE) {
            strafePower = 0; //Set as 0.3 (3/10th full power)
        } else {
            strafePower = 0.5 * MOVEMENT_MAX_POWER; //Set as 1 (full power)
        }
        //Pre-sets robot slide states at what speed.
        if (robot.desiredSlideState == Robot.SlideState.HIGH && robot.slides.getPower() == 0) {
            strafePower *= SLOW_MOVEMENT_SCALE_FACTOR; //Set as o.3
        } else if (robot.desiredSlideState == Robot.SlideState.MEDIUM && robot.slides.getPower() == 0) {
            strafePower *= SLOW_MOVEMENT_SCALE_FACTOR; //Set as o.3
        } else if (robot.desiredSlideState == Robot.SlideState.LOW && robot.slides.getPower() == 0) {
            strafePower *= SLOW_MOVEMENT_SCALE_FACTOR; //Set as o.3
        }
    }

    /**
     * DEGREE      ALT + 2 4 8
     * Moves the robot straight in one of the cardinal directions or at a 45 degree angle.
     * NOTE: ALL CONTROLLER MOVEMENTS ARE USING A PS5 CONTROLLER.
     * |Teleop| |Non Blocking|
     *

     * @param robot
     * @return whether any of the DPAD buttons were pressed
     */
    public void moveStraight(GamepadWrapper gamepads, Robot robot) {
        double direction;
        if (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_FORWARD) || gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_BACKWARD)) {
            if (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_LEFT)) {//moves left at 45° (or Northwest)
                direction = Math.PI * -0.75;
            } else if (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_RIGHT)) { //moves right at 45° (or Northeast)
                direction = Math.PI * -0.25;
            } else {//moving forward
                direction = Math.PI * -0.5;
            }
            if (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_BACKWARD)) { //invert the forward to just backwards
                direction = Math.PI * 0.5;
            }
            setDriveMotorPowers(direction, STRAIGHT_MOVEMENT_POWER, 0.0, robot, false);
        } else if (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_LEFT)) { //default direction. Set as 0
            direction = Math.PI;
            setDriveMotorPowers(direction, STRAIGHT_MOVEMENT_POWER, 0.0, robot, false);
        } else if (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_RIGHT)) {
            direction = 0;
            setDriveMotorPowers(direction, STRAIGHT_MOVEMENT_POWER, 0.0, robot, false);
        }

    }

    /** Changes drivetrain motor inputs based off the controller inputs
     * |Teleop| |Non Blocking|
     * @param gamepads
     * @param robot
     */
    public void moveJoystick(GamepadWrapper gamepads, Robot robot) {
        //Uses left joystick to go forward, and right joystick to turn.
        // NOTE: right-side drivetrain motor inputs don't have to be negated because their directions will be reversed
        //       upon initialization.

        double turn = -gamepads.gamepad1.right_stick_x; //turning was in the wrong direction, so negative sign
        double rotationPower = ROTATION_POWER;
        if (Math.abs(turn) < JOYSTICK_DEAD_ZONE_SIZE) {
            turn = 0;
        }
        if (gamepads.getButtonState(GamepadWrapper.DriverAction.REDUCED_CLOCKWISE)) {
            rotationPower = REDUCED_ROTATION_POWER;
            turn = -1;
        }
        if (gamepads.getButtonState(GamepadWrapper.DriverAction.REDUCED_COUNTER_CLOCKWISE)) {
            rotationPower = REDUCED_ROTATION_POWER;
            turn = -1;
        }
        double moveDirection = Math.atan2(gamepads.gamepad1.left_stick_y, gamepads.gamepad1.left_stick_x);
        if (Math.abs(moveDirection) < Math.PI / 12) {
            moveDirection = 0;
        } else if (Math.abs(moveDirection - Math.PI / 2) < Math.PI / 12) {
            moveDirection = Math.PI / 2;
        } else if (Math.abs(moveDirection - Math.PI) % Math.PI < Math.PI / 12) {
            moveDirection = Math.PI;
        } else if (Math.abs(moveDirection + Math.PI / 2) < Math.PI / 12) {
            moveDirection = -Math.PI / 2;
        } else {
            moveDirection = moveDirection;
        }

        setDriveMotorPowers(moveDirection, strafePower, turn * rotationPower, robot, false);
    }

    /** Sets drive motor powers to make the robot move a certain way.
     *
     *  @param strafeDirection the direction in which the robot should strafe.
     *  @param power the speed at which the robot should strafe. Must be in the interval [-1, 1]. Set this to zero if
     *               you only want the robot to rotate.
     *  @param turn the speed at which the robot should rotate (clockwise). Must be in the interval [-1, 1]. Set this to
     *              zero if you only want the robot to strafe.
     */
    static public void setDriveMotorPowers(double strafeDirection, double power, double turn, Robot robot, boolean debug) {
        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        robot.telemetry.addData("turn %.2f", turn);
        robot.telemetry.addData("current strafe direction", strafeDirection);
        if (Math.abs(power - 0) < FLOAT_EPSILON && Math.abs(turn - 0) < FLOAT_EPSILON) {
            stopMovement(robot);
            robot.telemetry.addData("stopping", "YES");
        } else {
            robot.telemetry.addData("stopping", "NO");
        }
        double sinMoveDirection = Math.sin(strafeDirection);
        double cosMoveDirection = Math.cos(strafeDirection);

        double powerSet1 = sinMoveDirection + cosMoveDirection;
        double powerSet2 = sinMoveDirection - cosMoveDirection;
        double[] rawPowers = scaleRange(powerSet1, powerSet2);

        //robot.telemetry.addData("Front Motors", "left (%.2f), right (%.2f)",
        //        (rawPowers[0] * power + turn) * wheel_speeds[2], (rawPowers[1] * power - turn) * wheel_speeds[3]);
        //robot.telemetry.addData("Rear Motors", "left (%.2f), right (%.2f)",
        //        (rawPowers[1] * power + turn) * wheel_speeds[0], (rawPowers[0] * power - turn) * wheel_speeds[1]);


        /*This sets the drive motor powers. This gives power to the wheels.
         * due to the fact that the operation of mechinum wheels requires is a bit different from normal wheels this is required for normal operation
         * first opposite wheels are given a specific base power calculated based on the direction the robot wants to move in
         * then the total power [0,1] is applied to the power value to set the over all robot speed.
         * then the turn factor is combined with the speed for robot turing
         * finally the motor power is adjusted by a ration that individual to each wheel in case of one wheel having outsized influence on the movement of the robot for some hardware reason.
         */
        robot.telemetry.addData("front right power", (rawPowers[1] * power - turn) * wheel_speeds[0]);
        robot.telemetry.addData("rear right power", (rawPowers[0] * power - turn) * wheel_speeds[1]);
        robot.telemetry.addData("front left power", (rawPowers[0] * power + turn) * wheel_speeds[2]);
        robot.telemetry.addData("rear left power", (rawPowers[1] * power + turn) * wheel_speeds[3]);


        robot.frontRight.setPower((rawPowers[1] * power - turn) * wheel_speeds[0]); //Turns the front right wheel
        robot.rearRight.setPower((rawPowers[0] * power - turn) * wheel_speeds[1]); //Turns the back right wheel
        robot.frontLeft.setPower((rawPowers[0] * power + turn) * wheel_speeds[2]); //Turns the left front wheel
        robot.rearLeft.setPower((rawPowers[1] * power + turn) * wheel_speeds[3]); //Turns the left back wheel
    }

    /**
     * @param robot stopping the robot movement.
     */
    static public void stopMovement(Robot robot) {
        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.frontLeft.setPower(0.0);
        robot.frontRight.setPower(0.0);
        robot.rearLeft.setPower(0.0);
        robot.rearRight.setPower(0.0);
    }

    /**preserves the ratio between a and b while restricting them to the range [-1, 1]
     * @param a value to be scaled
     * @param b value to be scaled
     * @return an array containing the scaled versions of a and b
     */

    public static double[] scaleRange(double a, double b) {
        double max = Math.max(Math.abs(a), Math.abs(b));
        return new double[] {a / max, b / max};
    }

    /** This function here references the NavagationAuton.java files and configs them both together for it to work properly.
     *
     * @param robot
     */
    public void NavAutonRef(Robot robot){

    }


}
//Coders: Tyler, Oscar
//Alumni Help: Stephen D.
