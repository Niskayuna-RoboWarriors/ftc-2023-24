package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="BlankAuton", group="Linear OpMode")
public class BlankAutonOp extends LinearOpMode {

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
    public enum PixelPosition {LEFT, CENTER, RIGHT};
    public enum AutonMode {TOP, MIDDLE, BOTTOM};
    private static CenterStageAuton.MovementMode movementMode;
    private static CenterStageAuton.StartingSide startingSide;
    private static CenterStageAuton.AllianceColor allianceColor;
    private static CenterStageAuton.PixelPosition pixelPosition;
    private static CenterStageAuton.ParkingPosition parkingPosition;
    private static CenterStageAuton.AutonMode autonMode;

    @Override
    public void runOpMode() {
        initSharedPreferences();
        robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, telemetry, elapsedTime);
        IMUPositioning.Initialize(this);

        /* Runs no code */
        // CenterStageAuton.PixelPosition pixelPosition = robotManager.computerVision.getPixelPosition();

        // autonomousPathing = new AutonomousPathing(robotManager, allianceColor, startingSide, movementMode, pixelPosition, parkingPosition, autonMode);
        // autonomousPathing.runAutonPath(robotManager, robotManager.robot, allianceColor, startingSide, pixelPosition, autonMode, parkingPosition);

    }
    public void initSharedPreferences() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.hardwareMap.appContext);
        String movementModePref = sharedPrefs.getString("movement_mode", "ERROR");
        String startingSidePref = sharedPrefs.getString("starting_side", "ERROR");
        String allianceColorPref = sharedPrefs.getString("alliance_color", "ERROR");
        String autonModePref = sharedPrefs.getString("auton_mode", "ERROR");
        String parkingPositionPref = sharedPrefs.getString("parking_position", "ERROR");

        telemetry.addData("Movement mode", movementModePref);
        telemetry.addData("Wait time", waitTime);
        telemetry.addData("Auton Mode", autonModePref);
        telemetry.addData("Parking position", parkingPositionPref);
        telemetry.addData("Starting side", startingSidePref);
        telemetry.addData("Alliance color", allianceColorPref);

        System.out.println("Movement mode "+ movementModePref);
        System.out.println("Wait time "+ waitTime);
        System.out.println("Starting side "+ startingSidePref);
        System.out.println("Alliance color "+ allianceColorPref);
        System.out.println("Auton Mode"+ autonModePref);
        System.out.println("Parking position "+ parkingPositionPref);


        /* Currently no waitTime functionality */

        if (allianceColorPref.equals("BLUE")) {
            allianceColor = CenterStageAuton.AllianceColor.BLUE;
        }
        else if (allianceColorPref.equals("RED")) {
            allianceColor = CenterStageAuton.AllianceColor.RED;
        }

        if (startingSidePref.equals("LEFT")) {
            startingSide = CenterStageAuton.StartingSide.LEFT;
        }
        else if (startingSidePref.equals("RIGHT")) {
            startingSide = CenterStageAuton.StartingSide.RIGHT;
        }

        if (movementModePref.equals("FORWARD_ONLY")) {
            movementMode = CenterStageAuton.MovementMode.FORWARD_ONLY;
        }
        else if (movementModePref.equals("STRAFE")) {
            movementMode = CenterStageAuton.MovementMode.STRAFE;
        }

        if (autonModePref.equals("TOP")) {
            autonMode = CenterStageAuton.AutonMode.TOP;
        }
        else if (autonModePref.equals("MIDDLE")) {
            autonMode = CenterStageAuton.AutonMode.MIDDLE;
        }
        else if (autonModePref.equals("BOTTOM")) {
            autonMode = CenterStageAuton.AutonMode.BOTTOM;
        }

        if (parkingPositionPref.equals("LEFT")) {
            parkingPosition = CenterStageAuton.ParkingPosition.LEFT;
        }
        else if (parkingPositionPref.equals("CENTER")) {
            parkingPosition = CenterStageAuton.ParkingPosition.CENTER;
        }
        else if (parkingPositionPref.equals("RIGHT")) {
            parkingPosition = CenterStageAuton.ParkingPosition.RIGHT;
        }
    }

    public void waitMilliseconds(long ms) {
        double start_time = elapsedTime.time();
        while (opModeIsActive() && elapsedTime.time() - start_time < ms) {}
    }
}
