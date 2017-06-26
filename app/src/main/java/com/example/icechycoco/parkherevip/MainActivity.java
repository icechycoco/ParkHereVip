package com.example.icechycoco.parkherevip;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btLogin;
    EditText etUsername , etPassword;
    String response = null;
    getHttp http = new getHttp();

    String getUsername;
    String getPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);

        getPassword = etPassword.getText().toString();
        getUsername = etUsername.getText().toString();

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        btLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v) {

                try {
                    response = http.run("http://parkhere.sit.kmutt.ac.th/Login.php?username="+getUsername+"&password="+getPassword);
                } catch (IOException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();
                }
                Log.d("test1",response);

            }

        });

        //ConnectDB conn = new ConnectDB();

    }

    public class getHttp {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }

}