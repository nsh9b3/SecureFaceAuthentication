package mst.nsh9b3.securefaceauthentication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * Created by nick on 11/19/15.
 */
public class CreateHistogram
{
    private static final String TAG = "SFA::CreateHistogram";

    private Bitmap savedBitmap;
    private int radius = 1;
    private int neighbors = 8;
    private int gridX = 8;
    private int gridY = 8;

    private int histSizeNum = 25;

    public CreateHistogram(Bitmap bitmap)
    {
        Log.i(TAG, "CreateHistogram");
        this.savedBitmap = bitmap;

        Mat tmp = new Mat(savedBitmap.getHeight(), savedBitmap.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitmap, tmp);

        MatOfInt channels = new MatOfInt(0);
        Mat hist = new Mat();
        MatOfInt mHistSize = new MatOfInt(histSizeNum);
        MatOfFloat mRanges = new MatOfFloat(0f, 256f);

        Imgproc.calcHist(Arrays.asList(tmp), channels, new Mat(), hist, mHistSize, mRanges);

        Bitmap histBitmap = Bitmap.createBitmap(hist.width(), hist.height(), Bitmap.Config.ARGB_8888);
//
//        Utilities.savePicture(Environment.getExternalStorageDirectory().getPath() + "/hist.png", histBitmap);
//
//        Bitmap test = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/hist.png");
//
//        if(Utilities.compare(test, histBitmap))
//        {
//            Log.i(TAG, "SAME");
//        }
//        else
//        {
//            Log.i(TAG, "DIFFERENT");
//        }
    }
}
