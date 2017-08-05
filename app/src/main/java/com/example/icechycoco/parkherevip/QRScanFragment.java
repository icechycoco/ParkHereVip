package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.DialogInterface;
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

    Button btn;
    EditText etCode;
    String response = null;
    getHttp http = new getHttp();

    String[] getInfo;
    String fN,lN,licen,date,parkN,gName,pId,resId,getCode;
    int timeInter;
    //CharSequence[] timeSeq = {"06:00 - 12:00", "13:00 - 18:00", "06:00-18:00"};
    String timeSeq = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public QRScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRScanFragment newInstance(String param1, String param2) {
        QRScanFragment fragment = new QRScanFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_qrscan, container, false);
        btn = (Button) v.findViewById(R.id.btn_scan);
        etCode = (EditText) v.findViewById(R.id.et_code);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getCode = etCode.getText().toString();

                try {
                    response = http.run("http://parkhere.sit.kmutt.ac.th/resCon.php?code="+getCode);

                } catch (IOException e) {

                    // TODO Auto-generat-ed catch block

                    e.printStackTrace();
                }
                getInfo = response.split(" ");
                fN = getInfo[0];
                lN = getInfo[1];
                gName = fN +"  " +lN;
                licen = getInfo[2];
                date = getInfo[3];
                timeInter = Integer.parseInt(getInfo[4]);
                parkN = getInfo[5];
                resId = getInfo[6];
                pId = getInfo[7];

                if(timeInter==01){
                    timeSeq = "06:00 - 12:00";
                }else if(timeInter==10){
                    timeSeq = "13:00 - 18:00";
                }else if(timeInter==11){
                    timeSeq = "06:00 - 18:00";
                }else{
                    timeSeq = "wrong time";
                }

                try {

                    response = http.run("http://parkhere.sit.kmutt.ac.th/confirmRes.php?reserveId="+resId+"&pId="+pId);

                } catch (IOException e) {

                    // TODO Auto-generat-ed catch block

                    e.printStackTrace();
                }

                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("\nคุณ "+gName+"\n\n"+parkN+"\n\n"+licen+"\n\n"+date+"\n\n"+timeSeq);

                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int id){

                        Toast.makeText(getActivity().getApplicationContext(), "COMPLETED RESERVATION",
                                Toast.LENGTH_SHORT).show();
                        RequestFragment requestFragment = new RequestFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, requestFragment);
                        transaction.commit();
                    }
                });
                builder.show();

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
