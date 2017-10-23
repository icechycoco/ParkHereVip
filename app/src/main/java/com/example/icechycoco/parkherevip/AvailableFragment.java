package com.example.icechycoco.parkherevip;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * Created by icechycoco on 10/22/2017 AD.
 */

public class AvailableFragment extends Fragment {

    private static final String KEY_ID = "uId";
    private static String uId;
    private static final String KEY_PO = "po";
    private static String po;

    //variable
    String[] getInfo;
    String getTime,timeIn;
    int getCost,getMCost;
    String currentTime,getFloor;
    Date date1,date2;
    long realFee;
    long diffHours = 0;
    int level,sta;


    // connect db
    String response = null;
    getHttp http = new getHttp();

    public AvailableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AvailableFragment newInstance(String uId,String po) {
        AvailableFragment fragment = new AvailableFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PO, po);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
            po = bundle.getString(KEY_PO);
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();

        String str = getLev(uId);
        String[] getInfo;
        getInfo = str.split(",");
        level = Integer.parseInt(getInfo[0]);
        sta = Integer.parseInt(getInfo[1]);

        if(sta==1) {

            try {
                response = http.run("http://parkhere.sit.kmutt.ac.th/estimate.php?uId=" + uId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.equals("0")) {

            } else {

                getInfo = response.split(" ");
                getTime = getInfo[0];
                getCost = Integer.parseInt(getInfo[1]);
                getMCost = Integer.parseInt(getInfo[2]);
                getFloor = getInfo[3];

                // convert string time in database to time
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(getTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeIn = sdf.format(date);

                //current time
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                currentTime = sdf2.format(cal.getTime());

                //calculate diff time
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                try {
                    date1 = format.parse(timeIn);
                    date2 = format.parse(currentTime);
                    long diff = date2.getTime() - date1.getTime();
                    System.out.println(diff);
                    diffHours = diff / (60 * 60 * 1000);
                    System.out.println("current time " + date2);
                    System.out.print(diffHours + " hours, ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //calculate fee
                long fee = diffHours * getCost;
                if (fee > getMCost) {
                    realFee = getMCost;
                    System.out.println(getMCost);
                } else {
                    realFee = fee;
                    System.out.println(fee);
                }

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_fee);

                final TextView tin = (TextView) dialog.findViewById(R.id.textView13);
                final TextView tout = (TextView) dialog.findViewById(R.id.textView15);
                final TextView estfee = (TextView) dialog.findViewById(R.id.textView17);
                final TextView floor = (TextView) dialog.findViewById(R.id.textView19);
                tin.setText(timeIn);
                tout.setText(currentTime);
                estfee.setText(realFee + " BAHT");
                floor.setText(getFloor);

                dialog.show();

            }
        }
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                showActivity();
//                                showStatus2();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_available, container, false);
        CustomAdapterParkArea adapter = new CustomAdapterParkArea(getContext(), getParkArea());
        ListView listView = (ListView) v.findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });


        return v;
    }

    public void setuId(String uId){
        this.uId = uId;
    }

    public String getuId(){
        return this.uId;
    }

    public void setPo(String po){
        this.po = po;
    }

    public String getPo(){
        return this.po;
    }

    public String getLev(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/Level.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public ArrayList getParkArea(){

        ArrayList<HashMap<String, String>> reserve = null;
        reserve = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map2;
        String[] getInfo2;
        int pId2;
        int res,remain;
        String[] getInfo;
        String parkName,a,pId;

        String str = getAvailable();
        String str2 = getCountRes();

        Scanner scanner = new Scanner(str);
        Scanner scan = new Scanner(str2);

        for(int j = 0; scan.hasNext(); j++){
            String data = scan.nextLine();
            System.out.println(data);
            getInfo2 = data.split(",");
            pId2 = Integer.parseInt(getInfo2[0]);
            res = Integer.parseInt(getInfo2[1]);
            //nRes.add(pId2, res);

            String data2 = scanner.nextLine();
            System.out.println(data2);
            getInfo = data2.split(",");
            pId = getInfo[0];
            parkName = getInfo[1];
            remain = Integer.parseInt(getInfo[2]);

            map2 = new HashMap<String, String>();
            map2.put("pId", pId);
            map2.put("pName", parkName);
            map2.put("available", remain - res + "" );
            reserve.add(map2);
        }

        return reserve;
    }

    public String getCountRes(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/countRes.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getAvailable(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAvailable.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getCurrentTime(){
        //current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf2.format(cal.getTime());
    }

    public interface OnFragmentInteractionListener {
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
