package mst.nsh9b3.securefaceauthentication;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.opencv.core.Mat;

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
}
