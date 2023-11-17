/* Authors: Nisky Robotics 6460 2021-2022 Programming Team
 */

package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collections;


/** Autonomous OpMode for Freight Frenzy.
 */
@TeleOp(name="CenterStage Tele-Op", group="TeleOp OpMode")
public class CenterStageTeleOp extends OpMode {

    private RobotManager robotManager;
    private ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    /**method that gets called when the init button is pressed
     */
    @Override
    public void init() {
        initSharedPreferences();
        CenterStageTeleOp.allianceColor = RobotManager.AllianceColor.BLUE;
        robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, new ArrayList<>(Collections.emptyList()),
                allianceColor, RobotManager.StartingSide.LEFT,
                Navigation.MovementMode.STRAFE, telemetry, elapsedTime);
        IMUPositioning.Initialize(this);
    }

    /**method that gets called when the play button gets pressed
     */
    @Override
    public void start() {}

    /**method that gets called repeatedly after start is called
     */
    @Override
    public void loop() {
        robotManager.robot.positionManager.updatePosition(robotManager.robot);
        telemetry.addData("Pos X", robotManager.robot.positionManager.position.getX());
        telemetry.addData("Pos Y", robotManager.robot.positionManager.position.getY());
        telemetry.addData("Pos R", robotManager.robot.positionManager.position.getRotation());
        double start_time = robotManager.elapsedTime.time();
        robotManager.readControllerInputs();
        telemetry.addData("after read controller inputs", robotManager.elapsedTime.time()-start_time);
        robotManager.driveMechanisms(robotManager);
        telemetry.addData("after drive mechanisms", robotManager.elapsedTime.time()-start_time);
        robotManager.maneuver();
        telemetry.addData("after maneuver", robotManager.elapsedTime.time()-start_time);

        telemetry.update();
    }

    /**method that is called when the stop button is pressed
     */
    @Override
    public void stop() {}

    // ANDROID SHARED PREFERENCES
    // ==========================

    // NOTE: not sure if we need this for Tele-Op, since we can just pass in random values for the Navigation constructor

    // Adapted from https://github.com/ver09934/twentytwenty/blob/ian-dev/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/SkystoneAuton.java

    private static SharedPreferences sharedPrefs;

    private static RobotManager.AllianceColor allianceColor;
    private static RobotManager.StartingSide startingSide;

    public void initSharedPreferences() {
        //
       sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.hardwareMap.appContext);

       String allianceColor = sharedPrefs.getString("alliance_color", "ERROR");
//        String startingSide = sharedPrefs.getString("starting_side", "ERROR");

       if (allianceColor.equals("BLUE")) {
           CenterStageTeleOp.allianceColor = RobotManager.AllianceColor.BLUE;
       }
       else if (allianceColor.equals("RED")) {
           CenterStageTeleOp.allianceColor = RobotManager.AllianceColor.RED;
       }
//        if (startingSide.equals("LEFT")) {
//            CenterStageTeleOp.startingSide = RobotManager.StartingSide.LEFT;
//        }
//        else if (startingSide.equals("RIGHT")) {
//            CenterStageTeleOp.startingSide = RobotManager.StartingSide.RIGHT;
//        }

       //CenterStageTeleOp.allianceColor = RobotManager.AllianceColor.BLUE;
    }
}