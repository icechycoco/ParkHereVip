package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends BaseAdapter {
    Context mContext;
    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> strHis) {
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
            view = mInflater.inflate(R.layout.listview_row, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setText(strHis.get(position).get("pName").toString());
        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText("Time in : " + strHis.get(position).get("timeIn").toString());
        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText("Time out : " + strHis.get(position).get("timeO").toString());
        TextView textView4 = (TextView)view.findViewById(R.id.textView4);
        textView4.setText("Date : " + strHis.get(position).get("date").toString());

        return view;
    }
}
