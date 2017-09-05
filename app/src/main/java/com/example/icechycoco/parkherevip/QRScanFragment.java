package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QRScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QRScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRScanFragment extends Fragment {

    // gui
    Button btn,btnScan;
    public static EditText etCode;
    // connect db
    String response = null;
    getHttp http = new getHttp();
    // variables
    String[] getInfo;
    String fN,lN,licen,date,parkN,gName,pId,resId,getCode,checkCode,setTimeScan;
    int timeInter;
    String timeSeq = "";

    private static final String KEY_ID = "uId";
    private String uId;

    private OnFragmentInteractionListener mListener;

    public QRScanFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QRScanFragment newInstance(String uId) {
        QRScanFragment fragment = new QRScanFragment();
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


        View v = inflater.inflate(R.layout.fragment_qrscan, container, false);
        btn = (Button) v.findViewById(R.id.btncheck);
        etCode = (EditText) v.findViewById(R.id.et_code);
        checkCode = "22074";

        btnScan = (Button) v.findViewById(R.id.btnscan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getCode = etCode.getText().toString();

                if(getCode.equals(checkCode)) {

                    try {
                        response = http.run("http://parkhere.sit.kmutt.ac.th/resCon.php?code=" + getCode);

                    } catch (IOException e) {

                        // TODO Auto-generat-ed catch block

                        e.printStackTrace();
                    }
                    getInfo = response.split(" ");
                    fN = getInfo[0];
                    lN = getInfo[1];
                    gName = fN + "  " + lN;
                    licen = getInfo[2];
                    date = getInfo[3];
                    timeInter = Integer.parseInt(getInfo[4]);
                    parkN = getInfo[5];
                    resId = getInfo[6];
                    pId = getInfo[7];

                    if (timeInter == 0) {
                        timeSeq = "06:00 - 12:00";
                    } else if (timeInter == 10) {
                        timeSeq = "13:00 - 18:00";
                    } else if (timeInter == 11) {
                        timeSeq = "06:00 - 18:00";
                    } else {
                        timeSeq = "wrong time";
                    }

                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setMessage("\nคุณ " + gName + "\n\n" + parkN + "\n\n" + licen + "\n\n" + date + "\n\n" + timeSeq);

                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            Toast.makeText(getActivity().getApplicationContext(), "COMPLETED RESERVATION",
                                    Toast.LENGTH_SHORT).show();
                            RequestFragment requestFragment = new RequestFragment().newInstance(uId);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, requestFragment);
                            transaction.commit();
                        }
                    });

                    builder.show();

                    //current time
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                    setTimeScan = sdf2.format(cal.getTime());

                    try {
                        response = http.run("http://parkhere.sit.kmutt.ac.th/confirmRes.php?reserveId=" + resId + "&pId=" + pId + "&timeScan=" + setTimeScan+ "&secId=" + uId);
                    } catch (IOException e) {
                        // TODO Auto-generat-ed catch block
                        e.printStackTrace();
                    }

                }else{
                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setMessage("INVALID CODE");

                    builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            RequestFragment requestFragment = new RequestFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, requestFragment);
                            transaction.commit();
                        }
                    });
                    builder.show();
                }


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

}
