package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;

/** Wraps a gamepad so that button mappings are stored in one place.
 */
public class GamepadWrapper {
    public enum DriverAction {
        SET_SLIDES_RETRACTED, SET_SLIDES_LOW, SET_SLIDES_MEDIUM, SET_SLIDES_HIGH,
        TOGGLE_WHEEL_SPEED_ADJUSTMENT,
        MOVE_STRAIGHT_FORWARD, MOVE_STRAIGHT_BACKWARD, MOVE_STRAIGHT_LEFT, MOVE_STRAIGHT_RIGHT,
        REDUCED_CLOCKWISE, REDUCED_COUNTER_CLOCKWISE,
    }

    //gamepad1 is for movement
    //gamepad2 is for operation
    Gamepad gamepad1, gamepad2;

    public final HashMap<DriverAction,Boolean> getButtonStateFromAction = new HashMap<DriverAction,Boolean>() {{
        put(DriverAction.TOGGLE_WHEEL_SPEED_ADJUSTMENT, gamepad1.left_bumper); // not entirely sure if we need that
        put(DriverAction.MOVE_STRAIGHT_FORWARD,         gamepad1.dpad_up);
        put(DriverAction.MOVE_STRAIGHT_BACKWARD,        gamepad1.dpad_down);
        put(DriverAction.MOVE_STRAIGHT_LEFT,            gamepad1.dpad_right);
        put(DriverAction.MOVE_STRAIGHT_RIGHT,           gamepad1.dpad_right);
        put(DriverAction.REDUCED_CLOCKWISE,             gamepad1.x);
        put(DriverAction.REDUCED_COUNTER_CLOCKWISE,     gamepad1.b);

        put(DriverAction.SET_SLIDES_RETRACTED,          gamepad2.dpad_down);
        put(DriverAction.SET_SLIDES_LOW,                gamepad2.dpad_left);
        put(DriverAction.SET_SLIDES_MEDIUM,             gamepad2.dpad_right);
        put(DriverAction.SET_SLIDES_HIGH,               gamepad2.dpad_up);
    }};

    public GamepadWrapper() {
        this.gamepad1 = new Gamepad();
        this.gamepad2 = new Gamepad();
    }

    public GamepadWrapper(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void copyGamepads(GamepadWrapper gamepadWrapper) {
//        try {
        this.gamepad1.copy(gamepadWrapper.gamepad1);
        this.gamepad2.copy(gamepadWrapper.gamepad2);
//        } catch (RobotCoreException e) {}
    }

    /**
     * gets analog values from gamepad1 and gamepad2
     * @return class with analog values
     */
    public AnalogValues getAnalogValues() {
        return new AnalogValues(gamepad1, gamepad2);
    }
}


/** Stores 8 analog values on the gamepad:
 *   - An x and y coordinate for each of 4 sticks across 2 gamepads
 *   - Each of the 4 triggers
 */
class AnalogValues {
    public double gamepad1RightStickX, gamepad1RightStickY, gamepad1LeftStickX, gamepad1LeftStickY,
            gamepad2RightStickX, gamepad2RightStickY, gamepad2LeftStickX, gamepad2LeftStickY,
            gamepad1LeftTrigger, gamepad1RightTrigger, gamepad2LeftTrigger, gamepad2RightTrigger;

    /**
     * storing analog values from gamepads
     * @param gamepad1 first gamepad
     * @param gamepad2 second gamepad
     */
    public AnalogValues(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1RightStickX = gamepad1.right_stick_x;
        this.gamepad1RightStickY = gamepad1.right_stick_y;
        this.gamepad1LeftStickX = gamepad1.left_stick_x;
        this.gamepad1LeftStickY = gamepad1.left_stick_y;
        //^ this line previously had a magic negative -> -gamepad1.left_stick_y

        this.gamepad2RightStickX = gamepad2.right_stick_x;
        this.gamepad2RightStickY = gamepad2.right_stick_y;
        this.gamepad2LeftStickX = gamepad2.left_stick_x;
        this.gamepad2LeftStickY = gamepad2.left_stick_y;

        this.gamepad1LeftTrigger = gamepad1.left_trigger;
        this.gamepad1RightTrigger = gamepad1.right_trigger;
        this.gamepad2LeftTrigger = gamepad2.left_trigger;
        this.gamepad2RightTrigger = gamepad2.right_trigger;
    }
}