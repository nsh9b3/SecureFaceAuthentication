package mst.nsh9b3.securefaceauthentication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nsh9b3 on 11/8/15.
 */
public class SplitImage
{
    private static final String TAG = "SFA::SplitImage";

    private int splits = 2;
    private File image = null;

    private ArrayList<Bitmap> chunkedImages;

    public SplitImage(File image)
    {
        this(2, image);
    }

    public SplitImage(int splits, File image)
    {
        Log.i(TAG, "SplitImage");

        this.splits = splits;
        this.image = image;
        this.chunkedImages = new ArrayList<Bitmap>(splits);

        split();
        createFiles();
    }

    private void split()
    {
        Log.i(TAG, "split");

        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        int rows = splits;
        int col = 1;
        int chunkHeight = bitmap.getHeight() / rows;
        int chunkWidth = bitmap.getWidth() / col;

        int yCoord = 0;
        for (int x = 0; x < rows; x++)
        {
            int xCoord = 0;
            for (int y = 0; y < col; y++)
            {
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
    }

    private void createFiles()
    {
        Log.i(TAG, "createFiles");

        String[] newFiles = new String[splits];

        int fileLength = image.getAbsolutePath().split("/").length;

        for (int i = 0; i < splits; i++)
        {
            newFiles[i] = image.getAbsolutePath().split("/")[fileLength - 1];
            newFiles[i] = newFiles[i].split("\\.")[0].concat("_" + i + ".jpg");
            newFiles[i] = Environment.getExternalStorageDirectory().getPath() + "/" + newFiles[i];

            FileOutputStream out = null;
            try
            {
                out = new FileOutputStream(newFiles[i]);

                chunkedImages.get(i).compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            } catch (Exception e)
            {
                e.printStackTrace();
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
        }
    }
}
