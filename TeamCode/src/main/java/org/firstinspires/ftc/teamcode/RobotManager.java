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

    // Controls dead zone of joystick and triggers so that actions don't accidently fire
    static final double JOYSTICK_DEAD_ZONE_SIZE = 0.05;
    static final double TRIGGER_DEAD_ZONE_SIZE = 0.05;

    public enum AllianceColor {BLUE, RED};
    public enum StartingSide {LEFT, RIGHT};
    public enum ParkingPosition {LEFT, RIGHT, MIDDLE};
    public Robot robot;
    public AllianceColor allianceColor;
    public StartingSide startingSide;

    public MechanismDriving mechanismDriving;
    public Navigation navigation;
    public ComputerVision computerVision;

    protected GamepadWrapper gamepads, previousStateGamepads;
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
        previousStateGamepads = new GamepadWrapper();
        previousStateGamepads.copyGamepads(gamepads);


    }

    /** Determine new robot desired states based on controller input (checks for button releases)
     */
    public void readControllerInputs() {
    }

    public void driveMechanisms(RobotManager robotManager) {
        mechanismDriving.updateCompartments(robot);
        mechanismDriving.updateSlides(robot);
        mechanismDriving.updatePlaneSpring(robot);
        mechanismDriving.updateFuzzyMotor(robot);

    }
    public void maneuver() {
        navigation.updateStrafePower(hasMovementDirection(), gamepads, robot);

        // Only move if one of the D-Pad buttons are pressed or the joystick is not centered.
        boolean movedStraight = navigation.moveStraight(
                gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_FORWARD),
                gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_BACKWARD),
                gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_LEFT),
                gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_RIGHT),
                robot
        );
        if (!movedStraight) {
            navigation.maneuver(gamepads,
                    gamepads.getAnalogValues(),
                    robot);
        }
    }

    public boolean hasMovementDirection() {
        boolean dpadPressed = (gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_FORWARD)
                || gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_BACKWARD)
                || gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_LEFT)
                || gamepads.getButtonState(GamepadWrapper.DriverAction.MOVE_STRAIGHT_RIGHT));
        double stickDist = Math.sqrt(
                Math.pow(gamepads.getAnalogValues().gamepad1LeftStickY,2)
                        + Math.pow(gamepads.getAnalogValues().gamepad1LeftStickX,2));
        boolean joystickMoved = stickDist >= RobotManager.JOYSTICK_DEAD_ZONE_SIZE;
        return dpadPressed || joystickMoved;
    }
}
