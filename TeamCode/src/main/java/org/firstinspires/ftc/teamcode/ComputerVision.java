package org.firstinspires.ftc.teamcode;
/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@TeleOp
public class ComputerVision extends LinearOpMode
{
    OpenCvCamera camera;
    ComputerVisionLibrariesFunctions aprilTagDetectionPipeline;
    AutoPixel autoPixel;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the AUKEY webcam at 1920x1080.
    // You will need to do your own calibration for other configurations!
    double fx = 1506.89815403;
    double fy = 1506.89815403;
    double cx = 960;
    double cy = 540;

    // UNITS ARE METERS
    double tagsize = 0.103;

    //insert ID of sleeve
    int left = 1;
    int middle = 2;
    int right = 3;

    int ID_TAG_OF_INTEREST = 18; // Tag ID 18 from the 36h11 family

    AprilTagDetection tagOfInterest = null;
    HardwareMap hardwareMap;
    Telemetry telemetry;
    ElapsedTime elapsedTime;
    public ComputerVision(HardwareMap hardwareMap, Telemetry telemetry, ElapsedTime elapsedTime) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.elapsedTime = elapsedTime;
    };
    @Override
    public void runOpMode() {

    }
    public CenterStageAuton.PixelPosition getPixelPosition()
    {
        telemetry.addData("start getPixelPosition", 1);
        telemetry.update();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        telemetry.addData("cameraMonitorViewId", cameraMonitorViewId);
        telemetry.update();
//        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
//        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        telemetry.addData("camera created", camera);
        telemetry.update();
        aprilTagDetectionPipeline = new ComputerVisionLibrariesFunctions(tagsize, fx, fy, cx, cy);
        camera.setPipeline(aprilTagDetectionPipeline);
        telemetry.addData("pipeline", aprilTagDetectionPipeline);
        telemetry.update();
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                //camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("on opened being called!!", 1);
                telemetry.update();
                camera.startStreaming(1920,1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                telemetry.addData("ERROR??? ", errorCode);
            }
        });

        telemetry.setMsTransmissionInterval(50);
        telemetry.addLine("after camera opened");
        telemetry.update();
        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    //    if(tag.id == ID_TAG_OF_INTEREST)
                    if(tag.id == left || tag.id == middle || tag.id == right)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */
        if(tagOfInterest == null || tagOfInterest.id == left)
        {
            /*
             * Insert your autonomous code here, presumably running some default configuration
             * since the tag was never sighted during INIT
             */
            //left code
            System.out.println("Left");
            return CenterStageAuton.PixelPosition.LEFT;

        }
        else if(tagOfInterest.id == middle) {
            //middle code
            System.out.println("middle");
            return CenterStageAuton.PixelPosition.CENTER;
        }
        System.out.println("right");
        return CenterStageAuton.PixelPosition.RIGHT;
        //       else {



        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        // while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
//        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
//        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
//        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }

    public long getPixelOffset() {
        telemetry.addData("start getPixelPosition", 1);
        telemetry.update();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        telemetry.addData("cameraMonitorViewId", cameraMonitorViewId);
        telemetry.update();
//        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
//        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        telemetry.addData("camera created", camera);
        telemetry.update();
        autoPixel = new AutoPixel();
        camera.setPipeline(autoPixel);
        telemetry.addData("pipeline", autoPixel);
        telemetry.update();
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                //camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("on opened being called!!", 1);
                telemetry.update();
                camera.startStreaming(1920,1080, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("on opened after called!!", 1);
                telemetry.update();
            }

            @Override
            public void onError(int errorCode)
            {
                telemetry.addData("ERROR??? ", errorCode);
            }
        });

//        telemetry.setMsTransmissionInterval(50);
        telemetry.addLine("after camera opened");
        telemetry.update();
        int i = 0;

        while (!isStopRequested()) {
            long[] results = autoPixel.getOffset();
            telemetry.addData("pixel offset,", results[0]);
            telemetry.addData("count,", results[1]);
            telemetry.addData("sum,", results[2]);
            telemetry.addData("B,", results[3]);
            telemetry.addData("G,", results[4]);
            telemetry.addData("R,", results[5]);
            telemetry.addData("iterations,", i);
//            telemetry.addData("pixel offset,", autoPixel.getOffset());
            telemetry.update();
            sleep(20);
        }
        telemetry.addLine("ends finally");
        telemetry.update();
        return 0;
    }
}