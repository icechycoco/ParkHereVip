package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapParkFragment extends Fragment {

    Button btn;
    TextView tv1,tv2,tv3;

    String response = null;
    getHttp http = new getHttp();

    String[] getInfo;
    String time;
    String timeIn,currentTime;
    int cost,mCost;

    Date date1,date2;
    long realFee;
    long diffHours = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapParkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapParkFragment newInstance(String param1, String param2) {
        MapParkFragment fragment = new MapParkFragment();
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
//        return inflater.inflate(R.layout.fragment_map_park, container, false);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_map_park, container, false);
        btn = (Button) v.findViewById(R.id.btn_detail);

//        tv1 = (TextView) v.findViewById(R.id.tv1);
//        tv2 = (TextView) v.findViewById(R.id.tv2);
//        tv3 = (TextView) v.findViewById(R.id.tv3);

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/estimate.php?uId=10001");

        } catch (IOException e) {

            // TODO Auto-generat-ed catch block

            e.printStackTrace();
        }

        getInfo = response.split(" ");
        time = getInfo[0];
        cost = Integer.parseInt(getInfo[1]);
        mCost = Integer.parseInt(getInfo[2]);

        // convert string time in database to time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeIn = sdf.format(date);

        //current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        currentTime = sdf2.format(cal.getTime());

        //calculate diff time
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            date1 = format.parse(timeIn);
            date2 = format.parse(currentTime);
            long diff = date2.getTime() - date1.getTime();
            System.out.println(diff);
            diffHours = diff / (60 * 60 * 1000);
            System.out.println("current time " + date2);
            System.out.print(diffHours + " hours, ");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //calculate fee
        long fee = diffHours*cost;
        if(fee>mCost){
            realFee = mCost;
            System.out.println(mCost);
        }else{
            realFee = fee;
            System.out.println(fee);
        }

//        tv1.setText(timeIn);
//        tv2.setText(Long.toString(realFee));
//        tv3.setText(currentTime);

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("\nTime in : "+timeIn+"\n\nCurrent time : "+currentTime+"\n\nReal-Time fee : "+realFee+" BAHT");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        builder.setView(view);
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
