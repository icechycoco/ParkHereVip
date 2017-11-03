package com.example.icechycoco.parkherevip;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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

    public static final String PREF_NAME = "PREF_NAME";
    public static final String P_NAME = "App_Config";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        SharedPreferences sp2 = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        String userId = sp2.getString("Username", "");
        String password = sp2.getString("Password", "");
        if(userId!=""){
            login(userId,password);
        }

        etPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                btLogin.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getUsername = etUsername.getText().toString();
                getPassword = etPassword.getText().toString();

                login(getUsername,getPassword);


//                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("username", getUsername);
//                editor.putString("password", getPassword);

//                SharedPreferences sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("Username", etUsername.getText().toString());
//                editor.putString("Password", etPassword.getText().toString());
//                editor.commit();

            }

        });
    }


    public void login(String user, String pass) {

        SharedPreferences sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Username", user);
        editor.putString("Password", pass);
        editor.commit();

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/Login.php?username="+user+"&password="+pass);

        } catch (IOException e) {

            // TODO Auto-generat-ed catch block

            e.printStackTrace();
        }

        if(response!=null) {
            if (response.equals("0")) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Wrong Username or Password");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            } else {
                getInfo = response.split(" ");
                position = getInfo[0];
                uId = getInfo[1];

                sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("UID", uId);
                editor.putBoolean("hasLoggedIn", true);
                editor.commit();
                boolean hasLoggedIn = sp.getBoolean("hasLoggedIn", false);


                if (position.equals("1")) { // user: student pass: 123456
                    Intent intent = new Intent(LoginActivity.this, HomeStuActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position.equals("2")) { //user : staff pass :123456
                    Intent intent = new Intent(LoginActivity.this, HomeStaActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position.equals("3")) { // user:guard pass:123456
                    Intent intent = new Intent(LoginActivity.this, HomeSecActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        }
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