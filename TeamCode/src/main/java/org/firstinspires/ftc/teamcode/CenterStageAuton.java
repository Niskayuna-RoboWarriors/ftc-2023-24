package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Random;

@Autonomous(name="CenterStageAuton", group="Linear OpMode")
public class CenterStageAuton extends LinearOpMode {

    private RobotManager robotManager;
    private AutonomousPathing autonomousPathing;
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
    public enum PixelPosition {LEFT, CENTER, RIGHT}
    private static MovementMode movementMode;
    private static StartingSide startingSide;
    private static AllianceColor allianceColor;
    private static ArrayList<Position> navigationPath;

    @Override
    public void runOpMode() {
        initSharedPreferences();
        robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, telemetry, elapsedTime);
        IMUPositioning.Initialize(this);
        robotManager.computerVision.initialize();

        autonomousPathing = new AutonomousPathing(robotManager, allianceColor, startingSide, movementMode);

        // Repeatedly run CV
        ParkingPosition parkingPosition = ParkingPosition.LEFT;
        while (!isStarted() && !isStopRequested()) {
            parkingPosition = robotManager.computerVision.getParkingPosition();
            waitMilliseconds(20);
        }

    }
    public void initSharedPreferences() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.hardwareMap.appContext);
        String movementModePref = sharedPrefs.getString("movement_mode", "ERROR");
        String startingSidePref = sharedPrefs.getString("starting_side", "ERROR");
        String allianceColorPref = sharedPrefs.getString("alliance_color", "ERROR");
        String pixelPositionPref = sharedPrefs.getString("pixel_position", "ERROR");
        String parkingPositionPref = sharedPrefs.getString("parking_position", "ERROR");

        telemetry.addData("Movement mode", movementModePref);
        telemetry.addData("Wait time", waitTime);
        telemetry.addData("Pixel position", pixelPositionPref);
        telemetry.addData("Parking position", parkingPositionPref);
        telemetry.addData("Starting side", startingSidePref);
        telemetry.addData("Alliance color", allianceColorPref);

        System.out.println("Movement mode "+ movementModePref);
        System.out.println("Wait time "+ waitTime);
        System.out.println("Starting side "+ startingSidePref);
        System.out.println("Alliance color "+ allianceColorPref);
        System.out.println("Pixel position "+ pixelPositionPref);
        System.out.println("Parking position "+ parkingPositionPref);

        if (allianceColor.equals("BLUE")) {
            allianceColor = AllianceColor.BLUE;
        }
        else if (allianceColor.equals("RED")) {
            allianceColor = AllianceColor.RED;
        }
        if (startingSide.equals("LEFT")) {
            startingSide = StartingSide.LEFT;
        }
        else if (startingSide.equals("RIGHT")) {
            startingSide = StartingSide.RIGHT;
        }
    }

    public void waitMilliseconds(long ms) {
        double start_time = elapsedTime.time();
        while (opModeIsActive() && elapsedTime.time() - start_time < ms) {}
    }
}
