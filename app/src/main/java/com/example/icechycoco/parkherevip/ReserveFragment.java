package com.example.icechycoco.parkherevip;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReserveFragment extends Fragment {

    String response = null;
    getHttp http = new getHttp();

    private static final String KEY_ID = "uId";
    private static String uId;

    private OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;

    public ReserveFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReserveFragment newInstance(String uId) {
        ReserveFragment fragment = new ReserveFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setuId(String uId){
        this.uId = uId;
    }

    public String getuId(){
        return this.uId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_reserve_info, container, false);
//        int[] resId = { R.drawable.aerithgainsborough
//                , R.drawable.barretwallace, R.drawable.caitsith
//                , R.drawable.cidhighwind, R.drawable.cloudstrife
//                , R.drawable.redxiii, R.drawable.sephiroth
//                , R.drawable.tifalockhart, R.drawable.vincentvalentine
//                , R.drawable.yuffiekisaragi, R.drawable.zackfair };
//
//        String[] list = { "Aerith Gainsborough", "Barret Wallace", "Cait Sith"
//                , "Cid Highwind", "Cloud Strife", "RedXIII", "Sephiroth"
//                , "Tifa Lockhart", "Vincent Valentine", "Yuffie Kisaragi"
//                , "ZackFair" };

        String str = getParkName();

        ArrayList al = new ArrayList();

        Scanner scanner = new Scanner(str);

        for(int i = 0; scanner.hasNext(); i++){
            String data = scanner.nextLine();
            al.add(data);
        }

        final CustomAdapterRes adapter = new CustomAdapterRes(getContext(),al);

        ListView listView = (ListView) v.findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });
        setuId(uId);
        Log.wtf("check uId2 "+uId+"..."+this.uId , getuId());


        return v;

    }

    public String getParkName(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getParkName.php?");
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

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
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