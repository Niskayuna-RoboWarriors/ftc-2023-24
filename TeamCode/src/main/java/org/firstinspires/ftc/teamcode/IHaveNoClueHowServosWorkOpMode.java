
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SERVO Test claw?", group="Linear OpMode")
public class IHaveNoClueHowServosWorkOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //private Servo clawServo = null;
    //private Servo rotationServo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Servo servo = hardwareMap.get(Servo.class, "claw");

        telemetry.addData("servo", servo);
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        double position = 0.3;

        // run until the end of the match (driver presses STOP;
        while (opModeIsActive()) {
            position += gamepad1.left_stick_y * .1;
            servo.setPosition(position);
            telemetry.addData("position", position);
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            sleep(1000);
        }
    }
}
