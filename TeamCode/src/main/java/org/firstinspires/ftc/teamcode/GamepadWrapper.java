package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import static java.lang.Math.abs;

import java.sql.Driver;

/** Wraps a gamepad so that button mappings are stored in one place.
 */
public class GamepadWrapper {
    public enum DriverAction {
        MOVE_STRAIGHT_FORWARD, MOVE_STRAIGHT_BACKWARD, MOVE_STRAIGHT_LEFT, MOVE_STRAIGHT_RIGHT,

        TOGGLE_INTAKE_MOTOR_ROTATION, TOGGLE_OUTTAKE_MOTOR_ROTATION,

        SET_SLIDES_RETRACTED, SET_SLIDES_LOW, SET_SLIDES_MEDIUM, SET_SLIDES_HIGH,

        TOGGLE_RIGHT_BUCKET, TOGGLE_LEFT_BUCKET,

        PLANE_RELEASE,

        //dunno bro | it was for debug and calibration stuff
        TOGGLE_WHEEL_SPEED_ADJUSTMENT,
        REDUCED_CLOCKWISE, REDUCED_COUNTER_CLOCKWISE,

        OPEN_CLAW, CLOSE_CLAW, DROP_1_FROM_CLAW
    }

    //gamepad1 is for movement
    //gamepad2 is for operation
    Gamepad gamepad1, gamepad2, previous_gamepad1, previous_gamepad2;

    public GamepadWrapper(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.previous_gamepad1 = new Gamepad();
        this.previous_gamepad2 = new Gamepad();
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


    private boolean getButtonStateFromGamepads(Gamepad gamepad1, Gamepad gamepad2, DriverAction action) {
        switch (action){
            // Gamepad 1 Controls
            case TOGGLE_WHEEL_SPEED_ADJUSTMENT:
                return gamepad1.left_bumper;
            case MOVE_STRAIGHT_FORWARD:
                return gamepad1.dpad_up;
            case MOVE_STRAIGHT_BACKWARD:
                return gamepad1.dpad_down;
            case MOVE_STRAIGHT_LEFT:
                return gamepad1.dpad_right;
            case MOVE_STRAIGHT_RIGHT:
                return gamepad1.dpad_left;
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
            case OPEN_CLAW:
                return gamepad2.b;
            case CLOSE_CLAW:
                return gamepad2.x;
            case DROP_1_FROM_CLAW:
                return gamepad2.y;
//            case TOGGLE_RIGHT_BUCKET:
//                return false; //TODO DETERMINE KEYBIND ON THE PLAYSTATION CONTROLLER
//            case TOGGLE_LEFT_BUCKET:
//                return false; //TODO DETERMINE KEYBIND ON THE PLAYSTATION CONTROLLER
            case PLANE_RELEASE:
                return gamepad2.a;
        }
        assert false; //if you really want the robot to crash, then i guess you can use this function. if this manages to reach this, your a frekin genius, (or a duck hole)
        return false;
    }

    public boolean getButtonState(DriverAction driverAction) {
        return getButtonStateFromGamepads(gamepad1, gamepad2, driverAction);
    }

    /**
     * if button was on in previous state but not anymore
     * @param action the action passed in
     * @return if the button was released
     */
    public boolean getButtonRelease(DriverAction action) {
        return !getButtonState(action)
                && getButtonStateFromGamepads(previous_gamepad1, previous_gamepad2, action);
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
