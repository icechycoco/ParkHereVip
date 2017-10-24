package com.example.icechycoco.parkherevip;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by icechycoco on 10/22/2017 AD.
 */

public class CustomAdapterParkArea extends BaseAdapter implements AvailableFragment.OnFragmentInteractionListener, BlankFragment.OnFragmentInteractionListener {
    Context mContext;
    String code;

    int available;
    String pId;

    String response = null;
    getHttp http = new getHttp();

    AvailableFragment availableFragment = new AvailableFragment();

    //String[] strName;
    ArrayList<HashMap<String, String>> strHis;
    ArrayList<HashMap<String, String>> strDistance;


    public CustomAdapterParkArea(Context context, ArrayList<HashMap<String, String>> strHis,
                                 ArrayList<HashMap<String, String>> strDistance) {
        this.mContext= context;
        this.strHis = strHis;
        this.strDistance = strDistance;
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
        TextView textView20 = (TextView)view.findViewById(R.id.textView20);
        textView20.setText(strDistance.get(position).get("d").toString()+" m");
        ImageView img = (ImageView) view.findViewById(R.id.imageView3);
        if(available < 15) {
            textView6.setTextColor(Color.parseColor("#DB0000"));
            img.setImageResource(R.drawable.arrowred);
        }else if(available == 0){
            img.setVisibility(View.INVISIBLE);
        }

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (availableFragment.getPo().equals("3")) {

                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_manage);

                    final EditText number = (EditText) dialog.findViewById(R.id.editText);
                    final Button cncl = (Button) dialog.findViewById(R.id.button_cancel);
                    final Button ok = (Button) dialog.findViewById(R.id.button_login);

//                    String title = marker.getTitle();
//                    String[] msg;
//                    final String pid;
//                    int ava;
//                    msg = title.split(" ");
//                    ava = Integer.parseInt(msg[0]);
//                    pid = msg[1];

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                // สร้าง php ใหม่เปลี่ยนจำนวน available
                                response = http.run("http://parkhere.sit.kmutt.ac.th/setNumber.php?pId=" + pId + "&remain=" + number.getText());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });

                    cncl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    number.setText(available + "");

                    dialog.show();

                }
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pId = position+1+"";

                BlankFragment blankFragment = new BlankFragment().newInstance(availableFragment.getuId(), availableFragment.getPo(), pId, getLocation(pId)  );
                FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, blankFragment);
                transaction.commit();
                Log.wtf("get position : ", availableFragment.getPo());
                Log.wtf("get uId : ", availableFragment.getuId());
                Log.wtf("get pId : ", pId);

            }

        });




        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public String getLocation(String pId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getLocation.php?pId="+pId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
