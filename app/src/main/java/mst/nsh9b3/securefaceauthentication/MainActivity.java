package mst.nsh9b3.securefaceauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "SFA::MainActivity";

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor sharedPreferencesEditor;

    public static String savedImageName = "Face";
    public static String savedFTPAddress = "192.168.1.1";
    public static String savedFTPPort = "21";
    public static String savedUsername = "Anon";
    public static String savedPassword = "";

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
        startActivity(takeIntent);
    }

    public void onSendClick(View view)
    {
        Log.i(TAG, "onSendClick");

        try
        {
            File temp = File.createTempFile("prefix", ".jpg", getExternalCacheDir());

            FileTransfer fileTransfer = new FileTransfer(temp.getAbsolutePath());
            fileTransfer.execute();
        }
        catch (Exception e)
        {

        }
    }
}
