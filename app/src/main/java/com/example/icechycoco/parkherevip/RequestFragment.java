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
import android.widget.ArrayAdapter;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment {

    private static final String KEY_ID = "uId";
    private static String uId;

    // connect db
    String response = null;
    getHttp http = new getHttp();
    private OnFragmentInteractionListener mListener;

    private ArrayList<String> parkArea = new ArrayList<String>();
    private Spinner spinner;
    private String filterParkArea;
    private String str;

    public RequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RequestFragment newInstance(String uId) {
        RequestFragment fragment = new RequestFragment();
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
    }

    public void setuId(String uId){
        this.uId = uId;
    }

    public String getuId(){
        return this.uId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_request, container, false);
        //btn = (Button) v.findViewById(R.id.btn_req);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        createThaiClubData();
        ArrayAdapter<String> adapterThai = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, parkArea);
        spinner.setAdapter(adapterThai);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    str = getReq2();
                    CustomAdapterReq adapter = new CustomAdapterReq(getContext(), showHis(str));
                    ListView listView = (ListView) v.findViewById(R.id.listView1);
                    TextView textView9 = (TextView) v.findViewById(R.id.textView9);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        }
                    });
                    listView.setVisibility(View.VISIBLE);
                    textView9.setVisibility(View.INVISIBLE);
                }else {
                    ListView listView = (ListView) v.findViewById(R.id.listView1);
                    TextView textView9 = (TextView) v.findViewById(R.id.textView9);
                    str = getReq(position + "");
                    if(str.equals("0 ")){
                        textView9.setText("No request(s) at this park area");
                        listView.setVisibility(View.INVISIBLE);
                        textView9.setVisibility(View.VISIBLE);
                    }else {
                        CustomAdapterReq adapter = new CustomAdapterReq(getContext(), showHis(str));
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                            }
                        });
                        listView.setVisibility(View.VISIBLE);
                        textView9.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setuId(uId);
        Log.wtf("check uId "+uId+"..."+this.uId , getuId());
        return v;
    }

    public ArrayList showHis(String str){
        String[] getInfo;
        String parkName,interval,licen,date,code;
        String timeInt = null;
        ArrayList<HashMap<String, String>> history = null;

        history = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        Scanner scanner = new Scanner(str);

        for(int i = 0; scanner.hasNext(); i++){
            String data = scanner.nextLine();
            System.out.println(data);

            getInfo = data.split(",");
            parkName = getInfo[0];
            date = getInfo[1];
            interval = getInfo[2];
            licen = getInfo[3];
            code = getInfo[4];

            map = new HashMap<String, String>();
            map.put("pName", parkName);
            map.put("date", date);
            map.put("timeInt", interval);
            map.put("licen", licen);
            map.put("code", code);
            history.add(map);
        }

        return history;
    }

    private void createThaiClubData() {

        parkArea.add("ALL");
        parkArea.add("CB4");
        parkArea.add("FIBO");
        parkArea.add("14 Floors Building");
    }

    public String getReq(String str){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/req.php?pId="+str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getReq2(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/req2.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
     * <p>
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
