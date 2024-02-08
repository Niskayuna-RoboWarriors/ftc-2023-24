package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Random;

@Autonomous(name="AutoPixelTestOpMode", group="Linear OpMode")
public class AutoPixelTestOpMode extends LinearOpMode {

    private RobotManager robotManager;
    private ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private static SharedPreferences sharedPrefs;

    private static int waitTime = 0;

    // Which Alliance we're on
    public enum AllianceColor {BLUE, RED};
    // Which side we're on (relative to our team)
    public enum StartingSide {LEFT, RIGHT};
    // Where to park at the end of the autonomous period
    public enum ParkingPosition {LEFT, CENTER, RIGHT};
    // Movement mode during the autonomous period
    public enum MovementMode {FORWARD_ONLY, STRAFE};
    // Position of the pixel placements
    public enum AutonMode {TOP, MIDDLE, BOTTOM};
    private static MovementMode movementMode;
    private static StartingSide startingSide;
    private static AllianceColor allianceColor;
    private static long pixelOffset;
    private static ParkingPosition parkingPosition;
    private static AutonMode autonMode;

    @Override
    public void runOpMode() {
        robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, telemetry, elapsedTime);
        IMUPositioning.Initialize(this);
//        waitForStart();

        pixelOffset = robotManager.computerVision.getPixelOffset();


        telemetry.addData("pixel offset", pixelOffset);
        telemetry.update();
//        autonomousPathing = new AutonomousPathing(robotManager, allianceColor, startingSide, movementMode, pixelPosition, parkingPosition, autonMode);
//        autonomousPathing.runAutonPath(robotManager, robotManager.robot, allianceColor, startingSide, pixelPosition, autonMode, parkingPosition);

    }
    public void waitMilliseconds(long ms) {
        double start_time = elapsedTime.time();
        while (opModeIsActive() && elapsedTime.time() - start_time < ms) {}
    }
}
