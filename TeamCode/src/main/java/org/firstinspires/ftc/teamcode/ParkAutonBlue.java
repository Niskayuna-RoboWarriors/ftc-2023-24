package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="ParkAutonBlue", group="Linear OpMode")
public class ParkAutonBlue extends LinearOpMode{

    private ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    @Override
    public void runOpMode() {
        RobotManager robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, telemetry, elapsedTime);
        waitForStart();
        NavigationTeleOp.setDriveMotorPowers(-Math.PI/2,0.3,0,robotManager.robot,false);
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e){
//            throw new RuntimeException(e);
//        }
        waitMilliseconds(150);
        NavigationTeleOp.setDriveMotorPowers(-Math.PI,0.3,0,robotManager.robot,false);
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        waitMilliseconds(9000);
        NavigationTeleOp.stopMovement(robotManager.robot);
    }

    public void waitMilliseconds(long ms) {
        double start_time = elapsedTime.time();
        while (opModeIsActive() && elapsedTime.time() - start_time < ms) {}
    }
}