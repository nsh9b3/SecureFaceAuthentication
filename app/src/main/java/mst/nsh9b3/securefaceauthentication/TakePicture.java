package mst.nsh9b3.securefaceauthentication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePicture extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener
{
    //Class Tag
    private static final String TAG = "SFA::TakePicture";

    //Color of square drawn (r, g, b, a)
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);

    //Which type of detector to use... Native is better
    private static final int JAVA_DETECTOR = 0;
    private static final int NATIVE_DETECTOR = 1;
    private int mDetectorType = NATIVE_DETECTOR;

    //Front-facing camera or back-facing camera
    private static final int BACK_CAMERA = 0;
    private static final int FRONT_CAMERA = 1;

    //Unique Camera View
    private OpenCvCameraView mCameraView;

    //Cascade Classifier file
    private File mCascadeFile;

    //How to load said file
    private DetectionBasedTracker mNativeDetector;
    private CascadeClassifier mJavaDetector;

    //Type of images created
    private Mat mRgba;
    private Mat mGray;

    //Bitmap of face - Gray Scale
    Bitmap mBitmap;
    Bitmap savedBitmap;
    String filename;

    //Size of face compared to rest of image
    private float mRelativeFaceSize = 0.3f;
    private int mAbsoluteFaceSize = 0;

    //Loads the cascade classifier file
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d(TAG, "OpenCV loaded successfully");
                    System.loadLibrary("face_detection");
                    try
                    {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1)
                        {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty())
                        {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
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

        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        mCameraView = (OpenCvCameraView) findViewById(R.id.Camera_View);
        mCameraView.setCameraIndex(FRONT_CAMERA);
        mCameraView.setVisibility(SurfaceView.VISIBLE);
        mCameraView.setCvCameraViewListener(this);
        mCameraView.enableView();
        mCameraView.setOnTouchListener(this);

        filename = "";
    }

    @Override
    public void onPause()
    {
        Log.i(TAG, "onPause");

        super.onPause();
        if (mCameraView != null)
            mCameraView.disableView();
    }

    public void onDestroy()
    {
        Log.i(TAG, "onDestroy");

        super.onDestroy();
        if (mCameraView != null)
            mCameraView.disableView();
    }


    @Override
    public void onCameraViewStarted(int width, int height)
    {
        Log.i(TAG, "onCameraViewStarted");

        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped()
    {
        Log.i(TAG, "onCameraViewStopped");

        mGray.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        mBitmap = null;

        if (mAbsoluteFaceSize == 0)
        {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0)
            {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mDetectorType == JAVA_DETECTOR)
        {
            if (mJavaDetector != null)
                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2,
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        } else if (mDetectorType == NATIVE_DETECTOR)
        {
            if (mNativeDetector != null)
                mNativeDetector.detect(mGray, faces);
        } else
        {
            Log.e(TAG, "Detection method is not selected!");
        }

        Rect[] facesArray = faces.toArray();

        for (int i = 0; i < facesArray.length; i++)
        {
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 1);
            Mat m = new Mat();
            Rect r = facesArray[i];

            m = mGray.submat(r);
            mBitmap = Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(m, mBitmap);
        }

        return mRgba;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        Log.i(TAG, "onTouch");

        if (mBitmap != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("_MM-dd-yyyy_HH-mm-ss");
            String currentDateandTime = sdf.format(new Date());

            filename = Environment.getExternalStorageDirectory().getPath() + "/" + MainActivity.savedImageName + currentDateandTime + ".jpg";
            //filename = this.getExternalCacheDir() + MainActivity.savedImageName + currentDateandTime + ".jpg";

            savedBitmap = mBitmap;
            //Utilities.savePicture(filename, mBitmap);
            Toast.makeText(this, "bitmap saved", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onBackPressed()
    {
        Log.i(TAG, "onBackPressed");

        Intent exitIntent = new Intent();
        exitIntent.putExtra("bytes", compressBitmap(savedBitmap));

        setResult(RESULT_OK, exitIntent);
        finish();
    }

    private byte[] compressBitmap(Bitmap compressBitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        compressBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        return bytes;
    }
}