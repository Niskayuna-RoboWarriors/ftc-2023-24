package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collections;

@TeleOp(name="Mechanism Test Op", group="Linear OpMode")
public class MechanismTestOp extends LinearOpMode {
    private ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    Robot robot;
    MechanismDriving mechanismDriving = new MechanismDriving();

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap, telemetry, elapsedTime);
        robot.slides.setPower(1.0);
        telemetry.addData("intake motor power", robot.slides.getPower());
        //mechanismDriving.openLeftCompartment(robot);
        //telemetry.addData("OPEN COMPARTMENT LEFT", robot.compartmentLeft.getPosition());
        sleep(1);
        //mechanismDriving.openRightCompartment(robot);
        //telemetry.addData("OPEN COMPARTMENT RIGHT", robot.compartmentRight.getPosition());
        sleep(1);
        /*
        for (Robot.SlideState slideState : new Robot.SlideState[] {
            Robot.SlideState.RETRACTED,
            Robot.SlideState.LOW,
            Robot.SlideState.MEDIUM,
            Robot.SlideState.HIGH
        }) {
            mechanismDriving.moveSlides(robot, slideState);
            telemetry.addData("MOVE SLIDES TO STATE " + slideState.toString(), robot.slides.getCurrentPosition());
            sleep(1000);
        }*/
        //mechanismDriving.closeLeftCompartment(robot);
        //telemetry.addData("CLOSE COMPARTMENT LEFT", robot.compartmentLeft.getPosition());
        //sleep(1);
        //mechanismDriving.closeRightCompartment(robot);
        //telemetry.addData("CLOSE COMPARTMENT RIGHT", robot.compartmentRight.getPosition());
        sleep(1000);
        mechanismDriving.turnIntakeIntakeMotor(robot);
        telemetry.addData("TURN INTAKE INTAKE MOTOR", robot.intakeMotor.getPower());
        sleep(1000);
        mechanismDriving.turnOffIntakeMotor(robot);
        telemetry.addData("TURN OFF INTAKE MOTOR", robot.intakeMotor.getPower());
        sleep(1000);
        mechanismDriving.turnOuttakeIntakeMotor(robot);
        sleep(1000);
        mechanismDriving.turnOffIntakeMotor(robot);
        //mechanismDriving.releasePlaneSpring(robot);
        //telemetry.addData("RELEASE PLANE SPRING", robot.planeSpring.getPosition());
        telemetry.update();
        sleep(100000);
    }
}
