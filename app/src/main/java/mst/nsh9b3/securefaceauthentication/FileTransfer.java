package mst.nsh9b3.securefaceauthentication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by nsh9b3 on 11/3/15.
 */
public class FileTransfer extends AsyncTask
{
    private static final String TAG = "SFA::FileTransfer";

    private String filename;
    private FTPClient ftpClient;

    public FileTransfer(String filename)
    {
        this.filename = filename;
    }

    @Override
    protected Object doInBackground(Object[] params)
    {
        Log.i(TAG, "doInBackground");

        if (connectToServer())
        {
            if(transferFile())
            {

            }
        }
        return null;
    }

    public boolean connectToServer()
    {
        Log.i(TAG, "connectToServer");

        ftpClient = new FTPClient();
        try
        {
            ftpClient.connect(MainActivity.savedFTPAddress, Integer.parseInt(MainActivity.savedFTPPort));
            Log.i(TAG, "Connected to Server");

            if (!ftpClient.login(MainActivity.savedUsername, MainActivity.savedPassword))
            {
                Log.e(TAG, "Unable to log in");

                ftpClient.logout();
                ftpClient.disconnect();

                return false;
            } else
            {
                Log.i(TAG, "Successfully logged in");
            }
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                Log.e(TAG, "Error - Reply Code: " + reply);

                ftpClient.logout();
                ftpClient.disconnect();

                return false;
            } else
            {
                Log.i(TAG, "Successfully connected + logged into server");

                return true;
            }

        } catch (Exception e)
        {
            Log.e(TAG, "Error: " + e.getMessage());

            return false;
        }
    }

    public boolean transferFile()
    {
        Log.i(TAG, "transferFile");

        try
        {
            InputStream input = new FileInputStream(filename);
            ftpClient.storeFile(MainActivity.savedImageName, input);

            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                Log.e(TAG, "Error - Reply Code: " + reply);

                ftpClient.logout();
                ftpClient.disconnect();

                return false;
            } else
            {
                Log.i(TAG, "Successfully transferred: " + filename);

                input.close();

                ftpClient.logout();
                ftpClient.disconnect();

                return true;
            }
        } catch (Exception e)
        {
            Log.e(TAG, "Error: " + e.getMessage());

            return false;
        }

    }
}
