/* Authors: James Lian, Tyler Montgomery, Jason Lian
 */

package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/** A completely encompassing class of all functionality of the robot. An OpMode should interface through an instance of
 *  this class in order to send or receive any data with the real robot.
 */
public class RobotManager {

    public Robot robot;

    public MechanismDriving mechanismDriving;
    public NavigationTeleOp navigation;
    public ComputerVision computerVision;

    protected GamepadWrapper gamepads;
    public ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    /**
     * makes robotmanager with subcomponents it's managing
     * @param hardwareMap
     * @param gamepad1
     * @param gamepad2
     * @param telemetry
     * @param elapsedTime
     */
    public RobotManager(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, ElapsedTime elapsedTime) {

        this.elapsedTime = elapsedTime;
        elapsedTime.reset();
        robot = new Robot(hardwareMap, telemetry, elapsedTime);
        navigation = new NavigationTeleOp();
        mechanismDriving = new MechanismDriving();

        // computerVision = new ComputerVision(hardwareMap, robot.telemetry, elapsedTime);

        gamepads = new GamepadWrapper(gamepad1, gamepad2);
        gamepads.updatePrevious();


    }

    /** Determine new robot desired states based on controller input (checks for button releases)
     */
    public void readControllerInputs() {
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_RETRACTED)) {
            robot.desiredSlideState = Robot.SlideState.RETRACTED;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_LOW)) {
            robot.desiredSlideState = Robot.SlideState.LOW;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_MEDIUM)) {
            robot.desiredSlideState = Robot.SlideState.MEDIUM;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_HIGH)) {
            robot.desiredSlideState = Robot.SlideState.HIGH;
        }
        else if (gamepads.gamepad2.left_stick_y > NavigationTeleOp.JOYSTICK_DEAD_ZONE_SIZE) {
            robot.desiredSlideState = Robot.SlideState.MOVE_DOWN;
        }
        else if (gamepads.gamepad2.left_stick_y < -NavigationTeleOp.JOYSTICK_DEAD_ZONE_SIZE) {
            robot.desiredSlideState = Robot.SlideState.MOVE_UP;
        }
        // Automatically set it to stopped if not actively being moved up or down
        else if (robot.desiredSlideState == Robot.SlideState.MOVE_DOWN || robot.desiredSlideState == Robot.SlideState.MOVE_UP) {
            robot.desiredSlideState = Robot.SlideState.STOPPED;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_RIGHT_BUCKET)) {
            if (robot.desiredCompartmentRightState == Robot.CompartmentState.CLOSED) {
                robot.desiredCompartmentRightState = Robot.CompartmentState.OPEN;
            }
            else {
                robot.desiredCompartmentRightState = Robot.CompartmentState.CLOSED;
            }
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_LEFT_BUCKET)) {
            if (robot.desiredCompartmentLeftState == Robot.CompartmentState.CLOSED) {
                robot.desiredCompartmentLeftState = Robot.CompartmentState.OPEN;
            }
            else {
                robot.desiredCompartmentLeftState = Robot.CompartmentState.CLOSED;
            }
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_INTAKE_MOTOR_ROTATION)) {
            if (robot.desiredIntakeMotorState == Robot.IntakeMotorState.OFF) {
                robot.desiredIntakeMotorState = Robot.IntakeMotorState.OFF;
            }
            else if (robot.desiredIntakeMotorState == Robot.IntakeMotorState.OUTTAKE) {
                robot.desiredIntakeMotorState = Robot.IntakeMotorState.OUTTAKE;
            }
            else {
                robot.desiredIntakeMotorState = Robot.IntakeMotorState.INTAKE;
            }
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.PLANE_RELEASE)) {
            if (robot.desiredPlaneSpringState == Robot.PlaneSpringState.UNRELEASED) {
                robot.desiredPlaneSpringState = Robot.PlaneSpringState.RELEASED;
            }
            else {
                robot.desiredPlaneSpringState = Robot.PlaneSpringState.UNRELEASED;
            }
        }
        gamepads.updatePrevious();
    };
    /** Wraps mechanism driving update functions
     */
    public void driveMechanisms() {
        mechanismDriving.updateCompartments(robot);
        mechanismDriving.updateSlides(robot);
        mechanismDriving.updatePlaneSpring(robot);
        mechanismDriving.updateIntakeMotor(robot);

    }
    public void moveRobot() {
        navigation.updateStrafePower(gamepads, robot);
        navigation.moveStraight(gamepads, robot);
        navigation.moveJoystick(gamepads, robot);
    }

}
