package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static java.security.AccessController.getContext;

public class CustomAdapterReq extends BaseAdapter implements RequestFragment.OnFragmentInteractionListener {
    Context mContext;
    String code;

    RequestFragment requestFragment = new RequestFragment();
    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;
    String[] getInfo1;
    String str1;
    String[] month = {"null","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String d,m,mm,y,newDate;

    public CustomAdapterReq(Context context, ArrayList<HashMap<String, String>> strHis) {
        this.mContext= context;
        this.strHis = strHis;
    }

    public String changeDateFormat(String str){
        getInfo1 = str.split("-");
        y = getInfo1[0];
        m = getInfo1[1];
        d = getInfo1[2];
        for(int i=0;i<month.length;i++){
            if(i==Integer.parseInt(m)){
                mm = month[i];
                break;
            }
        }
        newDate = d + " " + mm + " " + y;
        return newDate;
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

        TextView textView5 = (TextView)view.findViewById(R.id.textView5);
        textView5.setText(strHis.get(position).get("pName").toString());
        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText(strHis.get(position).get("licen").toString());
        str1 = strHis.get(position).get("date").toString();
        String interval = strHis.get(position).get("timeInt").toString();
        String timeInt = null;
        if (interval.equals("0")){
            timeInt = "06:00-12:00";
        }else if (interval.equals("10")){
            timeInt = "13:00-18:00";
        }else if (interval.equals("11")){
            timeInt = "06:00-18:00";
        }

        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText(changeDateFormat(str1) + " " + timeInt);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.wtf("position",strHis.get(position).get("code").toString());
                code = strHis.get(position).get("code").toString();
                setCode(code);

                Log.wtf("get licen : ",getCode());
                Log.wtf("get uId : ",requestFragment.getuId());

                Intent intent = new Intent(mContext, ScanActivity.class);
                intent.putExtra("uId", requestFragment.getuId());
                intent.putExtra("code", getCode());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
