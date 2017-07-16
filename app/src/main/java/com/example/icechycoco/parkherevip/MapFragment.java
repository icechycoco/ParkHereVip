package com.example.icechycoco.parkherevip;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    Button btn;
    TextView tv1,tv2;

    String response = null;
    getHttp http = new getHttp();

    String[] getInfo;
    String p1,p2,p3,p4,p5; //park in each park area
    String r1,r2,r3,r4; //reserve in each park area


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapStaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        return inflater.inflate(R.layout.fragment_map, container, false);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        btn = (Button) v.findViewById(R.id.btn_park);

        tv1 = (TextView) v.findViewById(R.id.tv1);
        tv2 = (TextView) v.findViewById(R.id.tv2);

        try {
            //ดึงค่าได้แล้ว แต่จะเกทมาโชว์แต่ละเอเรียยังไง
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAvailable.php");
        } catch (IOException e) {
            // TODO Auto-generat-ed catch block
            e.printStackTrace();
        }

        // get remain in each park into each variables
        // มันจะ real time ไหม แบบตัวเลขเปลี่ยนในแอพทันทีเลยถ้ามีเพิ่มลด
        getInfo = response.split("[A-Z]");
        p1 = getInfo[1];
        r1 = getInfo[2];
        p2 = getInfo[3];
        r2 = getInfo[4];
        System.out.println(p1);
        System.out.println(r1);
        System.out.println(p2);
        System.out.println(r2);
        //p3 = getInfo[2];
        tv1.setText("P "+p1+"\nR "+r1);
        tv2.setText("P "+p2+"\nR "+r2);

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //Fragment
                MapParkFragment mapParkFragment = new MapParkFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, mapParkFragment);
                transaction.commit();
            }
        });

        return v;

    }

//    @Override
//    public void onClick(View view) {
//        //Fragment
//        HistoryFragment historyFragment = new HistoryFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, historyFragment);
//        transaction.commit();
//    }

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
