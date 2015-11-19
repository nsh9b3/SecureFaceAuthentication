package mst.nsh9b3.securefaceauthentication;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
}
