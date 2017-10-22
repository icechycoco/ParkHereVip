package com.example.icechycoco.parkherevip;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by icechycoco on 10/22/2017 AD.
 */

public class AvailableFragment extends Fragment {

    private static final String KEY_ID = "uId";
    private static String uId;

    // connect db
    String response = null;
    getHttp http = new getHttp();

    public AvailableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AvailableFragment newInstance(String uId) {
        AvailableFragment fragment = new AvailableFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();
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

    public ArrayList getParkArea(){

        ArrayList<HashMap<String, String>> reserve = null;
        reserve = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map2;
        String[] getInfo2;
        String pId2,res;

        String str2 = getCountRes();
        Scanner scan = new Scanner(str2);
        for(int j = 0; scan.hasNext(); j++){
            String data = scan.nextLine();
            System.out.println(data);
            getInfo2 = data.split(",");
            pId2 = getInfo2[0];
            res = getInfo2[1];
            map2 = new HashMap<String, String>();
            if((j+1)!=Integer.parseInt(pId2)){
                map2.put("pId2", j+1+"");
                map2.put("res", 0+"");
                reserve.add(map2);
            }else {
                map2.put("pId2", pId2);
                map2.put("res", res);
                reserve.add(map2);
            }
        }

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAvailable.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String str = "1,14Floor Building,100,0,13.650784,100.496006\n2,CB2,110,10,13.650784,100.496006";
        String str = response;
        String[] getInfo;
        String parkName,a,amount,la,lo,pId,pa,remain;

        ArrayList<HashMap<String, String>> parkinglot = null;

        parkinglot = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        Scanner scanner = new Scanner(str);
        for(int i = 0; scanner.hasNext(); i++) {
            int aa = 0;
            String data = scanner.nextLine();
            System.out.println(data);
            getInfo = data.split(",");
            pId = getInfo[0];
            parkName = getInfo[1];
            remain = getInfo[2];
            amount = getInfo[3];
            pa = getInfo[4];
            Log.wtf("getNumParkinglot: ", amount);

//            a = Integer.parseInt(amount) - Integer.parseInt(pa) + "";
            a = Integer.parseInt(remain) + "";
            if (str2.equals("0,0")) {
//                a = Integer.parseInt(amount) - Integer.parseInt(pa) + "";
                a = Integer.parseInt(remain) + "";
            } else if (reserve.get(i).get("pId2").toString().equals(pId)) {
//                a = (Integer.parseInt(amount) -
//                        Integer.parseInt(reserve.get(i).get("res").toString()) - Integer.parseInt(pa))+"";
                a = Integer.parseInt(remain) - Integer.parseInt(reserve.get(i).get("res").toString()) + "";
            }

            map = new HashMap<String, String>();
            map.put("pId", pId);
            map.put("pName", parkName);
            map.put("available", a);
            parkinglot.add(map);
        }

        return parkinglot;
    }

    public String getCountRes(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/countRes.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
