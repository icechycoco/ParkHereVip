package com.example.icechycoco.parkherevip;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

//import android.location.LocationListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_ID = "uId";
    private String uId,best;



    LocationManager locationManager;
    String locationProvider;


    String text = "eieiza55plus";
    //para
    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker1,mCurrLocationMarker2,mCurrLocationMarker3;
    LocationRequest mLocationRequest;
    TextView txt,txt2,txt3,txt4;
    Button btn;
    public ActivityRecognizedService activityRecognition = new ActivityRecognizedService();
    boolean inside;
    static int vehicle;
    static boolean park = false;
    // connect db
    String response = null;
    getHttp http = new getHttp();

    String parkId,timeIn,timeOut;
    String pId,pName,available,reserved,latitude,longitude;
    long la,lo;
    int level,sta;
    String showsta = "NULL";

    int i;


    LocationListener locationListener;
    //variable
    String[] getInfo;
    String getTime;
    int getCost,getMCost;
    String currentTime;
    Date date1,date2;
    long realFee;
    long diffHours = 0;


    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String uId) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
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
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();

        if(sta==0){

            try {
                response = http.run("http://parkhere.sit.kmutt.ac.th/estimate.php?uId="+uId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response.equals("0")){

            }else{
                getInfo = response.split(" ");
                getTime = getInfo[0];
                getCost = Integer.parseInt(getInfo[1]);
                getMCost = Integer.parseInt(getInfo[2]);

                // convert string time in database to time
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(getTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeIn = sdf.format(date);

                //current time
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                currentTime = sdf2.format(cal.getTime());

                //calculate diff time
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                try {
                    date1 = format.parse(timeIn);
                    date2 = format.parse(currentTime);
                    long diff = date2.getTime() - date1.getTime();
                    System.out.println(diff);
                    diffHours = diff / (60 * 60 * 1000);
                    System.out.println("current time " + date2);
                    System.out.print(diffHours + " hours, ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //calculate fee
                long fee = diffHours*getCost;
                if(fee>getMCost){
                    realFee = getMCost;
                    System.out.println(getMCost);
                }else{
                    realFee = fee;
                    System.out.println(fee);
                }

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_fee);

                final TextView tin = (TextView) dialog.findViewById(R.id.textView13);
                final TextView tout = (TextView) dialog.findViewById(R.id.textView15);
                final TextView estfee = (TextView) dialog.findViewById(R.id.textView17);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView);
                tin.setText(timeIn);
                tout.setText(currentTime);
                estfee.setText(realFee +" BAHT");

//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

                dialog.show();
            }
        }
//



//        Criteria crit = new Criteria();
//        crit.setAccuracy(Criteria.ACCURACY_FINE);
//        best = locationManager.getBestProvider(crit, false);
//        locationManager.requestLocationUpdates(best,0,1, (LocationListener) this);

//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria, false);
//                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showActivity();
                                showStatus2();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();


//        this.initializeLocationManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_blank, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        txt = (TextView) v.findViewById(R.id.textView);
        txt2 = (TextView) v.findViewById(R.id.textView2);
        txt3 = (TextView) v.findViewById(R.id.textView3);
        txt4 = (TextView) v.findViewById(R.id.textView4);

        txt.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showResult(text);
                showActivity();
                showStatus2();
            }
        }));
//
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.wtf("","resume");

//        locationListener


//        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1, (android.location.LocationListener) this);
//        Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);


//        locationManager.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public boolean checkActivity(){
        if(vehicle==0 ){
            return true;
        }
        return false;
    }

    public void showStatus2(){
        String str = getLev(uId);
        String[] getInfo;
        getInfo = str.split(",");
        level = Integer.parseInt(getInfo[0]);
        sta = Integer.parseInt(getInfo[1]);


        if(sta==0){
            showsta = "not park";

//            Toast.makeText(getActivity(), "Not Park", Toast.LENGTH_LONG).cancel();
        }else if(sta==1){
            showsta = "park";
//            Toast.makeText(getActivity(), "Parked", Toast.LENGTH_LONG).show();
        }

        if(sta==0) {
            if (checkActivity() && inside) {
                level = 10;

                updateLev(level,uId);

                Log.wtf("level is : ",getLev(uId));
                Log.wtf("status is : ",sta+"");
                Log.wtf("Not Park","1");
                txt3.setText(showsta);
                String str2 = getLev(uId);
                String[] getInfo2;
                getInfo2 = str2.split(",");
                level = Integer.parseInt(getInfo2[0]);
                sta = Integer.parseInt(getInfo2[1]);
                txt4.setText("Level = " +level);

            }
            if (level == 10 && !checkActivity() && !inside) {
                level = 20;
                park = true;
                updateLev(level, uId);

                timeIn = getCurrentTime();

                Log.wtf("why i = ",i+"");
                i++;

                updateStatusPark(uId, "2", timeIn, "2017-08-18");

                Log.wtf("level is : ",getLev(uId));
                Log.wtf("status is : ",sta+"");
                Log.wtf("Not Park","2");
                txt3.setText(showsta);

                String str2 = getLev(uId);
                String[] getInfo2;
                getInfo2 = str2.split(",");
                level = Integer.parseInt(getInfo2[0]);
                sta = Integer.parseInt(getInfo2[1]);
                txt4.setText("Level = " +level);

            }

        }

        if(sta==1){

            if(!checkActivity() && inside) {

                level = 10;

                updateLev(level,uId);

                Log.wtf("level is : ",getLev(uId));
                Log.wtf("status is : ",sta+"");
                Log.wtf("Park","1");
                txt3.setText(showsta);
                String str2 = getLev(uId);
                String[] getInfo2;
                getInfo2 = str2.split(",");
                level = Integer.parseInt(getInfo2[0]);
                sta = Integer.parseInt(getInfo2[1]);
                txt4.setText("Level = " +level);

            }
            if(level==10 && checkActivity() && !inside){
                level=20;
                park=false;

                Log.wtf("why i = ","wow1");

                updateLev(level,uId);

                Log.wtf("why i = ","wow2");

                parkId = getParkId(uId);
                timeOut = getCurrentTime();

                Log.wtf("why i = ","wow3");

                Log.wtf("uId before call db ::",uId);
                Log.wtf("pId before call db::","2");
                Log.wtf("time before call db ::",timeOut);
                Log.wtf("get park id before call db :: ",parkId);

                updateStatusNotPark(uId,"2",timeOut,parkId);

                Log.wtf("why i = ","wow4");

                Log.wtf("level is : ",getLev(uId));
                Log.wtf("status is : ",sta+"");
                Log.wtf("Park","2");
                txt3.setText(showsta);
                String str2 = getLev(uId);
                String[] getInfo2;
                getInfo2 = str2.split(",");
                level = Integer.parseInt(getInfo2[0]);
                sta = Integer.parseInt(getInfo2[1]);
                txt4.setText("Level = " +level);
                Log.wtf("why i = ","wow5");

            }
        }

        Log.wtf("level is : ",getLev(uId)+"");
        Log.wtf("status is : ",sta+"");
        txt3.setText(showsta + sta);
        String str2 = getLev(uId);
        String[] getInfo2;
        getInfo2 = str2.split(",");
        level = Integer.parseInt(getInfo2[0]);
        sta = Integer.parseInt(getInfo2[1]);
        txt4.setText("Level = " +level);
    }

//    public void showStatus(){
//
//        String str = getLev(uId);
//
//        String[] getInfo;
//        getInfo = str.split(",");
//        level = Integer.parseInt(getInfo[0]);
//        sta = Integer.parseInt(getInfo[1]);
//
//        if(sta==0){
//            showsta = "not park";
//        }else if(sta==1){
//            showsta = "park";
//        }
//
//        if(checkActivity() && !inside && !park){
//            //String output = updateLev(1,"10003");
//            level = 1;
//
//            updateLev(level,uId);
//
//            Log.wtf("level is : ",getLev(uId));
//            Log.wtf("status is : ",sta+"");
//            txt3.setText(showsta);
//            String str2 = getLev(uId);
//            String[] getInfo2;
//            getInfo2 = str2.split(",");
//            level = Integer.parseInt(getInfo2[0]);
//            sta = Integer.parseInt(getInfo2[1]);
//            txt4.setText("Level = " +level);
//        }
//        if(level==1 && inside && !park){
//            level=2;
//
//            updateLev(level,uId);
//
//            Log.wtf("level is : ",getLev(uId));
//            Log.wtf("status is : ",sta+"");
//            txt3.setText(showsta);
//            String str2 = getLev(uId);
//            String[] getInfo2;
//            getInfo2 = str2.split(",");
//            level = Integer.parseInt(getInfo2[0]);
//            sta = Integer.parseInt(getInfo2[1]);
//            txt4.setText("Level = " +level);
//        }
//        if(level==2){
//            if(!checkActivity() && inside && !park){
//                level=3;
//
//                updateLev(level,uId);
//
//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
//                txt3.setText(showsta);
//                String str2 = getLev(uId);
//                String[] getInfo2;
//                getInfo2 = str2.split(",");
//                level = Integer.parseInt(getInfo2[0]);
//                sta = Integer.parseInt(getInfo2[1]);
//                txt4.setText("Level = " +level);
//            }else if(checkActivity() && !inside && park){
//                level=1;
//                park=false;
//
//                updateLev(level,uId);
//
//                parkId = getParkId(uId);
//                timeOut = getCurrentTime();
//                updateStatusNotPark(uId,"2",timeOut,parkId);
//
//
//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
//                txt3.setText(showsta);
//                String str2 = getLev(uId);
//                String[] getInfo2;
//                getInfo2 = str2.split(",");
//                level = Integer.parseInt(getInfo2[0]);
//                sta = Integer.parseInt(getInfo2[1]);
//                txt4.setText("Level = " +level);
//            }
//        }
//        if(level==3){
//            if(!checkActivity() && !inside && !park){
//                level=4;
//                park=true;
//
//                updateLev(level, uId);
//
//                timeIn = getCurrentTime();
//                updateStatusPark(uId, "2", timeIn, "2017-08-18");
//
//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
//                txt3.setText(showsta);
//                String str2 = getLev(uId);
//                String[] getInfo2;
//                getInfo2 = str2.split(",");
//                level = Integer.parseInt(getInfo2[0]);
//                sta = Integer.parseInt(getInfo2[1]);
//                txt4.setText("Level = " +level);
//            }else if(checkActivity() && inside && park){
//                level=2;
//
//                updateLev(level,uId);
//
//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
//                txt3.setText(showsta);
//                String str2 = getLev(uId);
//                String[] getInfo2;
//                getInfo2 = str2.split(",");
//                level = Integer.parseInt(getInfo2[0]);
//                sta = Integer.parseInt(getInfo2[1]);
//                txt4.setText("Level = " +level);
//            }
//        }
//        if(level==4){
//            if (inside && park){
//                level=3;
//
//                updateLev(level,uId);
//
//
//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
//                txt3.setText(showsta);
//                String str2 = getLev(uId);
//                String[] getInfo2;
//                getInfo2 = str2.split(",");
//                level = Integer.parseInt(getInfo2[0]);
//                sta = Integer.parseInt(getInfo2[1]);
//                txt4.setText("Level = " +level);
//            }
//        }
//        txt3.setText(showsta);
//        String str2 = getLev(uId);
//        String[] getInfo2;
//        getInfo2 = str2.split(",");
//        level = Integer.parseInt(getInfo2[0]);
//        sta = Integer.parseInt(getInfo2[1]);
//        txt4.setText("Level = " +level);
//    }

    public String getCurrentTime(){
        //current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf2.format(cal.getTime());
    }

    // get level มาเชคว่าอยู่ขั้นไหนแล้ว
    public String getLev(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/Level.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    // update level
    public String updateLev(int lev,String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/uplevel.php?uId="+uId+"&level="+lev);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    // decrease available parking lot
    public void updateStatusPark(String uId,String pId,String time,String date){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/UpdateParkStatus.php?status="+1+"&pId="+pId+"&timeIn="+time+"&date="+date+"&uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ดึงค่า parkId ที่ user park ไปแล้วเพื่ออัพเดตสถานะออกจากที่จอดและก็ไว้เก็บประวัติ
    public String getParkId(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getParkId.php?uId="+uId);
            Log.wtf("show park id : " , response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    // increase available parking lot
    public void updateStatusNotPark(String uId,String pId,String time,String parkId){
        Log.wtf("uId ::",uId);
        Log.wtf("pId ::",pId);
        Log.wtf("time ::",time);
        Log.wtf("get park id :: ",parkId);
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/UpdateNotParkStatus.php?status="+0+"&pId="+pId+"&timeOut="+time+"&uId="+uId+"&parkId="+parkId);
            Log.wtf("show update not park mai : " , response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showActivity(){

        txt2.setText(activityRecognition.getActivity()+"");
        vehicle=activityRecognition.getActivity();

        if(vehicle==0){
//            txt2.setText("In Vehicle"+vehicle);
            txt2.setText("In Vehicle");
        }else{
            txt2.setText("Not in vehicle");
        }
    }

    public void showResult(String t){
        txt.setText(t);
    }

    public void onClick(View v){
        txt.setText("FUCK555");

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.wtf("","location cc");

        final MarkerOptions markerOptions = new MarkerOptions();

        final LatLng latLng = new LatLng(13.6546897, 100.4946202);
        ArrayList<LatLng> latlngs = new ArrayList<>();
        latlngs.add(new LatLng(13.6530663, 100.4942920));
        latlngs.add(new LatLng(13.6494580, 100.4936801));

        if (mCurrLocationMarker2 != null) {
            mCurrLocationMarker2.remove();
        }
        int j=0;
        for (LatLng point : latlngs) {
            markerOptions.position(point);
            markerOptions.title("N: CB"+j+" A: "+15+" R: "+5);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            markerOptions.snippet("someDesc");
//            mCurrLocationMarker2 = mMap.addMarker(markerOptions);
//            mCurrLocationMarker2.showInfoWindow();
//

            if(getActivity()!=null) {
                IconGenerator generator = new IconGenerator(getActivity());
                generator.setStyle(generator.STYLE_BLUE);

//            generator.setBackground(getResources().getDrawable(R.drawable.amu_bubble_mask));
//            Bitmap icon = generator.makeIcon("N: CB"+j+" A: "+15+" R: "+5);
                Bitmap icon = generator.makeIcon(100 + j + "");


//            generator.setContentView(txt);

                MarkerOptions tp = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromBitmap(icon));
                mCurrLocationMarker2 = mMap.addMarker(tp);

            }
//            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                final AlertDialog.Builder builder =
//                        new AlertDialog.Builder(getActivity());
//                builder.setMessage("Name: \t\t\t\t\t\t\t\t\t\t\t\t\t\t"+pName+"\n\nAvailable: \t\t\t\t\t\t\t\t\t\t\t"+available+"\n\nReserve: \t\t\t\t\t\t\t\t\t\t\t\t"+reserved);
//                builder.setPositiveButton("ok", new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id){
//                        builder.getContext();
//                    }
//                });
//                builder.show();
//            }
//            });
            j=j+150;
        }

        float[] distance = new float[2];

                        /*
                        Location.distanceBetween( mMarker.getPosition().latitude, mMarker.getPosition().longitude,
                                mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
                                */
//                        Location mylocation = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
        mLastLocation = location;
        Location.distanceBetween( mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                13.6546897, 100.4946202, distance);

        if( distance[0] > 50 ){
//            Log.e("Outside "+location.getLatitude(),location.getLongitude()+"");
//            Toast.makeText(getActivity(), "Outside, distance from center: " + distance[0] + " radius: " + 80, Toast.LENGTH_LONG).show();
            showResult("Outside");
            inside=false;
            text="Outside"+distance[0];
            txt.setText("Outside");

        } else {
//            Log.e("Inside "+location.getLatitude()+"",location.getLongitude()+"");
//            Toast.makeText(getActivity(), "Inside, distance from center: " + distance[0] + " radius: " + 80 , Toast.LENGTH_LONG).show();
            showResult("Inside");
            inside=true;
            text="Inside"+distance[0];
            txt.setText("Inside");
        }

        if (mCurrLocationMarker1 != null) {
            mCurrLocationMarker1.remove();
        }
        getNumParkinglot();

        //Place current location marker

        markerOptions.position(latLng);
        markerOptions.title("N: "+pName+" A: eiei"+available+" R: "+reserved);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

//        markerOptions.snippet(txt);
//        markerOptions.draggable(true);
//        mCurrLocationMarker1 = mMap.addMarker(markerOptions);
//        mCurrLocationMarker1.showInfoWindow();


if(isAdded()) {
    IconGenerator generator = new IconGenerator(getActivity().getApplicationContext());
    generator.setStyle(generator.STYLE_BLUE);
//                                Bitmap icon = generator.makeIcon("N: "+pName+" A: "+available+" R: "+reserved);
    Bitmap icon = generator.makeIcon(available);
    MarkerOptions tp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(icon));
    mCurrLocationMarker1 = mMap.addMarker(tp);
    
}

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("Name: \t\t\t\t\t\t\t\t\t\t\t\t\t\t"+pName+"\n\nAvailable: \t\t\t\t\t\t\t\t\t\t\t"+available+"\n\nReserve: \t\t\t\t\t\t\t\t\t\t\t\t"+reserved);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        builder.getContext();
                    }
                });
                builder.show();
            }
        });

        showActivity();
        showStatus2();

    }

    // จำนวน available and reserved
    public void getNumParkinglot(){

        ArrayList<HashMap<String, String>> reserve = null;
        reserve = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map2;
        String[] getInfo2;
        String pId2,res;
        String str2 = getCountRes();
        Scanner scan = new Scanner(str2);
        for(int j = 0; scan.hasNext(); j++){
            String data = scan.nextLine();
            System.out.println(data);
            getInfo2 = data.split(",");
            pId2 = getInfo2[0];
            res = getInfo2[1];
            map2 = new HashMap<String, String>();
            map2.put("pId2", pId2);
            map2.put("res", res);
            reserve.add(map2);
        }

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAvailable.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String str = "1,14Floor Building,100,0,13.650784,100.496006\n2,CB2,110,10,13.650784,100.496006";
        String str = response;
        String[] getInfo;
        String parkName,a,amount,la,lo,pId,pa;

        ArrayList<HashMap<String, String>> parkinglot = null;

        parkinglot = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        Scanner scanner = new Scanner(str);
        for(int i = 0; scanner.hasNext(); i++){
            int aa=0;
            String data = scanner.nextLine();
            System.out.println(data);
            getInfo = data.split(",");
            pId = getInfo[0];
            parkName = getInfo[1];
            a = getInfo[2];
            amount = getInfo[3];
            la = getInfo[4];
            lo = getInfo[5];
            pa = getInfo[6];
            Log.wtf("getNumParkinglot: ", amount );

            a = Integer.parseInt(amount) - Integer.parseInt(pa) + "";
            if(str2.equals("0,0")) {
                a = Integer.parseInt(amount) - Integer.parseInt(pa) + "";
            }else if(reserve.get(i).get("pId2").toString().equals(pId)){
                a = (Integer.parseInt(amount) -
                        Integer.parseInt(reserve.get(i).get("res").toString()) - Integer.parseInt(pa))+"";
            }

            map = new HashMap<String, String>();
            map.put("pId",pId);
            map.put("pName", parkName);
            map.put("available", a);
            map.put("amount", amount);
            map.put("latitude", la);
            map.put("longitude", lo);
            parkinglot.add(map);
        }

        // วิธีเรียกใช้
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < reserve.size(); j++){
                if(reserve.get(j).get("pId2").toString().equals(parkinglot.get(i).get("pId").toString())){
                }
            }
            pName = parkinglot.get(i).get("pName").toString();
            available = parkinglot.get(i).get("available").toString();
            reserved = parkinglot.get(i).get("amount").toString();
//            latitude = parkinglot.get(i).get("reserved").toString();
//            longitude = parkinglot.get(i).get("reserved").toString();
        }
    }

    public String getCountRes(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/countRes.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }



        Intent intent = new Intent( getActivity(), ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 3000, pendingIntent );

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
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