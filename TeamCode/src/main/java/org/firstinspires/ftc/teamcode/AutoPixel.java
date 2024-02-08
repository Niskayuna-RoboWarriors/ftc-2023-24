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

    private final Scalar // In BGR order!!
            Green = new Scalar(0, 255, 0),
            Yellow = new Scalar(255, 255),
            Purple = new Scalar(255, 0, 255),
            White = new Scalar(255, 255, 255);
    @Override
    public Mat processFrame(Mat input) {

        Mat areaMat = input.submat(new Rect(topLeft, bottomRight));
        Scalar colorSum = Core.sumElems(areaMat);
        Scalar colorAverage = new Scalar(
                colorSum.val[0]/(1920*540),
                colorSum.val[1]/(1920*540),
                colorSum.val[2]/(1920*540));
        Mat subAreaMat = new Mat();
//        Core.subtract(areaMat, colorAverage, subAreaMat);
//        offset = calculateOffset(areaMat, White, colorAverage);
        results = calculateOffset(areaMat, White, colorAverage);
        return areaMat;
    }

    public long[] getOffset() {
//        return offset;
        return results;
    }

    private long[] calculateOffset(Mat input, Scalar color, Scalar colorAverage) {
//        Scalar avgDiff = Core.norm(color - colorAverage);
        int count = 0;
        int sum = 0;
        for (int row = 0; row < 540; row += 50) {
            for (int col = 0; col < 1920; col += 50) {
                double[] pixel = input.get(row, col);
                if (Math.abs(
                        Math.pow(pixel[0]-colorAverage.val[0], 2) +
                        Math.pow(pixel[1]-colorAverage.val[1], 2) +
                        Math.pow(pixel[2]-colorAverage.val[2], 2)) > 3500) {
                    Imgproc.drawMarker(input, new Point(col, row), new Scalar(255, 255, 255));
                    sum += col;
                    count += 1;
                }
            }
        }
        double xAverage = (double) sum / count;
        return new long[]{Math.round(xAverage) - 960, count, sum,
                (long) colorAverage.val[0],
                (long) colorAverage.val[1],
                (long) colorAverage.val[2]};

    }

}
