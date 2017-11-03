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

public class CustomAdapterHisRes extends BaseAdapter {
    Context mContext;
    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;
    String[] getInfo1,getInfo2,getInfo3;
    String str1,str2,str3;
    String[] month = {"null","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String d,m,mm,y,newDate;
    String h,min,s,newTime;

    public CustomAdapterHisRes(Context context, ArrayList<HashMap<String, String>> strHis) {
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
            view = mInflater.inflate(R.layout.listview_rowhisres, parent, false);

        String interval = strHis.get(position).get("timeInter").toString();
        String timeInt = null;
        if(interval.equals("0")){
            timeInt = "06:00 - 12:00";
        }else if(interval.equals("10")){
            timeInt = "13:00 - 18:00";
        }else if(interval.equals("11")){
            timeInt = "06:00 - 18:00";
        }

        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setText(strHis.get(position).get("parkName").toString());
        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText("Khun " + strHis.get(position).get("gFi").toString()+ "  " + strHis.get(position).get("gLa").toString());
        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText(strHis.get(position).get("gLi").toString());

        str1 = strHis.get(position).get("date").toString();
        TextView textView8 = (TextView)view.findViewById(R.id.textView8);
        textView8.setText(changeDateFormat(str1));

        TextView textView4 = (TextView)view.findViewById(R.id.textView4);
        textView4.setText(timeInt);

        str2 = strHis.get(position).get("dateRes").toString();
        str3 = strHis.get(position).get("timeRes").toString();
        TextView textView6 = (TextView)view.findViewById(R.id.textView6);
        textView6.setText( changeDateFormat(str2) + " " + changeTimeFormat(str3));



        return view;
        //+ strHis.get(position).get("gFi").toString() + strHis.get(position).get("gLa").toString()
        //+ strHis.get(position).get("gLi").toString()
    }
}
