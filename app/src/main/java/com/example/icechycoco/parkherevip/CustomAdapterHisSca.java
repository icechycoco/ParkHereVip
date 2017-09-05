package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by icechycoco on 9/4/2017 AD.
 */

public class CustomAdapterHisSca extends BaseAdapter {
    Context mContext;
    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;

    public CustomAdapterHisSca(Context context, ArrayList<HashMap<String, String>> strHis) {
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

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.listview_rowhissca, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setText(strHis.get(position).get("parkName").toString());
        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText("Guest Name " + strHis.get(position).get("gFi").toString()+ "  " + strHis.get(position).get("gLa").toString() );
        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText("License " + strHis.get(position).get("gLi").toString());
        TextView textView4 = (TextView)view.findViewById(R.id.textView4);
        textView4.setText("Reserve For  : " + strHis.get(position).get("date").toString());
        TextView textView6 = (TextView)view.findViewById(R.id.textView6);
        textView6.setText("Parked at : " + strHis.get(position).get("timeScan").toString());

        return view;
    }
}
