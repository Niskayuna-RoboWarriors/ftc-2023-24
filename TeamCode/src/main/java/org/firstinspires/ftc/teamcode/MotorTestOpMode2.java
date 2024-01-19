package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="MOTOR Test2", group="Linear OpMode")
public class MotorTestOpMode2 extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo clawServo = null;
    private Servo rotationServo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        DcMotor front_right  = hardwareMap.get(DcMotor.class, "front_right");
        DcMotor front_left  = hardwareMap.get(DcMotor.class, "front_left");
        DcMotor rear_right  = hardwareMap.get(DcMotor.class, "rear_right");
        DcMotor rear_left  = hardwareMap.get(DcMotor.class, "rear_left");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//            if (runtime.milliseconds() % 2000 < 1000)
//                motor.setPower(0.5);
//            else
//                motor.setPower(0.0);
            telemetry.addData("rear_right", gamepad2.right_stick_y);
            telemetry.addData("rear_left", gamepad2.left_stick_y);
            telemetry.addData("front_right", gamepad1.right_stick_y);
            telemetry.addData("front_left", gamepad1.left_stick_y);
            rear_right.setPower(gamepad2.right_stick_y);
            rear_left.setPower(gamepad2.left_stick_y);
            front_right.setPower(gamepad1.right_stick_y);
            front_left.setPower(gamepad1.left_stick_y);
            telemetry.addData("rear_right encoder", rear_right.getCurrentPosition());
            telemetry.addData("rear_left encoder", rear_left.getCurrentPosition());
            telemetry.addData("front_right encoder", front_right.getCurrentPosition());
            telemetry.addData("front_left encoder", front_left.getCurrentPosition());

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}