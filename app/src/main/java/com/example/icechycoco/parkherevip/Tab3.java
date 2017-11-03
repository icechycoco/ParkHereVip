package com.example.icechycoco.parkherevip;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

public class Tab3 extends Activity {

    // connect db
    String response = null;
    getHttp http =  new getHttp();
    String po,uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        po = getIntent().getStringExtra("po");
        uId = getIntent().getStringExtra("uId");
        Log.wtf("position id is :  ", po);
        Log.wtf("user id is :  ", uId);

        if(po.equals("3")){
            String str3 = getHisScan(uId);
            Log.wtf("check in if : ", str3);
            final ArrayList<HashMap<String, String>> b = imSecurity(str3);
            CustomAdapterHisSca adapterHisSca = new CustomAdapterHisSca(this, b);
            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.setAdapter(adapterHisSca);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                }
            });
        }else {
            TextView textView9 = (TextView) findViewById(R.id.textView9);
            textView9.setText("Security Guard only");
            final ImageView img = (ImageView) findViewById(R.id.imageView6);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.warningsign);
            img.setImageBitmap(bitmap);
            img.setVisibility(View.VISIBLE);
        }
    }

    public String getHisScan(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/historySca.php?secId=" + uId);
            Log.wtf("show his scan : ",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public ArrayList<HashMap<String, String>> imSecurity(String hisScan) {

        String[] getInfo;
        String parkName, date,timeScan, gFi, gLa, gLi;
        ArrayList<HashMap<String, String>> historySca = null;

        historySca = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        Scanner scanner = new Scanner(hisScan);

        if (hisScan.equals("0 ")) {
            historySca = null;
        }else {
            for (int i = 0; scanner.hasNext(); i++) {
                String data = scanner.nextLine();
                System.out.println(data);

                //14Floor Building,2017-08-30,14:04:00,icechy,coco
                getInfo = data.split(",");
                parkName = getInfo[0];
                date = getInfo[1];
                timeScan = getInfo[2];
                gFi = getInfo[3];
                gLa = getInfo[4];
                gLi = getInfo[5];

                map = new HashMap<String, String>();
                map.put("parkName", parkName);
                map.put("date", date);
                map.put("timeScan", timeScan);
                map.put("gFi", gFi);
                map.put("gLa", gLa);
                map.put("gLi", gLi);

                historySca.add(map);
            }
        }
        return historySca;
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