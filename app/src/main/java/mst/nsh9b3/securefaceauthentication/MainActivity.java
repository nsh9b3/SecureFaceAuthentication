package mst.nsh9b3.securefaceauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FileTransfer.AsyncResponse
{
    private static final String TAG = "SFA::MainActivity";

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor sharedPreferencesEditor;

    public static String savedImageName = "Face";
    public static String savedFTPAddress = "192.168.1.1";
    public static String savedFTPPort = "21";
    public static String savedUsername = "Anon";
    public static String savedPassword = "";

    private ImageView imageView;

    private File temporaryFile;
    private Bitmap savedBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Saved Values
        sharedPreferences = this.getPreferences(MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        savedImageName = sharedPreferences.getString(getString(R.string.saved_image_name), savedImageName);
        savedFTPAddress = sharedPreferences.getString(getString(R.string.ftp_address), savedFTPAddress);
        savedFTPPort = sharedPreferences.getString(getString(R.string.ftp_port), savedFTPPort);
        savedUsername = sharedPreferences.getString(getString(R.string.username), savedUsername);
        savedPassword = sharedPreferences.getString(getString(R.string.password), savedPassword);

        // Set a Default picture for screen
        imageView = (ImageView) findViewById(R.id.Image_View);
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.i(TAG, "onConfigurationChanged");

        super.onConfigurationChanged(newConfig);

        if(savedBitmap != null)
        {
            imageView.setImageBitmap(savedBitmap);
        }
        else
        {
            // Set a Default picture for screen
            imageView = (ImageView) findViewById(R.id.Image_View);
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public void onOptionsClick(View view)
    {
        Log.i(TAG, "onOptionsClick");

        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        startActivity(optionsIntent);
    }

    public void onTakeClick(View view)
    {
        Log.i(TAG, "onTakeClick");

        Intent takeIntent = new Intent(this, TakePicture.class);
        startActivityForResult(takeIntent, 1);
    }

    public void onSendClick(View view)
    {
        Log.i(TAG, "onSendClick");

//        DetectFeatures detectFeatures = new DetectFeatures(temporaryFile.getAbsolutePath());
//        DetectFeatures detectFeatures = new DetectFeatures(null);


//        if(temporaryFile != null)
//        {
//            if (temporaryFile.exists())
//            {
////                SplitImage splitImage = new SplitImage(2, temporaryFile);
////                String[] splitFiles = splitImage.getNewFiles();
//
//                FileTransfer fileTransfer = new FileTransfer(temporaryFile.getAbsolutePath(), this);
//                fileTransfer.execute();
//
////                for (int i = 0; i < splitFiles.length; i++)
////                {
////                    File splitFile = new File(splitFiles[i]);
////                    fileTransfer = new FileTransfer(splitFile.getAbsolutePath(), this);
////                    fileTransfer.execute();
////                }
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        byte[] bytes;
        if(resultCode == RESULT_OK)
        {
            bytes = data.getByteArrayExtra("bytes");
            savedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if(savedBitmap != null)
            {
                imageView.setImageBitmap(savedBitmap);
            }
            Utilities.createHistogram(savedBitmap);
        }
    }

    @Override
    public void processFinish(String output)
    {
        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
    }
}
