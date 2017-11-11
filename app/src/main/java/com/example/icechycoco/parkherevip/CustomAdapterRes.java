package com.example.icechycoco.parkherevip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by icechycoco on 8/28/2017 AD.
 */

public class CustomAdapterRes extends BaseAdapter implements ReserveFragment.OnFragmentInteractionListener{

    int[] resId;
    Context mContext;
    String pId;
    Button button;
    String uId;

    ReserveFragment reserveFragment = new ReserveFragment();

    ArrayList strParkArea;

    public CustomAdapterRes(Context context, ArrayList strParkArea, int[] resId) {
        this.mContext= context;
        this.strParkArea = strParkArea;
        this.resId = resId;
    }

    public int getCount() {

        return strParkArea.size();
    }

    public void setpId(String pId){
        this.pId = pId;
    }

    public String getpId(){
        return pId;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.listview_rowres, parent, false);

        TextView textView = (TextView)view.findViewById(R.id.textView1);
        textView.setText(strParkArea.get(position).toString());
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView1);
        imageView.setBackgroundResource(resId[position]);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);

        linearLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.wtf("position",position+1+"");
                pId = position+1 + "";
                setpId(pId);

                Log.wtf("eiei",""+getpId());

                ReserveinfoFragment reserveinfoFragment = new ReserveinfoFragment().newInstance(reserveFragment.getuId(),getpId());
                Log.wtf("get uid dai mai na : ", reserveFragment.getuId());
                Log.wtf("get pid : ",getpId());
                FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, reserveinfoFragment);
                transaction.commit();

            }

        });

        return view ;
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
