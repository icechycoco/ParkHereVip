package com.example.icechycoco.parkherevip;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by icechycoco on 10/9/2017 AD.
 */

public class Tab1 extends Activity {

    // connect db
    String response = null;
    getHttp http =  new getHttp();
    String uId,po;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        po = getIntent().getStringExtra("po");
        uId = getIntent().getStringExtra("uId");
        Log.wtf("position id is :  ", po);
        Log.wtf("user id is :  ", uId);

        String str = getHis(uId);
        if (str.equals("end")) {
            TextView textView9 = (TextView) findViewById(R.id.textView9);
            textView9.setText("No parked history");
        } else {
            String[] getInfo;
            String parkName, timeI, timeO, date;
            ArrayList<HashMap<String, String>> history = null;
            history = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map, map2;

            Scanner scanner = new Scanner(str);

            for (int i = 0; scanner.hasNext(); i++) {
                String data = scanner.nextLine();
                System.out.println(data);

                getInfo = data.split(",");
                parkName = getInfo[0];
                timeI = getInfo[1];
                timeO = getInfo[2];
                date = getInfo[3];

                map = new HashMap<String, String>();
                map.put("pName", parkName);
                map.put("timeIn", timeI);
                map.put("timeO", timeO);
                map.put("date", date);
                history.add(map);
            }
            CustomAdapter adapter = new CustomAdapter(this, history);
            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                }
            });
        }
    }

    public String getHis(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/history.php?uId=" + uId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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