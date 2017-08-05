package com.example.icechycoco.parkherevip;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;
    EditText etUsername, etPassword;

    String getUsername;
    String getPassword;
    String[] getInfo;
    String position,uId;

    String response = null;
    getHttp http = new getHttp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);


        //getPassword = etPassword.getText().toString();
        //getUsername = etUsername.getText().toString();


        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getUsername = etUsername.getText().toString();
                getPassword = etPassword.getText().toString();

                try {
                    response = http.run("http://parkhere.sit.kmutt.ac.th/Login.php?username="+getUsername+"&password="+getPassword);

                } catch (IOException e) {

                    // TODO Auto-generat-ed catch block

                    e.printStackTrace();
                }

                getInfo = response.split(" ");
                position = getInfo[0];
                uId = getInfo[1];

                if(position.equals("1")){ // user: rodrin pass: 123456
                    Intent intent = new Intent(LoginActivity.this, HomeStuActivity.class);
                    intent.putExtra("uId", uId);
                    startActivity(intent);
                }else if(position.equals("2")){ //user : icechy pass :123456
                    Intent intent = new Intent(LoginActivity.this, HomeStaActivity.class);
                    intent.putExtra("uId", uId);
                    startActivity(intent);
                }else if(position.equals("3")){ // user:park13 pass:123456
                    Intent intent = new Intent(LoginActivity.this, HomeSecActivity.class);
                    intent.putExtra("uId", uId);
                    startActivity(intent);
                }else{
                    //show error
                }


            }

        });


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