package org.firstinspires.ftc.teamcode;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import org.openftc.easyopencv.OpenCvPipeline;


public class AutoPixel extends OpenCvPipeline {

//    Point topLeft = new Point(0, 0);
    Point topLeft = new Point(0, 540);
//    Point bottomRight = new Point(1919, 539);
    Point bottomRight = new Point(1919, 1079);
    long offset = 0;
    long[] results = {0, 0, 0, 0, 0, 0};

    private final Scalar[] colors = {  // In RGB order!!
//        new Scalar(71,238,143), // Green
//        new Scalar(255, 255, 0), // Yellow
//        new Scalar(255, 0, 255), // Purple
//        new Scalar(255, 255, 255), // White
          new Scalar(255, 0, 0),
          new Scalar(0, 0, 255),
    };
    @Override
    public Mat processFrame(Mat input) {

//        Mat areaMat = input.submat(new Rect(topLeft, bottomRight));
        Mat areaMat = input;
        Scalar colorSum = Core.sumElems(areaMat);
        Scalar colorAverage = new Scalar(
                colorSum.val[0]/(1920*1080),
                colorSum.val[1]/(1920*1080),
                colorSum.val[2]/(1920*1080));
        Mat subAreaMat = new Mat();
//        Core.subtract(areaMat, colorAverage, subAreaMat);
//        offset = calculateOffset(areaMat, White, colorAverage);
        results = calculateOffset(areaMat, colorAverage);
        return areaMat;
    }

    public long[] getOffset() {
//        return offset;
        return results;
    }

    private long[] calculateOffset(Mat input, Scalar colorAverage) {
        int count = 0;
        int sum = 0;
//        int row = 0;
//        int col = 0;
        for (int row = 0; row < 1080; row += 40) {
            for (int col = 0; col < 1920;) {
                double[] pixel = input.get(row, col);
                Boolean sample = false;
                for (Scalar color : colors) {
                    if (Math.abs(
                            Math.pow(pixel[0]-color.val[0], 2) +
                                    Math.pow(pixel[1]-color.val[1], 2) +
                                    Math.pow(pixel[2]-color.val[2], 2)) < 22500) {
                        Imgproc.drawMarker(input, new Point(col, row), new Scalar(0, 0, 255));
                        sum += col;
                        count += 1;
                        sample = true;
                        break;
                    }
                }
                if (sample) {
                    col += 20;
                }
                else {
                    col += 50;
                }
            }
        }
        double xAverage = 960;
        if (count > 20) {
            xAverage = (double) sum / count;
        }
//        return new long[]{Math.round(xAverage) - 1134, count, sum,
        return new long[]{Math.round(xAverage), count, sum,
                (long) colorAverage.val[0],
                (long) colorAverage.val[1],
                (long) colorAverage.val[2]};

    }

}
