/* Authors: James Lian, Tyler Montgomery, Jason Lian
 */

package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    public NavigationAuton navigationAuton;
    public ComputerVision computerVision;

    protected GamepadWrapper gamepads;
    public ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public Thread computerVisionThread;
    public AtomicInteger pixelOffset;
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

        computerVision = new ComputerVision(hardwareMap, robot.telemetry, elapsedTime);

        gamepads = new GamepadWrapper(gamepad1, gamepad2);
        gamepads.updatePrevious();
        RobotManager temp = this;
        pixelOffset = new AtomicInteger(0);
    }

    /** Determine new robot desired states based on controller input (checks for button releases)
     */
    public void readControllerInputs() {
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.CHANGE_MOVEMENT_MODE)) {
            if (robot.movementMode == Robot.MovementMode.NORMAL) {
                robot.movementMode = Robot.MovementMode.ULTRA_FINE;
            } else {
                robot.movementMode = Robot.MovementMode.NORMAL;
            }
        }
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_AUTOPIXEL)) {
            if (robot.autoPixelState == Robot.AutoPixelState.OFF) {
                robot.autoPixelState = Robot.AutoPixelState.ON;
            } else {
                robot.autoPixelState = Robot.AutoPixelState.OFF;
            }
        }
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.CHANGE_CLAW_ROTATOR_POSITION)) {
            if (robot.desiredClawRotatorState == Robot.ClawRotatorState.DOWN) {
                robot.desiredClawRotatorState = Robot.ClawRotatorState.PARALLEL;
            } else {
                robot.desiredClawRotatorState = Robot.ClawRotatorState.DOWN;
            }
        }
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
        else if (Math.abs(gamepads.gamepad2.left_stick_y) > NavigationTeleOp.JOYSTICK_DEAD_ZONE_SIZE) {
            robot.desiredSlideState = Robot.SlideState.MOVE_ANALOG;
        }
        else if (Math.abs(gamepads.gamepad2.left_stick_y) <= NavigationTeleOp.JOYSTICK_DEAD_ZONE_SIZE) {
            robot.desiredSlideState = Robot.SlideState.STOPPED;
        }
//        // Automatically set it to stopped if not actively being moved up or down
//        else if (robot.desiredSlideState == Robot.SlideState.MOVE_DOWN || robot.desiredSlideState == Robot.SlideState.MOVE_UP) {
//            robot.desiredSlideState = Robot.SlideState.STOPPED;
//        }
//        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_RIGHT_BUCKET)) {
//            if (robot.desiredCompartmentRightState == Robot.CompartmentState.CLOSED) {
//                robot.desiredCompartmentRightState = Robot.CompartmentState.OPEN;
//            }
//            else {
//                robot.desiredCompartmentRightState = Robot.CompartmentState.CLOSED;
//            }
//        }
//        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_LEFT_BUCKET)) {
//            if (robot.desiredCompartmentLeftState == Robot.CompartmentState.CLOSED) {
//                robot.desiredCompartmentLeftState = Robot.CompartmentState.OPEN;
//            }
//            else {
//                robot.desiredCompartmentLeftState = Robot.CompartmentState.CLOSED;
//            }
//        }
//        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_INTAKE_MOTOR_ROTATION)) {
//            if (robot.desiredIntakeMotorState != Robot.IntakeMotorState.INTAKE) {
//                robot.desiredIntakeMotorState = Robot.IntakeMotorState.INTAKE;
//            }
//            else {
//                robot.desiredIntakeMotorState = Robot.IntakeMotorState.OFF;
//            }
//        }
//        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.TOGGLE_OUTTAKE_MOTOR_ROTATION)) {
//            if (robot.desiredIntakeMotorState != Robot.IntakeMotorState.OUTTAKE) {
//                robot.desiredIntakeMotorState = Robot.IntakeMotorState.OUTTAKE;
//            } else {
//                robot.desiredIntakeMotorState = Robot.IntakeMotorState.OFF;
//            }
//        }
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.PLANE_RELEASE)) {
            if (robot.desiredPlaneSpringState == Robot.PlaneSpringState.UNRELEASED) {
                robot.desiredPlaneSpringState = Robot.PlaneSpringState.RELEASED;
            }
            else {
                robot.desiredPlaneSpringState = Robot.PlaneSpringState.UNRELEASED;
            }
        }
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.OPEN_CLAW)) {
            robot.desiredClawState = Robot.ClawState.OPEN2;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.DROP_1_FROM_CLAW)) {
            switch (robot.desiredClawState) {
                case CLOSED:
                    robot.desiredClawState = Robot.ClawState.OPEN1;
                    break;
                case OPEN1:
                    robot.desiredClawState = Robot.ClawState.OPEN2;
                    break;
            }
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.CLOSE_CLAW)) {
            robot.desiredClawState = Robot.ClawState.CLOSED;
        }
//        else if (Math.abs(gamepads.gamepad2.right_stick_y) > NavigationTeleOp.JOYSTICK_DEAD_ZONE_SIZE) {
//            robot.desiredIntakeMotorState = Robot.IntakeMotorState.ANALOG;
//        }
        gamepads.updatePrevious();
    };
    /** Wraps mechanism driving update functions
     */
    public void driveMechanisms() {
        //mechanismDriving.updateCompartments(robot);
        mechanismDriving.updateSlides(gamepads, robot);
        mechanismDriving.updatePlaneSpring(robot);
//        mechanismDriving.updateIntakeMotor(gamepads, robot);
        mechanismDriving.updateClaw(robot);

    }
    public void moveRobot() {
        navigation.updatePixelOffset(pixelOffset.get(), robot);
        navigation.updateStrafePower(gamepads, robot);
        if (!navigation.moveStraight(gamepads, robot)) {
            navigation.moveJoystick(gamepads, robot);
        }
    }
}