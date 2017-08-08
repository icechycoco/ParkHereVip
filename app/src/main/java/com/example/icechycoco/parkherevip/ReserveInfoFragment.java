package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

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
    // gui
    Button btn, btnC;
    EditText etGuestN, etGuestS, etLicen, etEmail, etPhone, etDate;
    RadioButton radioButton1, radioButton2, radioButton3;
    TextView textView;

    String setName, setSur, setLicen, setEmail, setPhone, setgId;
    String setDate, setQR, setTimeRes, setuId;
    String getCode, setpId, setInterval, setStatus;
    String[] getGInfo;
    String getGId, getEmail, getLicen, getPhone;

    //setpId ต้องรับค่าจากปุ่มที่กดเลือกแอเรีย
    // setuId ต้องรับค่ามาจากหน้าลอกอิน
    // setQR รับค่ามาจากตัว generate

    String response = null;
    getHttp http = new getHttp();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_ID = "uId";

    // TODO: Rename and change types of parameters
    private String uId;


    private ReserveInfoFragment.OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;

    public ReserveInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReserveInfoFragment newInstance(String uId) {
        ReserveInfoFragment fragment = new ReserveInfoFragment();
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
        btn = (Button) v.findViewById(R.id.btn_reserve);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

//        btnC = (Button) v.findViewById(R.id.btn_check);
//
//        etGuestN = (EditText) v.findViewById(R.id.etGuestN);
//        etGuestS = (EditText) v.findViewById(R.id.etGuestS);
//        etLicen = (EditText) v.findViewById(R.id.etLicen);
//        etEmail = (EditText) v.findViewById(R.id.etEmail);
//        etPhone = (EditText) v.findViewById(R.id.etPhone);
//        etDate = (EditText) v.findViewById(R.id.etDate);
//
//        radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
//        radioButton2 = (RadioButton) v.findViewById(R.id.radioButton2);
//        radioButton3 = (RadioButton) v.findViewById(R.id.radioButton3);
//
//        textView = (TextView) v.findViewById(R.id.tv_result);
//
//        btnC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setName = etGuestN.getText().toString();
//                setSur = etGuestS.getText().toString();
//                try {
//                    response = http.run("http://parkhere.sit.kmutt.ac.th/checkG.php?gFirstN=" + setName + "&gLastN=" + setSur);
//                } catch (IOException e) {
//                    // TODO Auto-generat-ed catch block
//                    e.printStackTrace();
//                }
//
//                if (response.equals("0")) {
//                    textView.setText("please fill in a guest infomation");
//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            setLicen = etLicen.getText().toString();
//                            setEmail = etEmail.getText().toString();
//                            //setDate = etDate.getText().toString();
//                            setPhone = etPhone.getText().toString();
//                            setDate = "20170710"; // เปลี่ยนมารับค่าวันที่
//                            //current time
//                            Calendar cal = Calendar.getInstance();
//                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
//                            setTimeRes = sdf2.format(cal.getTime());
//                            setuId = uId; //รับค่ามาจากหน้าลอคอิน
//                            setpId = "1"; //รับค่าจากการเลือกแอเรีย
//                            //setgId = "73"; // ตารางต้องเหมือนกันอะ reserve and guest ถึงจะสร้างได้ ยัง bug อยุ่
//                            setQR = randCode();
//                            setStatus = "0"; // 0 = จองอยู่ 1=จอด อาจจะไม่ต้องมีก็ได้
//
//                            try {
//                                response = http.run("http://parkhere.sit.kmutt.ac.th/newguest.php?gFirstN=" + setName + "&gLastN=" + setSur + "&gEmail=" + setEmail + "&gLicense=" + setLicen + "&gPhone=" + setPhone);
//                            } catch (IOException e) {
//                                // TODO Auto-generat-ed catch block
//                                e.printStackTrace();
//                            }
//                            reserve(setuId, setpId, setgId, setDate, setInterval, setTimeRes, setQR, setQR);
//                            sendEmail(setEmail);
//                        }
//                    });
//                } else {
//                    textView.setText("Existed Guest Infomation");
//
//                    getGInfo = response.split(" ");
//                    getGId = getGInfo[0];
//                    getEmail = getGInfo[1];
//                    getLicen = getGInfo[2];
//                    getPhone = getGInfo[3];
//
//                    etEmail.setText(getEmail);
//                    etLicen.setText(getLicen);
//                    etPhone.setText(getPhone);
//
//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            setDate = "20170710"; // เปลี่ยนมารับค่าวันที่
//                            //current time
//                            Calendar cal = Calendar.getInstance();
//                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
//                            setTimeRes = sdf2.format(cal.getTime());
//                            setuId = uId; //รับค่ามาจากหน้าลอคอิน
//                            setpId = "1"; //รับค่าจากการเลือกแอเรีย
//                            setQR = randCode();
//                            setStatus = "0"; // 0 = จองอยู่ 1=จอด อาจจะไม่ต้องมีก็ได้
//                            setgId = getGId;
//
//                            reserve(setuId, setpId, setgId, setDate, setInterval, setTimeRes, setQR, setQR);
//                            sendEmail(getEmail);
//
//                        }
//                    });
//                }
//            }
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
        if (context instanceof ReserveInfoFragment.OnFragmentInteractionListener) {
            mListener = (ReserveInfoFragment.OnFragmentInteractionListener) context;
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
                setInterval = "00";
                break;
            case R.id.radioButton2:
                setInterval = "10";
                break;
            case R.id.radioButton3:
                setInterval = "11";
                break;
        }
    }

    public void reserve(String uId,String pId, String gId, String date, String interval, String time, String code,String status){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/reserve.php?uId=" + uId + "&pId=" + pId + "&gId=" + gId + "&date=" + date + "&timeInterval=" + interval + "&timeRes=" + time + "&code=" + code + "&status=" + status);
        } catch (IOException e) {
            // TODO Auto-generat-ed catch block
            e.printStackTrace();
        }
    }

    public String randCode(){
        int min = 10000;
        int max = 99999;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/checkCode.php?code="+i1);
        } catch (IOException e) {
            // TODO Auto-generat-ed catch block
            e.printStackTrace();
        }

        getCode = response;
        if(getCode.equals("n")) {
            return "" + i1;
        }
        return randCode();

    }

    public void sendEmail(final String email){
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender(
                            "vipsmartpark@gmail.com",
                            "villicepark");
                    //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");
                    sender.sendMail("Confirm Park Reservation at KMUTT",
                            "To: " + setName + setSur + "\n\n" +
                                    "this is your code : " + setQR + "\n\n" +
                                    "This mail has been sent from ParkHere application.",
                            "vipsmartpark@gmail.com",
                            email);
                } catch (Exception e) {

                }

            }

        }).start();
    }



}
