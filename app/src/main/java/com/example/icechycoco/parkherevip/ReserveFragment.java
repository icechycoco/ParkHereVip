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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReserveFragment extends Fragment {

    Button btn;

    EditText etGuestN,etGuestS,etLicen, etEmail,etPhone,etDate;
    RadioButton radioButton1,radioButton2,radioButton3;

    String setName,setSur,setLicen,setEmail,setPhone;
    //String setuId,setpId,setgId;
    String setDate,setQR,setTimeRes;
    //setTimeRes;
    //String setInterval,setTimeRes,setStatus;
    int setuId,setpId,setgId,setInterval,setStatus;

    //setpId ต้องรับค่าจากปุ่มที่กดเลือกแอเรีย
    // setuId ต้องรับค่ามาจากหน้าลอกอิน
    // setQR รับค่ามาจากตัว generate

    String response = null;
    getHttp http = new getHttp();

    private static final String KEY_ID = "uId";
    private String uId;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_reserve_info, container, false);
        btn = (Button) v.findViewById(R.id.btn_reserve_cb1);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Fragment

                ReserveinfoFragment reserveFragment = new ReserveinfoFragment().newInstance(uId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, reserveFragment);
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

    // รับค่ายังไง?
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                setInterval = 101;
                break;
            case R.id.radioButton2:
                setInterval = 10;
                break;
            case R.id.radioButton3:
                setInterval = 11;
                break;
        }
    }


}