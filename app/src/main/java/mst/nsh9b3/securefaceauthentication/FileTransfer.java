package mst.nsh9b3.securefaceauthentication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by nsh9b3 on 11/3/15.
 */
public class FileTransfer extends AsyncTask
{
    public interface AsyncResponse
    {
        void processFinish(String output);
    }

    private static final String TAG = "SFA::FileTransfer";

    private String filename;
    private FTPClient ftpClient;

    private AsyncResponse delegate = null;

    private String output;

    public FileTransfer(String filename, AsyncResponse delegate)
    {
        this.filename = filename;
        this.delegate = delegate;
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

    @Override
    protected void onPostExecute(Object o)
    {
        delegate.processFinish(output);
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
                output = "Unable to log in";
                Log.e(TAG, output);

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
                output = "Error - Reply Code: " + reply;
                Log.e(TAG, output);

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
            output = "Error: " + e.getMessage();
            Log.e(TAG, output);

            return false;
        }
    }

    public boolean transferFile()
    {
        Log.i(TAG, "transferFile");

        try
        {
            InputStream input = new FileInputStream(filename);
            String name = filename.split("/")[filename.split("/").length - 1];
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.storeFile(name, input);

            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                output = "Error - Reply Code: " + reply;
                Log.e(TAG, output);

                ftpClient.logout();
                ftpClient.disconnect();

                return false;
            } else
            {
                output = "Successfully transferred: " + filename;
                Log.i(TAG, output);

                input.close();

                ftpClient.logout();
                ftpClient.disconnect();

                return true;
            }
        } catch (Exception e)
        {
            output = "Error: " + e.getMessage();
            Log.e(TAG, output);

            return false;
        }

    }
}
