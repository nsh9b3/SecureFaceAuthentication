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

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make sure Screen is not turned off
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
    }

    public void onSetName(View view)
    {
        textSavedName.setText(editSavedName.getText());
        editSavedName.setText("");
    }

    public void onClearName(View view)
    {
        editSavedName.setText("");
    }

    public void onDefaultName(View view)
    {
        textSavedName.setText(MainActivity.sharedPreferences.getString(getString(R.string.saved_image_name), MainActivity.savedImageName));
    }

    public void onSetAddress(View view)
    {
        textFtpAddress.setText(editFtpAddress.getText());
        editFtpAddress.setText("");
    }

    public void onClearAddress(View view)
    {
        editFtpAddress.setText("");
    }

    public void onDefaultAddress(View view)
    {
        textFtpAddress.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_address), MainActivity.savedFTPAddress));
    }

    public void onSetPort(View view)
    {
        textFtpPort.setText(editFtpPort.getText());
        editFtpPort.setText("");
    }

    public void onClearPort(View view)
    {
        editFtpPort.setText("");
    }

    public void onDefaultPort(View view)
    {
        textFtpPort.setText(MainActivity.sharedPreferences.getString(getString(R.string.ftp_port), MainActivity.savedFTPPort));
    }

    public void onSetUsername(View view)
    {
        textUsername.setText(editUsername.getText());
        editUsername.setText("");
    }

    public void onClearUsername(View view)
    {
        editUsername.setText("");
    }

    public void onDefaultUsername(View view)
    {
        textUsername.setText(MainActivity.sharedPreferences.getString(getString(R.string.username), MainActivity.savedUsername));
    }

    public void onSetPassword(View view)
    {
        password = editPassword.getText().toString();
        editPassword.setText("");
    }

    public void onClearPassword(View view)
    {
        editPassword.setText("");
    }


    public void onLoadDefaults(View view)
    {

    }

    public void onSaveAsDefaults(View view)
    {
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.saved_image_name), textSavedName.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.ftp_address), textFtpAddress.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.ftp_port), textFtpPort.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.username), textUsername.getText().toString());
        MainActivity.sharedPreferencesEditor.putString(getString(R.string.saved_image_name), textSavedName.getText().toString());
    }

    public void onSaveValues(View view)
    {

    }
}
