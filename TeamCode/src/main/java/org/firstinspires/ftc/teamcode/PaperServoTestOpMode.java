package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SERVO Test", group="Linear OpMode")
public class PaperServoTestOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //private Servo clawServo = null;
    //private Servo rotationServo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Servo servo = hardwareMap.get(Servo.class, "plane_spring");
        telemetry.addData("servo", servo);
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (runtime.milliseconds() % 2000 < 1000) {
                servo.setPosition(MechanismDriving.PLANE_SPRING_UNRELEASED_POS);
                telemetry.addData("Going to ", MechanismDriving.PLANE_SPRING_UNRELEASED_POS);
            }
            else {
                servo.setPosition(MechanismDriving.PLANE_SPRING_RELEASED_POS);
                telemetry.addData("Going to ", MechanismDriving.PLANE_SPRING_RELEASED_POS);
            }

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
