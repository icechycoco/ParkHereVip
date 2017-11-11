package com.example.icechycoco.parkherevip;

/**
 * Created by icechycoco on 8/26/2017 AD.
 */

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, RequestFragment.OnFragmentInteractionListener{

    private ZXingScannerView mScannerView;
    // connect db
    String response = null;
    getHttp http = new getHttp();

    String[] getInfo;
    String fN,lN,licen,date,parkN,gName,pId,resId,checkCode,setTimeScan;
    int timeInter;
    String timeSeq = "";
    String uId,getCode;
    String[] getInfo1;
    String[] month = {"null","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String d,m,mm,y,newDate;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        uId = getIntent().getStringExtra("uId");
        getCode = getIntent().getStringExtra("code");
        Log.wtf("get code : ",getCode);
        Log.wtf("get uId : ", uId);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        //getCode = etCode.getText().toString();

        if(getCode.equals(rawResult.getText())) {

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/resCon.php?code=" + getCode);

        } catch (IOException e) {

            // TODO Auto-generat-ed catch block

            e.printStackTrace();
        }
        getInfo = response.split(",");
        fN = getInfo[0];
        lN = getInfo[1];
        gName = fN + " " + lN;
        licen = getInfo[2];
        date = getInfo[3];
        timeInter = Integer.parseInt(getInfo[4]);
        parkN = getInfo[5];
        resId = getInfo[6];
        pId = getInfo[7];

        if (timeInter == 0) {
            timeSeq = "06:00 - 12:00";
        } else if (timeInter == 10) {
            timeSeq = "13:00 - 18:00";
        } else if (timeInter == 11) {
            timeSeq = "06:00 - 18:00";
        } else {
            timeSeq = "wrong time";
        }

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage("\nKhun " + gName + "\n\nPark Area :  " + parkN + "\n\nLicense :  " + licen + "\n\nDate :     " + changeDateFormat(date) + "\n\nTime :     " + timeSeq);

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                //current time
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                setTimeScan = sdf2.format(cal.getTime());

                try {
                    response = http.run("http://parkhere.sit.kmutt.ac.th/confirmRes.php?reserveId=" + resId + "&pId=" + pId + "&timeScan=" + setTimeScan+ "&secId=" + uId);
                } catch (IOException e) {
                    // TODO Auto-generat-ed catch block
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "PARKED HERE", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        builder.show();

    }else{

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage("INVALID CODE");
        builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                onBackPressed();
            }
        });
        builder.show();
    }
        //QRScanFragment.etCode.setText(rawResult.getText());
        //onBackPressed();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public String changeDateFormat(String str){
        getInfo1 = str.split("-");
        y = getInfo1[0];
        m = getInfo1[1];
        d = getInfo1[2];
        for(int i=0;i<month.length;i++){
            if(i==Integer.parseInt(m)){
                mm = month[i];
                break;
            }
        }
        newDate = d + " " + mm + " " + y;
        return newDate;
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
