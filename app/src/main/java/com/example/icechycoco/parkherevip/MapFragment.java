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
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveinfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveinfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    // gui
    Button btn;
    TextView tv1,tv2;
    // connect db
    String response = null;
    getHttp http = new getHttp();
    // variables
    String[] getInfo;
    String p1,p2,p3,p4,p5; //park in each park area
    String r1,r2,r3,r4; //reserve in each park area

    private static final String KEY_ID = "uId";
    private String uId;

    private OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;

    public MapFragment() {
        // Required empty public constructor
    }

    // constuctor to get variable
    public static MapFragment newInstance(String uId) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
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
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();

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
        tv1.setText("P "+p1+"\nR "+r1);
        tv2.setText("P "+p2+"\nR "+r2);

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //Fragment
                MapParkFragment mapParkFragment = new MapParkFragment().newInstance(uId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, mapParkFragment);
                transaction.commit();
            }
        });

        return v;

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
