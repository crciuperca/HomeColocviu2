package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyFtpServer extends AsyncTask<String, String, Void> {
    private TextView text;

    public MyFtpServer(TextView text) {
        this.text = text;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            socket = new Socket(params[0], Constants.FTP_PORT);
            Log.v(Constants.TAG, "Connected to: " + socket.getInetAddress() + ":" + socket.getLocalPort());
            BufferedReader bufferedReader = Utilities.getReader(socket);
            String line = bufferedReader.readLine();
            Log.v(Constants.TAG, "A line has been received from the FTP server: " + line);
            if (line != null && line.startsWith(Constants.FTP_MULTILINE_START_CODE)) {
                while ((line = bufferedReader.readLine()) != null) {
                    if (!Constants.FTP_MULTILINE_END_CODE1.equals(line) && !line.startsWith(Constants.FTP_MULTILINE_END_CODE2)) {
                        Log.v(Constants.TAG, "A line has been received from the FTP server: " + line);
                        publishProgress(line);
                    } else {
                        break;
                    }
                }
            }
        } catch (UnknownHostException unknownHostException) {
            Log.d(Constants.TAG, unknownHostException.getMessage());
            if (Constants.DEBUG) {
                unknownHostException.printStackTrace();
            }
        } catch (IOException ioException) {
            Log.d(Constants.TAG, ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
                Log.d(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        text.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progres) {
        text.append(progres[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}
}
