package com.example.icechycoco.parkherevip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeSecActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HistoryFragment.OnFragmentInteractionListener, RequestFragment.OnFragmentInteractionListener,
        MapParkFragment.OnFragmentInteractionListener, QRScanFragment.OnFragmentInteractionListener,
        BlankFragment.OnFragmentInteractionListener{
    // shared variables
    String uId;
    SharedPreferences sp;

    int level,sta;

    String[] name = new String[]{"View","Ice","Park"};
    String[] getInfo = null;
    String[] getInfo2 = null;
    String pName,fN,lN,phone,uName;
    String pName2,fN2,lN2,phone2,gName;

    // connect db
    String response = null;
    getHttp http = new getHttp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sec);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // send variable
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        uId = sp.getString("UID", "0");
        Toast.makeText(this, "uId : " + uId, Toast.LENGTH_SHORT).show();

        String str = getLev(uId);
        String[] getInfo3;
        getInfo3 = str.split(",");
        level = Integer.parseInt(getInfo3[0]);
        sta = Integer.parseInt(getInfo3[1]);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView = navigationView.getHeaderView(0);
            TextView nav_user = (TextView) hView.findViewById(R.id.tvname);
            nav_user.setText(getName(uId));
            ImageView img = (ImageView) hView.findViewById(R.id.imageView);
            img.setBackgroundResource(R.drawable.sec);
            navigationView.setNavigationItemSelectedListener(this);


            final SearchView searchView = (SearchView) findViewById(R.id.search_view);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    String u = userInfo(query);
                    if (u.equals("0 ")) {
                        String g = guestInfo(query);
                        if (g.equals("0 ")) {
                            final AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getBaseContext());
                            builder.setMessage("----Invalid License---");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    builder.getContext();
                                }
                            });
                            builder.show();
                        } else {
                            getInfo = g.split(",");
                            pName2 = getInfo[0];
                            fN2 = getInfo[1];
                            lN2 = getInfo[2];
                            gName = fN2 + "   " + lN2;
                            phone2 = getInfo[3];

                            final AlertDialog.Builder builder =
                                    new AlertDialog.Builder(HomeSecActivity.this);
                            builder.setMessage("Guest Name: " + gName + "\n\nParked Area: " + pName2 + "\n\nPhone Number : " + phone2);
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    builder.getContext();
                                }
                            });
                            builder.show();
                        }
                    } else {
                        Log.wtf("show userinfo : ", u);
                        getInfo2 = u.split(",");
                        pName = getInfo2[0];
                        fN = getInfo2[1];
                        lN = getInfo2[2];
                        uName = fN + "   " + lN;
                        phone = getInfo2[3];

                        final AlertDialog.Builder builder =
                                new AlertDialog.Builder(HomeSecActivity.this);
                        builder.setMessage("Name: " + uName + "\n\nParked Area: " + pName + "\n\nPhone Number : " + phone);
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                builder.getContext();
                            }
                        });
                        builder.show();

                    }
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        if(sta==1){
            String pId = getpId(uId);
            String parkLoc = getParkLocation(uId);
            BlankFragment blankFragment = new BlankFragment().newInstance(uId,"3",parkLoc);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, blankFragment);
            transaction.commit();

        }else {
            AvailableFragment availableFragment = new AvailableFragment().newInstance(uId, "3");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, availableFragment);
            transaction.commit();

        }
    }

    public String userInfo(String query){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/search.php?uLicense=" + query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String guestInfo(String query){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/searchGuest.php?gLicense=" + query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_sec, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Fragment
//            BlankFragment blankFragment = new BlankFragment().newInstance(uId,"3");
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, blankFragment);
//            transaction.commit();
            AvailableFragment availableFragment = new AvailableFragment().newInstance(uId,"3");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, availableFragment);
            transaction.commit();
        } else if (id == R.id.nav_history) {
            //Fragment
            HistoryFragment historyFragment = new HistoryFragment().newInstance(uId,"3");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, historyFragment);
            transaction.commit();
        } else if (id == R.id.nav_request){
            //Fragment
            RequestFragment requestFragment = new RequestFragment().newInstance(uId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, requestFragment);
            transaction.commit();
        } else if (id == R.id.nav_logout) {
            SharedPreferences sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Username", "");
            editor.putString("Password", "");
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public String getName(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getUname.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getLev(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/Level.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getParkLocation(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getParkLatLong.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getpId(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getpId.php?uId="+uId);
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
