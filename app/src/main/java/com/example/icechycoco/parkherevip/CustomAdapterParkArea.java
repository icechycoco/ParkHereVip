package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by icechycoco on 10/22/2017 AD.
 */

public class CustomAdapterParkArea extends BaseAdapter implements AvailableFragment.OnFragmentInteractionListener {
    Context mContext;
    String code;

    int available;

    AvailableFragment requestFragment = new AvailableFragment();
    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;

    public CustomAdapterParkArea(Context context, ArrayList<HashMap<String, String>> strHis) {
        this.mContext= context;
        this.strHis = strHis;
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
            view = mInflater.inflate(R.layout.listview_parkarea, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setText(strHis.get(position).get("pName").toString());
        available = Integer.parseInt(strHis.get(position).get("available").toString());
        TextView textView6 = (TextView)view.findViewById(R.id.textView6);
        textView6.setText(available+"");
        if(available > 20){
            textView6.setTextColor(Color.parseColor("#96DA14"));
        }else if(available < 20){
            textView6.setTextColor(Color.parseColor("#FFAA0E"));
        }else if(available < 10){
            textView6.setTextColor(Color.parseColor("#DB0000"));
        }

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Log.wtf("position",strHis.get(position).get("code").toString());
//                code = strHis.get(position).get("code").toString();
//
//                Log.wtf("get licen : ",getCode());
//                Log.wtf("get uId : ",requestFragment.getuId());

//                Intent intent = new Intent(mContext, ScanActivity.class);
//                intent.putExtra("uId", requestFragment.getuId());
//                intent.putExtra("code", getCode());
//                mContext.startActivity(intent);
            }
        });

        return view;
    }

}
