package mst.nsh9b3.securefaceauthentication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by nick on 11/17/15.
 */
public class Utilities
{
    private static String TAG = "Utilities";

    public static boolean savePicture(String filename, Bitmap bitmap)
    {
        Log.i(TAG, "savePicture");
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean compare(Bitmap b1, Bitmap b2)
    {
        Log.i(TAG, "compare");

        if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight())
        {
            int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
            int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];
            b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
            b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());
            if (Arrays.equals(pixels1, pixels2))
            {
                return true;
            } else
            {
                Log.i(TAG, "Bitmaps have different pixels");
                return false;
            }
        } else
        {
            Log.i(TAG, "Bitmaps have different sizes");
            return false;
        }
    }

    public static void createHistogram(Bitmap bitmap, int histSizeNum)
    {
        Log.i(TAG, "createHistogram");

        Mat tmp = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitmap, tmp);

        MatOfInt channels = new MatOfInt(0);
        Mat hist = new Mat();
        MatOfInt mHistSize = new MatOfInt(histSizeNum);
        MatOfFloat mRanges = new MatOfFloat(0f, 256f);

        Imgproc.calcHist(Arrays.asList(tmp), channels, new Mat(), hist, mHistSize, mRanges);

//        for (int i = 0; i < 25; i++)
//        {
//            double[] histValues = hist.get(i, 0);
//            for (int j = 0; j < histValues.length; j++)
//            {
//                Log.d(TAG, "yourData=" + histValues[j]);
//            }
//        }
//
//        Bitmap histBitmap = Bitmap.createBitmap(hist.width(), hist.height(), Bitmap.Config.ARGB_8888);
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

    public static void createHistogram(Bitmap bitmap)
    {
        createHistogram(bitmap, 25);
    }
}
