package com.example.icechycoco.parkherevip;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    // gui
    Button btLogin;
    EditText etUsername, etPassword;
    // connect db
    String response = null;
    getHttp http = new getHttp();
    // variables
    String getUsername;
    String getPassword;
    String[] getInfo;
    String position,uId;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);

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

                if(response.equals("0")){
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Wrong Username or Password");
                    builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.show();
                }else {
                    getInfo = response.split(" ");
                    position = getInfo[0];
                    uId = getInfo[1];

                    sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                    editor = sp.edit();
                    editor.putString("UID", uId);
                    editor.commit();

                    if (position.equals("1")) { // user: rodrin pass: 123456
                        Intent intent = new Intent(LoginActivity.this, HomeStuActivity.class);
                        startActivity(intent);
                    } else if (position.equals("2")) { //user : icechy pass :123456
                        Intent intent = new Intent(LoginActivity.this, HomeStaActivity.class);
                        startActivity(intent);
                    } else if (position.equals("3")) { // user:park13 pass:123456
                        Intent intent = new Intent(LoginActivity.this, HomeSecActivity.class);
                        startActivity(intent);
                    } else {

                    }
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