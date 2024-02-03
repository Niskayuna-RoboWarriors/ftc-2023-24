/* Authors: Nisky Robotics 6460 2023-2024 Programming Team
 */

package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collections;


/** Tele-Op OpMode for Center Stage.
 */
@TeleOp(name="CenterStage Tele-Op", group="TeleOp OpMode")
public class CenterStageTeleOp extends OpMode {

    private RobotManager robotManager;
    private ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    /**method that gets called when the init button is pressed
     */
    @Override
    public void init() {
        telemetry.addData("init", "start");
        telemetry.update();
        robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, telemetry, elapsedTime);
        telemetry.addData("init after robot manager", null);
        telemetry.update();
        IMUPositioning.Initialize(this);
        telemetry.addData("Initalized", null);
        telemetry.update();
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
        robotManager.driveMechanisms();
        telemetry.addData("after drive mechanisms", robotManager.elapsedTime.time()-start_time);
        robotManager.moveRobot();
        telemetry.addData("after maneuver", robotManager.elapsedTime.time()-start_time);
        telemetry.update();
    }

    /**method that is called when the stop button is pressed
     */
    @Override
    public void stop() {}

}