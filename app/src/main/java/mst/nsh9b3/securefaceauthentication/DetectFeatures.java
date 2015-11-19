package mst.nsh9b3.securefaceauthentication;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nick on 11/16/15.
 */
public class DetectFeatures
{
    private static final String TAG = "SFA::DetectFeatures";

    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;

    MatOfKeyPoint keyPoints1;
    MatOfKeyPoint keyPoints2;

    Mat descriptors1;
    Mat descriptors2;

    String filename;

    public DetectFeatures(String filename)
    {
        Log.i(TAG, "DetectFeatures");

        this.filename = filename;

        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        keyPoints1 = new MatOfKeyPoint();
        keyPoints2 = new MatOfKeyPoint();

        descriptors1= new Mat();
        descriptors2= new Mat();

        compare(filename, new String[]{"Nick1.jpg", "Nick2.jpg", "Nick3.jpg"});
    }

    private void detect()
    {
        Log.i(TAG, "detect");

        String newFilename;
        SimpleDateFormat sdf = new SimpleDateFormat("_MM-dd-yyyy_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());

        newFilename = Environment.getExternalStorageDirectory().getPath() + "/" + MainActivity.savedImageName + currentDateandTime + ".jpg";

        Mat img = Imgcodecs.imread(filename);

        detector.detect(img, keyPoints1);
        descriptor.compute(img, keyPoints1, descriptors1);

//        detector.detect(img, keyPoints2);
//        descriptor.compute(img, keyPoints2, descriptors2);

//        MatOfDMatch matches = new MatOfDMatch();
//        matcher.match(descriptors1, descriptors2, matches);
//
//        Scalar RED = new Scalar(255,0,0);
//        Scalar GREEN = new Scalar(0,255,0);
        Scalar kpColor = new Scalar(255,159,10);

        Mat outputImg = new Mat();
//        MatOfByte drawnMatches = new MatOfByte();

        //Features2d.drawMatches(img, keyPoints1, img, keyPoints2, matches, outputImg, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);

        Features2d.drawKeypoints(img, keyPoints1, outputImg, kpColor, 0);

        Bitmap imageMatched = Bitmap.createBitmap(outputImg.cols(), outputImg.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(outputImg, imageMatched);
        Utilities.savePicture(newFilename, imageMatched);
    }

    public void compare(String filename, String[] compareFiles)
    {
        Log.i(TAG, "compare");
        Scalar RED = new Scalar(255,0,0);
        Scalar GREEN = new Scalar(0,255,0);

        Mat img = Imgcodecs.imread( Environment.getExternalStorageDirectory().getPath() + "/Nick1.jpg");
        MatOfKeyPoint mainKeyPoints = new MatOfKeyPoint();
        Mat mainDescriptor = new Mat();

        detector.detect(img, mainKeyPoints);
        descriptor.compute(img, mainKeyPoints, mainDescriptor);

        //compareFiles[0] = Environment.getExternalStorageDirectory().getPath() + "/" + compareFiles[0] + ".jpg";

        Mat nickImg = Imgcodecs.imread(Environment.getExternalStorageDirectory().getPath() + "/Nick3.jpg");
        MatOfKeyPoint nickKeyPoints = new MatOfKeyPoint();
        Mat nickDescriptor = new Mat();

        detector.detect(nickImg, nickKeyPoints);
        descriptor.compute(nickImg, nickKeyPoints, nickDescriptor);

        MatOfDMatch  matches = new MatOfDMatch();
        matcher.match(mainDescriptor, nickDescriptor, matches);

        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();
        //this will draw all matches, works fine
        Features2d.drawMatches(img, mainKeyPoints, nickImg, nickKeyPoints, matches,
                outputImg, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);

        Bitmap imageMatched = Bitmap.createBitmap(outputImg.cols(), outputImg.rows(), Bitmap.Config.RGB_565);//need to save bitmap
        Utils.matToBitmap(outputImg, imageMatched);

        Utilities.savePicture(Environment.getExternalStorageDirectory().getPath() + "/compare.jpg", imageMatched);
//        for(int i = 0; i < compareFiles.length; i++)
//        {
//            //Fix file names
//            compareFiles[i] = Environment.getExternalStorageDirectory().getPath() + "/" + compareFiles[i] + ".jpg";
//
//            Mat compareImg = Imgcodecs.imread(compareFiles[i]);
//            MatOfKeyPoint compareKeyPoints = new MatOfKeyPoint();
//            Mat compareDescriptor = new Mat();
//
//            detector.detect(compareImg, compareKeyPoints);
//            descriptor.compute(compareImg, compareKeyPoints, compareDescriptor);
//
//            MatOfDMatch matches = new MatOfDMatch();
//            matcher.match(mainDescriptor, compareDescriptor, matches);
//
//            Mat outputImg = new Mat();
//            MatOfByte drawnMatches = new MatOfByte();
//            Features2d.drawMatches(img, keyPoints1, img, keyPoints2, matches, outputImg, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
//
//            Bitmap imageMatched = Bitmap.createBitmap(outputImg.cols(), outputImg.rows(), Bitmap.Config.RGB_565);//need to save bitmap
//            Utils.matToBitmap(outputImg, imageMatched);
//
//            Utilities.savePicture(Environment.getExternalStorageDirectory().getPath() + "/compare" + i, imageMatched);
//        }


    }
}
