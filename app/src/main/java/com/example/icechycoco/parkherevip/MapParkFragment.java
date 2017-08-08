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
import android.widget.Toast;

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

    // gui
    Button btn;
    // connect db
    String response = null;
    getHttp http = new getHttp();
    //variable
    String[] getInfo;
    String getTime;
    int getCost,getMCost;
    String timeIn,currentTime;
    Date date1,date2;
    long realFee;
    long diffHours = 0;

    private static final String KEY_ID = "uId";

    private String uId;

    private OnFragmentInteractionListener mListener;

    public MapParkFragment() {
        // Required empty public constructor
    }

    public static MapParkFragment newInstance(String uId) {
        MapParkFragment fragment = new MapParkFragment();
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

        View v = inflater.inflate(R.layout.fragment_map_park, container, false);
        btn = (Button) v.findViewById(R.id.btn_detail);

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/estimate.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getInfo = response.split(" ");
        getTime = getInfo[0];
        getCost = Integer.parseInt(getInfo[1]);
        getMCost = Integer.parseInt(getInfo[2]);

        // convert string time in database to time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(getTime);
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
        long fee = diffHours*getCost;
        if(fee>getMCost){
            realFee = getMCost;
            System.out.println(getMCost);
        }else{
            realFee = fee;
            System.out.println(fee);
        }

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
