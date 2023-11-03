/* Authors: Arin Khare, Kai Vernooy
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


    public enum AllianceColor {BLUE, RED};
    public enum StartingSide {LEFT, RIGHT};
    public enum ParkingPosition {LEFT, RIGHT, MIDDLE};
    public Robot robot;
    public AllianceColor allianceColor;
    public StartingSide startingSide;

    public MechanismDriving mechanismDriving;
    public Navigation navigation;
    public ComputerVision computerVision;

    protected GamepadWrapper gamepads;
    public ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    /**
     * makes robotmanager with subcomponents it's managing
     * @param hardwareMap
     * @param gamepad1
     * @param gamepad2
     * @param path
     * @param allianceColor
     * @param startingSide
     * @param movementMode
     * @param telemetry
     * @param elapsedTime
     */
    public RobotManager(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2,
                        ArrayList<Position> path, AllianceColor allianceColor, StartingSide startingSide,
                        Navigation.MovementMode movementMode, Telemetry telemetry, ElapsedTime elapsedTime) {

        this.elapsedTime = elapsedTime;
        this.allianceColor = allianceColor;
        this.startingSide = startingSide;

        elapsedTime.reset();
        robot = new Robot(hardwareMap, telemetry, elapsedTime);
        robot.telemetry.addData("auton path", path.size());
        navigation = new Navigation(path, allianceColor, startingSide, movementMode);
        mechanismDriving = new MechanismDriving();

        computerVision = new ComputerVision(hardwareMap, robot.telemetry, elapsedTime);

        gamepads = new GamepadWrapper(gamepad1, gamepad2);
        gamepads.updatePrevious();


    }

    /** Determine new robot desired states based on controller input (checks for button releases)
     */
    public void readControllerInputs() {
        if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_RETRACTED)) {
                Robot.desiredSlideState = Robot.SlidesState.RETRACTED;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_LOW)) {
            Robot.desiredSlidesState = Robot.SlidesState.LOW;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_MEDIUM)) {
            Robot.desiredSlidesState = Robot.SlidesState.MEDIUM;
        }
        else if (gamepads.getButtonRelease(GamepadWrapper.DriverAction.SET_SLIDES_HIGH)) {
            Robot.desiredSlidesState = Robot.SlidesState.HIGH;
        }
        else if (gamepads.gamepad2.left_stick_y > RobotManager.JOYSTICK_DEAD_ZONE_SIZE) {
            Robot.desiredSlidesState = Robot.SlidesState.MOVE_DOWN;
        }
        else if (gamepads.gamepad2.left_stick_y < -RobotManager.JOYSTICK_DEAD_ZONE_SIZE) {
            Robot.desiredSlidesState = Robot.SlidesState.MOVE_UP;
        } else if (Robot.desiredSlidesState == Robot.SlidesState.MOVE_DOWN || Robot.desiredSlidesState == Robot.SlidesState.MOVE_UP) {
            Robot.desiredSlidesState = Robot.SlidesState.STOPPED;
        }
        gamepads.updatePrevious();
    };

    public void driveMechanisms(RobotManager robotManager) {
        mechanismDriving.updateCompartments(robot);
        mechanismDriving.updateSlides(robot);
        mechanismDriving.updatePlaneSpring(robot);
        mechanismDriving.updateFuzzyMotor(robot);

    }
    public void moveRobot() {
        navigation.updateStrafePower(gamepads, robot);
        navigation.moveStraight(gamepads, robot);
        navigation.moveJoystick(gamepads, robot);
    }

}
