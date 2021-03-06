package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveInfoFragment extends Fragment {

    Button btn;
    EditText etGuestN,etGuestS,etLicen, etEmail,etPhone,etDate;
    RadioButton radioButton1,radioButton2,radioButton3;

    String setName,setSur,setLicen,setEmail,setPhone;
    //String setuId,setpId,setgId;
    String setDate,setQR;
    //String setInterval,setTimeRes,setStatus;
    int setuId,setpId,setgId,setInterval,setTimeRes,setStatus;

    //setpId ต้องรับค่าจากปุ่มที่กดเลือกแอเรีย
    // setuId ต้องรับค่ามาจากหน้าลอกอิน
    // setQR รับค่ามาจากตัว generate

    String response = null;
    getHttp http = new getHttp();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReserveInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveInfoFragment newInstance(String param1, String param2) {
        ReserveInfoFragment fragment = new ReserveInfoFragment();
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
//        return inflater.inflate(R.layout.fragment_reserve_info, container, false);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_reserve_info, container, false);
        btn = (Button) v.findViewById(R.id.btn_reserve_ok);

        etGuestN = (EditText) v.findViewById(R.id.etGuestN);
        etGuestS = (EditText) v.findViewById(R.id.etGuestS);
        etLicen = (EditText) v.findViewById(R.id.etLicen);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPhone = (EditText) v.findViewById(R.id.etPhone);
        etDate = (EditText) v.findViewById(R.id.etDate);

        radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) v.findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) v.findViewById(R.id.radioButton3);

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //Fragment
                setName = etGuestN.getText().toString();
                setSur = etGuestS.getText().toString();
                setLicen = etLicen.getText().toString();
                setEmail = etEmail.getText().toString();
                //setDate = etDate.getText().toString();
                setPhone = etPhone.getText().toString();

                setDate = "20170710";
                setTimeRes = 180000;
                setuId = 10000;
                setpId = 1;
                setgId = 63;
                setQR = "0000";
                setStatus = 0; // 0 = จองอยู่ 1=จอด อาจจะไม่ต้องมีก็ได้


                //ทำไมมันไม่ insert ข้อมูลในตาราง reserve , guest ก็ไม่ขึ้นละสาส
                //แก้ php เรื่องหักลบ amount บางทีมันเป็น -1
                //ให้มันคืนค่าamountทุกๆเที่ยงคืน


                try {

                    response = http.run("http://parkhere.sit.kmutt.ac.th/newguest.php?gFirstN="+setName+"&gLastN="+setSur+"&gEmail="+setEmail+"&gLicense="+setLicen+"&gPhone="+setPhone);
                    //response = http.run("http://parkhere.sit.kmutt.ac.th/newguest.php?gFirstN="+setName+"&gLastN="+setSur+"&gEmail="+setEmail+"&gLicense="+setLicen+"&gPhone="+setPhone);
                    response = http.run("http://parkhere.sit.kmutt.ac.th/reserve.php?uId="+setuId+"&pId="+setpId+"&gId="+setgId+"&date="+setDate+"&timeInterval="+setInterval+"&timeRes="+setTimeRes+"&code="+setQR+"&status="+setStatus);
                    //response = http.run("http://parkhere.sit.kmutt.ac.th/reserve.php?uId="+setuId+"&pId="+setpId+"&gId="+setgId+"&date="+setDate+"&timeInterval="+setInterval+"&timeRes="+setTimeRes+"&code="+setQR+"&status="+setStatus);

                } catch (IOException e) {

                    // TODO Auto-generat-ed catch block

                    e.printStackTrace();
                }

//                HistoryFragment historyFragment = new HistoryFragment();
                Toast.makeText(getActivity().getApplicationContext(), "COMPLETED RESERVATION",
                        Toast.LENGTH_SHORT).show();
                ReserveFragment reserveFragmentt = new ReserveFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, reserveFragmentt);
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
