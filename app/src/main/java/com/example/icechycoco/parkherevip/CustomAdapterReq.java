package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapterReq extends BaseAdapter implements RequestFragment.OnFragmentInteractionListener {
    Context mContext;
    String code;

    RequestFragment requestFragment = new RequestFragment();
    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;

    public CustomAdapterReq(Context context, ArrayList<HashMap<String, String>> strHis) {
        this.mContext= context;
        this.strHis = strHis;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public int getCount() {
        return strHis.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.listview_rowreq, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setText(strHis.get(position).get("pName").toString());
        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText(strHis.get(position).get("licen").toString());
        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText(strHis.get(position).get("date").toString());
        TextView textView4 = (TextView)view.findViewById(R.id.textView4);
        String interval = strHis.get(position).get("timeInt").toString();
        String timeInt = null;
        if (interval.equals("0")){
            timeInt = "06:00 - 12:00";
        }else if (interval.equals("10")){
            timeInt = "13:00 - 18:00";
        }else if (interval.equals("11")){
            timeInt = "06:00 - 18:00";
        }
        textView4.setText("Interval : " + timeInt);

        textView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.wtf("position",strHis.get(position).get("code").toString());
                code = strHis.get(position).get("code").toString();
                setCode(code);

                Log.wtf("get licen : ",getCode());
                Log.wtf("get uId : ",requestFragment.getuId());

                QRScanFragment qrScanFragment = new QRScanFragment().newInstance(requestFragment.getuId(),getCode());

                FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, qrScanFragment);
                transaction.commit();

            }

        });

        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
