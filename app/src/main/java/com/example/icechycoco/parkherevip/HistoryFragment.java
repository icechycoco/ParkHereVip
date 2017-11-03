package com.example.icechycoco.parkherevip;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private static final String KEY_ID = "uId";
    private String uId;
    private static final String KEY_PO = "po";
    private String po;

    private OnFragmentInteractionListener mListener;
    LocalActivityManager mLocalActivityManager;

    public HistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String uId,String po) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PO, po);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get variable
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
            po = bundle.getString(KEY_PO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mLocalActivityManager = new LocalActivityManager(getActivity(), false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        final View v = inflater.inflate(R.layout.fragment_history, container, false);

        TabHost tabHost = (TabHost) v.findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);

        Intent intent = new Intent(getContext(),Tab1.class);
        intent.putExtra("uId",uId);
        intent.putExtra("po",po);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("PARKED")
                .setIndicator("PARKED")
                .setContent(intent);

        Intent intent2 = new Intent(getContext(),Tab2.class);
        intent2.putExtra("uId",uId);
        intent2.putExtra("po",po);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("RESERVED")
                .setIndicator("RESERVED")
                .setContent(intent2);

        Intent intent3 = new Intent(getContext(),Tab3.class);
        intent3.putExtra("uId",uId);
        intent3.putExtra("po",po);
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("SCANNED")
                .setIndicator("SCANNED")
                .setContent(intent3);

        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
