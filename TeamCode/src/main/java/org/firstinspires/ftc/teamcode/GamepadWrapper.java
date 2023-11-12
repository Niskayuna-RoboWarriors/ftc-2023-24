package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.sql.Driver;
import java.util.HashMap;

/** Wraps a gamepad so that button mappings are stored in one place.
 */
public class GamepadWrapper {
    public enum DriverAction {
        MOVE_STRAIGHT_FORWARD, MOVE_STRAIGHT_BACKWARD, MOVE_STRAIGHT_LEFT, MOVE_STRAIGHT_RIGHT,

        TOGGLE_INTAKE_MOTOR_ROTATION,

        SET_SLIDES_RETRACTED, SET_SLIDES_LOW, SET_SLIDES_MEDIUM, SET_SLIDES_HIGH,

        TOGGLE_RIGHT_BUCKET, TOGGLE_LEFT_BUCKET,

        PLANE_RELEASE,

        //dunno bro | it was for debug and calibration stuff
        TOGGLE_WHEEL_SPEED_ADJUSTMENT,
        REDUCED_CLOCKWISE, REDUCED_COUNTER_CLOCKWISE,
    }

    //gamepad1 is for movement
    //gamepad2 is for operation
    Gamepad gamepad1, gamepad2, previous_gamepad1, previous_gamepad2;

    public final HashMap<DriverAction, Boolean> getButtonState = new HashMap<DriverAction,Boolean>() {{
        //put(DriverAction.TOGGLE_WHEEL_SPEED_ADJUSTMENT, gamepad1.left_bumper);
        put(DriverAction.MOVE_STRAIGHT_FORWARD,         gamepad1.dpad_up);
        put(DriverAction.MOVE_STRAIGHT_BACKWARD,        gamepad1.dpad_down);
        put(DriverAction.MOVE_STRAIGHT_LEFT,            gamepad1.dpad_left);
        put(DriverAction.MOVE_STRAIGHT_RIGHT,           gamepad1.dpad_right);

        put(DriverAction.SET_SLIDES_RETRACTED,          gamepad2.dpad_down);
        put(DriverAction.SET_SLIDES_LOW,                gamepad2.dpad_left);
        put(DriverAction.SET_SLIDES_MEDIUM,             gamepad2.dpad_up);
        put(DriverAction.SET_SLIDES_HIGH,               gamepad2.dpad_right);
        put(DriverAction.TOGGLE_RIGHT_BUCKET,           gamepad2.right_bumper);
        put(DriverAction.TOGGLE_LEFT_BUCKET,            gamepad2.left_bumper);
        put(DriverAction.PLANE_RELEASE,                 gamepad2.x);
        put(DriverAction.TOGGLE_INTAKE_MOTOR_ROTATION,  gamepad2.circle);

        /*
        put(DriverAction.REDUCED_CLOCKWISE,             gamepad1.x);
        put(DriverAction.REDUCED_COUNTER_CLOCKWISE,     gamepad1.b);
        put(DriverAction.SET_SLIDES_RETRACTED,          gamepad2.dpad_down);
        put(DriverAction.SET_SLIDES_LOW,                gamepad2.dpad_left);
        put(DriverAction.SET_SLIDES_MEDIUM,             gamepad2.dpad_right);
        put(DriverAction.SET_SLIDES_HIGH,               gamepad2.dpad_up);
         */
    }};

    public final HashMap<DriverAction, Boolean> getPreviousButtonState = new HashMap<DriverAction, Boolean>() {{
        //put(DriverAction.TOGGLE_WHEEL_SPEED_ADJUSTMENT, previous_gamepad1.left_bumper); // not entirely sure if we need that
        put(DriverAction.MOVE_STRAIGHT_FORWARD,         previous_gamepad1.dpad_up);
        put(DriverAction.MOVE_STRAIGHT_BACKWARD,        previous_gamepad1.dpad_down);
        put(DriverAction.MOVE_STRAIGHT_LEFT,            previous_gamepad1.dpad_left);
        put(DriverAction.MOVE_STRAIGHT_RIGHT,           previous_gamepad1.dpad_right);

        put(DriverAction.SET_SLIDES_RETRACTED,          previous_gamepad2.dpad_down);
        put(DriverAction.SET_SLIDES_LOW,                previous_gamepad2.dpad_left);
        put(DriverAction.SET_SLIDES_MEDIUM,             previous_gamepad2.dpad_up);
        put(DriverAction.SET_SLIDES_HIGH,               previous_gamepad2.dpad_right);
        put(DriverAction.TOGGLE_RIGHT_BUCKET,           previous_gamepad2.right_bumper);
        put(DriverAction.TOGGLE_LEFT_BUCKET,            previous_gamepad2.left_bumper);
        put(DriverAction.PLANE_RELEASE,                 previous_gamepad2.x);

        /*
        put(DriverAction.REDUCED_CLOCKWISE,             previous_gamepad1.x);
        put(DriverAction.REDUCED_COUNTER_CLOCKWISE,     previous_gamepad1.b);
        put(DriverAction.SET_SLIDES_RETRACTED,          previous_gamepad2.dpad_down);
        put(DriverAction.SET_SLIDES_LOW,                previous_gamepad2.dpad_left);
        put(DriverAction.SET_SLIDES_MEDIUM,             previous_gamepad2.dpad_right);
        put(DriverAction.SET_SLIDES_HIGH,               previous_gamepad2.dpad_up);
        */
    }};

    /**
     * if button was on in previous state but not anymore
     * @param action the action passed in
     * @return if the button was released
     */
    public boolean getButtonRelease(DriverAction action) {
        return !getButtonState.get(action) && getPreviousButtonState.get(action);
    }


    public GamepadWrapper(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        updatePrevious();
    }

    public void updatePrevious() {
//        try {
        previous_gamepad1.copy(gamepad1);
        previous_gamepad2.copy(gamepad2);
//        } catch (RobotCoreException e) {}
    }

    public AnalogValues getAnalogValues() {
        return new AnalogValues(gamepad1, gamepad2);
    }

    public boolean getButtonState(DriverAction driverAction) {
        switch (driverAction) {
            // Gamepad 1 Controls
            case TOGGLE_WHEEL_SPEED_ADJUSTMENT:
                return gamepad1.left_bumper;
            case MOVE_STRAIGHT_FORWARD:
                return gamepad1.dpad_up;
            case MOVE_STRAIGHT_BACKWARD:
                return gamepad1.dpad_down;
            case MOVE_STRAIGHT_LEFT:
                return gamepad1.dpad_left;
            case MOVE_STRAIGHT_RIGHT:
                return gamepad1.dpad_right;
            case REDUCED_CLOCKWISE:
                return gamepad1.x;
            case REDUCED_COUNTER_CLOCKWISE:
                return gamepad1.b;

            // Gamepad 2 Controls
            case SET_SLIDES_RETRACTED:
                return gamepad2.dpad_down;
            case SET_SLIDES_LOW:
                return gamepad2.dpad_left;
            case SET_SLIDES_MEDIUM:
                return gamepad2.dpad_right;
            case SET_SLIDES_HIGH:
                return gamepad2.dpad_up;
            case TOGGLE_INTAKE_MOTOR_ROTATION:
                return false; //TODO DETERMINE KEYBIND ON THE PLAYSTATION CONTROLLER
            case TOGGLE_RIGHT_BUCKET:
                return false; //TODO DETERMINE KEYBIND ON THE PLAYSTATION CONTROLLER
            case TOGGLE_LEFT_BUCKET:
                return false; //TODO DETERMINE KEYBIND ON THE PLAYSTATION CONTROLLER
            case PLANE_RELEASE:
                return false; //TODO DETERMINE KEYBIND ON THE PLAYSTATION CONTROLLER
        }
        assert false; //if you really want the robot to crash, then i guess you can use this function. if this manages to reach this, your a frekin genius, (or a duck hole)
        return false;
    }

}

class AnalogValues {
    public double gamepad1RightStickX, gamepad1RightStickY, gamepad1LeftStickX, gamepad1LeftStickY,
            gamepad2RightStickX, gamepad2RightStickY, gamepad2LeftStickX, gamepad2LeftStickY,
            gamepad1LeftTrigger, gamepad1RightTrigger, gamepad2LeftTrigger, gamepad2RightTrigger;

    public AnalogValues(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1RightStickX = gamepad1.right_stick_x;
        this.gamepad1RightStickY = gamepad1.right_stick_y;
        this.gamepad1LeftStickX = -gamepad1.left_stick_x;
        this.gamepad1LeftStickY = gamepad1.left_stick_y;

        this.gamepad2RightStickX = gamepad2.right_stick_x;
        this.gamepad2RightStickY = gamepad2.right_stick_y;
        this.gamepad2LeftStickX = gamepad2.left_stick_x;
        this.gamepad2LeftStickY = gamepad2.left_stick_y;

        this.gamepad1LeftTrigger = gamepad1.left_trigger;
        this.gamepad1RightTrigger = gamepad1.right_trigger;
        this.gamepad2LeftTrigger = gamepad2.left_trigger;
        this.gamepad2RightTrigger = gamepad2.left_trigger;
    }
}