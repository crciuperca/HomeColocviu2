package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button b1;
    private Button b2;
    private EditText msg;
    private EditText portServer;
    private EditText t2;
    private EditText t3;
    private EditText city;
    private TextView tv;
    private MyServerThread serverThread;
    private Spinner dropdown;
    public static Context context;

    private Listener1 listener1 = new Listener1();
    private class Listener1 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            MyFtpServer ftpServerCommunicationAsyncTask = new MyFtpServer(tv);
//            ftpServerCommunicationAsyncTask.execute(portServer.getText().toString());
            if ("Server Start".equals(msg.getText().toString())) {
                serverThread = new MyServerThread(Integer.parseInt(portServer.getText().toString()), city, dropdown);
                serverThread.startServer();

                Log.v(Constants.TAG, "Starting server...");
            }
            if ("Server Stop".equals(msg.getText().toString())) {
                serverThread.stopServer();
                Log.v(Constants.TAG, "Stopping server...");
            }
        }
    }

    private Listener2 listener2 = new Listener2();
    private class Listener2 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            MyFtpServer ftpServerCommunicationAsyncTask = new MyFtpServer(tv);
//            ftpServerCommunicationAsyncTask.execute(portServer.getText().toString());
            ClientAsyncTask clientAsyncTask = new ClientAsyncTask(tv);
            clientAsyncTask.execute(t2.getText().toString(), t3.getText().toString());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serverThread.stopServer();
        }
    }

    private ServerTextContentWatcher serverTextContentWatcher = new ServerTextContentWatcher();
    private class ServerTextContentWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            Log.v(Constants.TAG, "Text changed in edit text: " + charSequence.toString());
            if ("Server Start".equals(charSequence.toString())) {

                serverThread = new MyServerThread(Integer.parseInt(portServer.getText().toString()), city, dropdown);
                serverThread.startServer();
                Log.v(Constants.TAG, "Starting server...");
            }
            if ("Server Stop".equals(charSequence.toString())) {
                serverThread.stopServer();
                Log.v(Constants.TAG, "Stopping server...");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        portServer = (EditText)findViewById(R.id.editText1);
        msg = (EditText)findViewById(R.id.editText);
        t2 = (EditText)findViewById(R.id.editText2);
        t3 = (EditText)findViewById(R.id.editText3);
        city = (EditText)findViewById(R.id.editText4);
        tv = (TextView) findViewById(R.id.textView);
        dropdown = (Spinner)findViewById(R.id.dropdown);

        String[] arrayDropdown = new String[] {"Temperature", "Humidity" , "POST"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayDropdown);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);

        b1.setOnClickListener(listener1);
        b2.setOnClickListener(listener2);

        portServer.setText("2017");
        t2.setText("127.0.0.1");
        t3.setText("2017");
        msg.setText("Server Start");
        city.setText("Bucharest");
        //portServer.addTextChangedListener(serverTextContentWatcher);
    }
}