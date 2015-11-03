package mst.nsh9b3.securefaceauthentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity
{
    private static final String TAG = "SFA::OptionsActivity";

    private static EditText editSavedName;
    private static EditText editFtpAddress;
    private static EditText editFtpPort;
    private static EditText editUsername;
    private static EditText editPassword;

    private static TextView textSavedName;
    private static TextView textFtpAddress;
    private static TextView textFtpPort;
    private static TextView textUsername;

    private static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Get all the EditText Views
        editSavedName = (EditText)findViewById(R.id.Saved_Name);
        editFtpAddress = (EditText)findViewById(R.id.FTP_Address);
        editFtpPort = (EditText)findViewById(R.id.FTP_Port);
        editUsername = (EditText)findViewById(R.id.FTP_Username);
        editPassword = (EditText)findViewById(R.id.FTP_Password);

        //Get all the TextViews
        textSavedName = (TextView)findViewById(R.id.Text_Saved_Name);
        textFtpAddress = (TextView)findViewById(R.id.Text_FTP_Address);
        textFtpPort = (TextView)findViewById(R.id.Text_FTP_Port);
        textUsername = (TextView)findViewById(R.id.Text_Username);

        //Set TextViews to correct initial text
        textSavedName.setText(MainActivity.sharedPreferences.getString(getString(R.string.saved_image_name), MainActivity.savedImageName));
        textFtpAddress.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_address), MainActivity.savedFTPAddress));
        textFtpPort.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_port), MainActivity.savedFTPPort));
        textUsername.setText(MainActivity.sharedPreferences.getString(getString(R.string.username), MainActivity.savedUsername));
    }

    public void onSetName(View view)
    {
        Log.i(TAG, "onSetName");

        textSavedName.setText(editSavedName.getText());
        editSavedName.setText("");
    }

    public void onClearName(View view)
    {
        Log.i(TAG, "onClearName");

        editSavedName.setText("");
    }

    public void onDefaultName(View view)
    {
        Log.i(TAG, "onDefaultName");

        textSavedName.setText(MainActivity.sharedPreferences.getString(getString(R.string.saved_image_name), MainActivity.savedImageName));
    }

    public void onSetAddress(View view)
    {
        Log.i(TAG, "onSetAddress");

        textFtpAddress.setText(editFtpAddress.getText());
        editFtpAddress.setText("");
    }

    public void onClearAddress(View view)
    {
        Log.i(TAG, "onClearAddress");

        editFtpAddress.setText("");
    }

    public void onDefaultAddress(View view)
    {
        Log.i(TAG, "onDefaultAddress");

        textFtpAddress.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_address), MainActivity.savedFTPAddress));
    }

    public void onSetPort(View view)
    {
        Log.i(TAG, "onSetPort");

        textFtpPort.setText(editFtpPort.getText());
        editFtpPort.setText("");
    }

    public void onClearPort(View view)
    {
        Log.i(TAG, "onClearPort");

        editFtpPort.setText("");
    }

    public void onDefaultPort(View view)
    {
        Log.i(TAG, "onDefaultPort");

        textFtpPort.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_port), MainActivity.savedFTPPort));
    }

    public void onSetUsername(View view)
    {
        Log.i(TAG, "onSetUsername");

        textUsername.setText(editUsername.getText());
        editUsername.setText("");
    }

    public void onClearUsername(View view)
    {
        Log.i(TAG, "onClearUsername");

        editUsername.setText("");
    }

    public void onDefaultUsername(View view)
    {
        Log.i(TAG, "onDefaultUsername");

        textUsername.setText(MainActivity.sharedPreferences.getString(getString(R.string.username), MainActivity.savedUsername));
    }

    public void onSetPassword(View view)
    {
        Log.i(TAG, "onSetPassword");

        password = editPassword.getText().toString();
        editPassword.setText("");
    }

    public void onClearPassword(View view)
    {
        Log.i(TAG, "onClearPassword");

        editPassword.setText("");
    }


    public void onLoadDefaults(View view)
    {
        Log.i(TAG, "onLoadDefaults");

        textSavedName.setText(MainActivity.sharedPreferences.getString(getString(R.string.saved_image_name), MainActivity.savedImageName));
        textFtpAddress.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_address), MainActivity.savedFTPAddress));
        textFtpPort.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_port), MainActivity.savedFTPPort));
        textUsername.setText(MainActivity.sharedPreferences.getString(getString(R.string.username), MainActivity.savedUsername));
    }

    public void onSaveAsDefaults(View view)
    {
        Log.i(TAG, "onSaveAsDefaults");

        MainActivity.sharedPreferencesEditor.putString(getString(R.string.saved_image_name), textSavedName.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.ftp_address), textFtpAddress.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.ftp_port), textFtpPort.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.username), textUsername.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.password), password);
        MainActivity.sharedPreferencesEditor.commit();
    }
}
