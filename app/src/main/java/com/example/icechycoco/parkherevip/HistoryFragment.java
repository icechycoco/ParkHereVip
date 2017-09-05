package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private static final String KEY_ID = "uId";
    private String uId;
    private static final String KEY_PO = "po";
    private String po;
    // connect db
    String response = null;
    getHttp http = new getHttp();

    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String uId,String po) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PO, po);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get variable
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
            po = bundle.getString(KEY_PO);
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "position : " + po, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_history, container, false);
        //final Button button = (Button) v.findViewById(R.id.button2);
        final TextView textView7 = (TextView) v.findViewById(R.id.textView7);
        final TextView textView8 = (TextView) v.findViewById(R.id.textView8);
        final TextView textView9 = (TextView) v.findViewById(R.id.textView9);
        final TextView textView10 = (TextView) v.findViewById(R.id.textView10);
        final TextView textView11 = (TextView) v.findViewById(R.id.textView11);
        final TextView textView12 = (TextView) v.findViewById(R.id.textView12);
        final ListView listView = (ListView) v.findViewById(R.id.listView1);

        String str =  getHis(uId);
        //Log.wtf("checkcheck : " , str);
        if(str.equals("end")){
            //TextView textView10 = (TextView) v.findViewById(R.id.textView10);
            textView10.setText("0");
        }else{
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

            CustomAdapter adapter = new CustomAdapter(getContext(), history);
//            ListView listView = (ListView) v.findViewById(R.id.listView1);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                }
            });

            //TextView textView10 = (TextView) v.findViewById(R.id.textView10);
            textView10.setText(history.size() + "");

            if(po.equals("1")){
                textView9.setText("0");
                textView12.setText("0");
            }
        }

        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str =  getHis(uId);
                //Log.wtf("checkcheck : " , str);
                if(str.equals("end")){
                    textView10.setText("0");
                }else{
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
                    CustomAdapter adapter = new CustomAdapter(getContext(), history);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        }
                    });
                    textView10.setText(history.size() + "");

                    if(po.equals("1")){
                        textView9.setText("0");
                        textView12.setText("0");
                    }
                }
            }
        });

        if(po.equals("2")){
            String str2 = getHisRes(uId);
            final ArrayList<HashMap<String, String>> a = imStaff(str2);
            textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAdapterHisRes adapterHisRes = new CustomAdapterHisRes(getContext(), a);
                listView.setAdapter(adapterHisRes);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    }
                });
            }
        });
            textView9.setText(a.size() + "");
            textView12.setText("0");
        }

        if(po.equals("3")){
            String str3 = getHisScan(uId);
            Log.wtf("check in if : ", str3);
            final ArrayList<HashMap<String, String>> b = imSecurity(str3);
            textView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAdapterHisSca adapterHisSca = new CustomAdapterHisSca(getContext(), b);
                    listView.setAdapter(adapterHisSca);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        }
                    });
                }
            });
            textView12.setText(b.size() + "");
            textView9.setText("0");
        }

        return v;
    }

    public String getHis(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/history.php?uId=" + uId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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

    public String getHisScan(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/historySca.php?secId=" + uId);
            Log.wtf("show his scan : ",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
                //dateRes = getInfo[5];
                dateRes = "04:09:17";

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
