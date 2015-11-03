package mst.nsh9b3.securefaceauthentication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class TakePicture extends Activity
{
    private static final String TAG = "SFA::TakePicture";

    // This loads the libraries I need to run face detection
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");

                    System.loadLibrary("face_detection");
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreate");

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make sure Screen is not turned off
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug())
        {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else
        {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}
