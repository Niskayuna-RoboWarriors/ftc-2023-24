package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="ParkAuton", group="Linear OpMode")
public class ParkAuton extends LinearOpMode{
    @Override
    public void runOpMode() {
        RobotManager robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, telemetry, new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS));
        waitForStart();
        NavigationTeleOp.setDriveMotorPowers(0,1,0,robotManager.robot,false);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        NavigationTeleOp.setDriveMotorPowers(Math.PI/2,1,0,robotManager.robot,false);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        NavigationTeleOp.stopMovement(robotManager.robot);
    }}