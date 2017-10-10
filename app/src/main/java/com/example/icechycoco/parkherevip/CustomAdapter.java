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
    String[] getInfo1,getInfo2,getInfo3;
    String str1,str2,str3;
    String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String d,m,mm,y,newDate;
    String h,min,s,newTime;

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

    public String changeTimeFormat(String str){
        getInfo2 = str.split(":");
        h = getInfo2[0];
        min = getInfo2[1];
        s = getInfo2[2];
        newTime = h + ":" + min;
        return newTime;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.listview_row, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setText(strHis.get(position).get("pName").toString());
        str1 = strHis.get(position).get("timeIn").toString();
        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText("Time in  " + changeTimeFormat(str1));

        str2 = strHis.get(position).get("timeO").toString();
        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText("Time out : " + changeTimeFormat(str2));

        str3 = strHis.get(position).get("date").toString();
        TextView textView4 = (TextView)view.findViewById(R.id.textView4);
        textView4.setText(changeDateFormat(str3));

        return view;
    }
}
