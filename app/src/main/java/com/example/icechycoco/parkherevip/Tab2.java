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

public class Tab2 extends Activity {

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

        if(po.equals("2")){
            String str2 = getHisRes(uId);
            final ArrayList<HashMap<String, String>> a = imStaff(str2);
            CustomAdapterHisRes adapterHisRes = new CustomAdapterHisRes(this, a);
            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.setAdapter(adapterHisRes);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                }
            });
        }else {
            TextView textView9 = (TextView) findViewById(R.id.textView9);
            textView9.setText("Available for Staff only");
        }
    }

    public String getHisRes(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/historyRes.php?uId=" + uId);
            Log.wtf("his res : " ,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public ArrayList<HashMap<String, String>> imStaff(String hisRes){
        String[] getInfo, guestInfo;
        String parkName, date, timeInter, timeRes, dateRes, gId, gFi, gLa, gLi;
        ArrayList<HashMap<String, String>> historyRes = null;

        historyRes = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        Scanner scanner = new Scanner(hisRes);

        if(hisRes.equals("0 ")){
            historyRes = null;
        }else {

            for (int i = 0; scanner.hasNext(); i++) {
                String data = scanner.nextLine();
                System.out.println(data);

                getInfo = data.split(",");
                gId = getInfo[0];
                parkName = getInfo[1];
                date = getInfo[2];
                timeInter = getInfo[3];
                timeRes = getInfo[4];
                dateRes = getInfo[5];
//                dateRes = "04:09:17";

                map = new HashMap<String, String>();
                //map.put("gId", gId);
                map.put("parkName", parkName);
                map.put("date", date);
                map.put("timeInter", timeInter);
                map.put("timeRes", timeRes);
                map.put("dateRes", dateRes);
                Log.wtf("gid res : " ,gId);

                String g = gInfo(gId);
                guestInfo = g.split(",");
                gFi = guestInfo[0];
                gLa = guestInfo[1];
                gLi = guestInfo[2];

                map.put("gFi", gFi);
                map.put("gLa", gLa);
                map.put("gLi", gLi);

                historyRes.add(map);
            }
        }
        return historyRes;
    }

    public String gInfo(String gId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/guestInfo.php?gId=" + gId);
            Log.wtf("guest info : ",response);
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