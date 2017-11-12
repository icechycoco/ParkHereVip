package com.example.icechycoco.parkherevip;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeStaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReserveinfoFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener, ReserveFragment.OnFragmentInteractionListener,
         MapParkFragment.OnFragmentInteractionListener,
        BlankFragment.OnFragmentInteractionListener{

    String uId;
    SharedPreferences sp;

    // connect db
    String response = null;
    getHttp http = new getHttp();

    int level,sta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        uId = sp.getString("UID", "0");

        String str = getLev(uId);
        String[] getInfo;
        getInfo = str.split(",");
        level = Integer.parseInt(getInfo[0]);
        sta = Integer.parseInt(getInfo[1]);


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
            img.setBackgroundResource(R.drawable.sta);
            navigationView.setNavigationItemSelectedListener(this);
        if(sta==1){
            String pId = getpId(uId);
            String parkLoc = getParkLocation(uId);
            BlankFragment blankFragment = new BlankFragment().newInstance(uId,"2",parkLoc);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, blankFragment);
            transaction.commit();

        }else {

            AvailableFragment availableFragment = new AvailableFragment().newInstance(uId, "2");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, availableFragment);
            transaction.commit();
        }
    }
    public void showNotification(View view) {

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Confirm Parking")
                        .setContentText("Guest already park at the reserved parking area")
                        .setAutoCancel(true)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);
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
        getMenuInflater().inflate(R.menu.home_sta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Fragment
//            MapFragment mapFragment = new MapFragment().newInstance(uId);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, mapFragment);
//            transaction.commit();

//            BlankFragment blankFragment = new BlankFragment().newInstance(uId,"2");
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, blankFragment);
//            transaction.commit();
            if(sta==1){
                String pId = getpId(uId);
                String parkLoc = getParkLocation(uId);
                BlankFragment blankFragment = new BlankFragment().newInstance(uId,"2",parkLoc);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, blankFragment);
                transaction.commit();

                setActionBarTitle("ParkHere");

            }else {
                AvailableFragment availableFragment = new AvailableFragment().newInstance(uId, "2");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, availableFragment);
                transaction.commit();

                setActionBarTitle("ParkHere");
            }

        } else if (id == R.id.nav_history) {
            //Fragment
            HistoryFragment historyFragment = new HistoryFragment().newInstance(uId,"2");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, historyFragment);
            transaction.commit();
            setActionBarTitle("History");

        } else if (id == R.id.nav_reserve) {
            //Fragment
            ReserveFragment reserveFragmentt = new ReserveFragment().newInstance(uId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, reserveFragmentt);
            transaction.commit();
            setActionBarTitle("Reserve");

        } else if (id == R.id.nav_logout) {
            SharedPreferences sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Username", "");
            editor.putString("Password", "");
            editor.commit();

            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            setActionBarTitle("ParkHere");
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

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}
