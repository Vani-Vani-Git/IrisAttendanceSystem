package com.vani.irisapi.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class IrisMatcher {

    public static double compare(String path1, String path2) {

        Mat img1 = extractEye(path1);
        Mat img2 = extractEye(path2);

        if (img1.empty() || img2.empty()) {
            return Double.MAX_VALUE;
        }

        Imgproc.equalizeHist(img1, img1);
        Imgproc.equalizeHist(img2, img2);

        Imgproc.GaussianBlur(img1, img1, new Size(5,5), 0);
        Imgproc.GaussianBlur(img2, img2, new Size(5,5), 0);

        double score = Core.norm(img1, img2, Core.NORM_L2);

        return score;
    }

    public static Mat extractEye(String path) {

        Mat image = Imgcodecs.imread(path);

        if (image.empty()) {
            return new Mat();
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 🔥 Normalize + enhance
        Core.normalize(gray, gray, 0, 255, Core.NORM_MINMAX);
        Imgproc.equalizeHist(gray, gray);

        // 🔥 Eye detector (optional)
        CascadeClassifier eyeDetector =
                new CascadeClassifier("src/main/resources/haarcascade_eye.xml");

        MatOfRect eyes = new MatOfRect();
        eyeDetector.detectMultiScale(gray, eyes);

        Rect[] eyeArray = eyes.toArray();

        if (eyeArray.length > 0) {

            // 🔥 Pick largest eye
            Rect bestEye = eyeArray[0];

            for (Rect r : eyeArray) {
                if (r.area() > bestEye.area()) {
                    bestEye = r;
                }
            }

            Mat eyeRegion = new Mat(gray, bestEye);

            // 🔥 Focus center (iris-like)
            int x = eyeRegion.cols() / 4;
            int y = eyeRegion.rows() / 4;
            int w = eyeRegion.cols() / 2;
            int h = eyeRegion.rows() / 2;

            Mat irisRegion = new Mat(eyeRegion, new Rect(x, y, w, h));

            Imgproc.resize(irisRegion, irisRegion, new Size(100, 100));

            return irisRegion;
        }

        // 🔥 FALLBACK (very important)
        int x = gray.cols() / 4;
        int y = gray.rows() / 4;
        int w = gray.cols() / 2;
        int h = gray.rows() / 2;

        Mat cropped = new Mat(gray, new Rect(x, y, w, h));
        Imgproc.resize(cropped, cropped, new Size(100, 100));

        return cropped;
    }
}