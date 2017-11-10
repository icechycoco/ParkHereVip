package com.example.icechycoco.parkherevip;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

import static android.content.Context.SENSOR_SERVICE;
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
        GoogleApiClient.OnConnectionFailedListener,LocationListener, SensorEventListener, StepListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_ID = "uId";
    private String uId,best;
    private static final String KEY_PO = "po";
    private String po;
    private static final String KEY_PID = "pid";
    private String pid;
    private static final String KEY_LOC = "loc";
    private String loc;
    private static final String KEY_PARKLOC = "parkLoc";
    private String parkLoc;

    int p=0;

    Drawable image;
    Resources res;
    BitmapDrawable finalImage;

    String text = "eieiza55plus";
    //para
    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker[] mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView txt,txt2,txt3,txt4,txt5;
    Button btn;
    public ActivityRecognizedService activityRecognition = new ActivityRecognizedService();
    boolean inside;
    static int vehicle;
    static boolean park = false;
    // connect db
    String response = null;
    getHttp http = new getHttp();

    String parkId,timeIn,timeOut;
    String[] pId,pName,available,reserved,latitude,longitude;
    long la,lo;
    int level,sta;
    String showsta = "NULL";

    //SensorManager sensorManager;
    boolean running = false;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;

    int i;
    boolean focus = true;

    android.location.LocationListener locationListener;
    //variable
    String[] getInfo;
    String getTime;
    int getCost,getMCost;
    String currentTime,getFloor;
    Date date1,date2;
    long realFee;
    long diffHours = 0;


    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String uId, String po, String pid , String loc) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PO, po);
        args.putString(KEY_PID, pid);
        args.putString(KEY_LOC, loc);
        fragment.setArguments(args);
        return fragment;
    }

    public static BlankFragment newInstance(String uId, String po, String parkLoc) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PO, po);
        args.putString(KEY_PARKLOC, parkLoc);
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
            pid = bundle.getString(KEY_PID);
            loc = bundle.getString(KEY_LOC);
            parkLoc = bundle.getString(KEY_PARKLOC);
        }

        String str = getLev(uId);
        String[] getInfo;
        getInfo = str.split(",");
        level = Integer.parseInt(getInfo[0]);
        sta = Integer.parseInt(getInfo[1]);

        if(sta==1){

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
                getFloor = getInfo[3];

                // convert string time in database to time
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
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
                }

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_fee);

                final TextView tin = (TextView) dialog.findViewById(R.id.textView13);
                final TextView tout = (TextView) dialog.findViewById(R.id.textView15);
                final TextView estfee = (TextView) dialog.findViewById(R.id.textView17);
                final TextView floor = (TextView) dialog.findViewById(R.id.textView19);
                tin.setText(timeIn);
                tout.setText(currentTime);
                estfee.setText(realFee +" BAHT");
                floor.setText(getFloor);

                dialog.show();

            }
        }

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
        txt5 = (TextView) v.findViewById(R.id.textView5);

//        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        txt.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showResult(text);
                showActivity();
                showStatus2();
            }
        }));


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
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
        }
        Log.wtf("","resume");

//        locationListener


//        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1, (android.location.LocationListener) this);
//        Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);


//        locationManager.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
    }
    public void onPause(){
        super.onPause();
        running = false;
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        //txt5.setText(TEXT_NUM_STEPS + numSteps);
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
        if(vehicle==0 || vehicle == 4){
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

//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
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


                ArrayList<HashMap<String,String>> arrayList= checkLocation();
                for(int i =0; i<arrayList.size(); i++){
                    float[] distance = new float[2];
                    float minDis = 100;
                    Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            Double.parseDouble(arrayList.get(i).get("la")), Double.parseDouble(arrayList.get(i).get("lo")), distance);
                    if(distance[0]<minDis){
                        minDis = distance[0];
                        pid = arrayList.get(i).get("pl");
                    }
                }


                //current time
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                currentTime = sdf2.format(cal.getTime());


                Log.wtf("showStatus2: ",uId+" "+ pid+" "+ timeIn+" "+ currentTime.toString()+" "+mLastLocation.getLatitude()+" "+mLastLocation.getLongitude()+"");
                // ไม่รู้ถูกป่าว แบบกูเพิ่มให้มันจำ lat long ที่ปาร์คไปเลย ตอนสร้างตารางใหม่อะ

                updateStatusPark(uId, pid, timeIn, currentTime,mLastLocation.getLatitude()+"",mLastLocation.getLongitude()+"");
//                updateStatusPark("10003", "1", "10:10:10", "2017-11-11","13.65","13.65");
//                Log.wtf("showStatus2: ",""+updateStatusPark(uId, pid, timeIn, "2017-11-10",
//                        mLastLocation.getLatitude()+"",mLastLocation.getLongitude()+""));

                showDialogReminder();

                Log.wtf("Not Park","2");

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

//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
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

                updateStatusNotPark(uId,pid,timeOut,parkId);

                Log.wtf("why i = ","wow4");

//                Log.wtf("level is : ",getLev(uId));
//                Log.wtf("status is : ",sta+"");
                Log.wtf("Park","2");
                txt3.setText(showsta);
                String str2 = getLev(uId);
                String[] getInfo2;
                getInfo2 = str2.split(",");
                level = Integer.parseInt(getInfo2[0]);
                sta = Integer.parseInt(getInfo2[1]);
                txt4.setText("Level = " +level);
                Log.wtf("why i = ","wow5");


                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        }

//        Log.wtf("level is : ",getLev(uId)+"");
//        Log.wtf("status is : ",sta+"");
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


    public ArrayList checkLocation(){
        String[] getInfo;
        String plo,lat,lon;
        ArrayList<HashMap<String, String>> parkLocation = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> latlong;

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAllLocation.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(response);
        for(int i = 0; scanner.hasNext(); i++){
            String data = scanner.nextLine();
            getInfo = data.split(",");
            plo = getInfo[0];
            lat = getInfo[1];
            lon = getInfo[2];

            latlong = new HashMap<String, String>();
            latlong.put("pl", plo);
            latlong.put("la", lat);
            latlong.put("lo", lon);
            parkLocation.add(latlong);
        }
        return parkLocation;
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
    public String updateStatusPark(String uId,String pId,String time,String date, String lat,String lon){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/UpdateParkStatus.php?status="+1
                    +"&pId="+pId+"&timeIn="+time+"&date="+date+"&uId="+uId+"&lat="+lat+"&lon="+lon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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

        //txt2.setText(activityRecognition.getActivity()+"");
        vehicle=activityRecognition.getActivity();

        Log.wtf("showActivity: ",""+vehicle );
        if(vehicle==0){
//            //txt2.setText("In Vehicle"+vehicle);
            txt2.setText("In Vehicle");
        }else{
            txt2.setText("Not in vehicle");
        }
    }

    public void showResult(String t){
        txt.setText(t);
    }

    public void onClick(View v){
        //txt.setText("FUCK555");

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

        if(parkLoc==null) {
            if(focus) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                focus = false;
            }
            String serverKey = "AIzaSyCrvg_MLcS21bt3a11mN9MFKg8FTqBNkkc";
            LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
            String[] des = loc.split(",");
            LatLng destination = new LatLng(Double.parseDouble(des[0]), Double.parseDouble(des[1]));
            final MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(destination);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .unit(Unit.METRIC)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            if (direction.isOK()) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                if(isAdded()) {
                                    mMap.clear();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline
                                            (getActivity().getApplicationContext(), directionPositionList, 5, Color.BLUE);
                                    mMap.addPolyline(polylineOptions);
                                }
                                mMap.addMarker(markerOptions);
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Log.wtf("onDirectiom.0nFailure", t);
                        }
                    });
        }

        float[] distance = new float[2];

                        /*
                        Location.distanceBetween( mMarker.getPosition().latitude, mMarker.getPosition().longitude,
                                mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
                                */
//                        Location mylocation = locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
        mLastLocation = location;

        ArrayList<HashMap<String,String>> arrayList= checkLocation();
        float minDis = 1000;
        for(int i =0; i<arrayList.size(); i++){
            float[] distance2 = new float[2];
            Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                    Double.parseDouble(arrayList.get(i).get("la")), Double.parseDouble(arrayList.get(i).get("lo")), distance);
            if(distance[0]<minDis) {
                minDis = distance[0];
                pid = arrayList.get(i).get("pl");
            }
        }

        if (minDis > 30) {
            showResult("Outside");
            inside = false;
            text = "Outside" + distance[0];
        } else {
            showResult("Inside");
            inside = true;
            text = "Inside" + distance[0];
            txt.setText("Inside");
        }
//        Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
//                13.6546897, 100.4946202, distance);
//
//
//
//        if (distance[0] > 50) {
////            Log.e("Outside "+location.getLatitude(),location.getLongitude()+"");
////            Toast.makeText(getActivity(), "Outside, distance from center: " + distance[0] + " radius: " + 80, Toast.LENGTH_LONG).show();
//            showResult("Outside");
//            inside = false;
//            text = "Outside" + distance[0];
//            //txt.setText("Outside");
////            txt.setText(getElevationFromGoogleMaps(mLastLocation.getLatitude(),mLastLocation.getLongitude())+"");
////            Log.wtf("H is : " , getElevationFromGoogleMaps(mLastLocation.getLatitude(),mLastLocation.getLongitude())+"");
//
//        } else {
////            Log.e("Inside "+location.getLatitude()+"",location.getLongitude()+"");
////            Toast.makeText(getActivity(), "Inside, distance from center: " + distance[0] + " radius: " + 80 , Toast.LENGTH_LONG).show();
//            showResult("Inside");
//            inside = true;
//            text = "Inside" + distance[0];
//            //txt.setText("Inside");
//        }

        showActivity();
        showStatus2();


        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }

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
        image = ContextCompat.getDrawable(getActivity(), R.drawable.marker_01);

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

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if(parkLoc!=null){
            final MarkerOptions markerOptions = new MarkerOptions();
            Log.wtf("okwoi",parkLoc);
            String[] des = parkLoc.split(",");

            LatLng parkLocation = new LatLng(Double.parseDouble(des[0]), Double.parseDouble(des[1]));
            markerOptions.position(parkLocation);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
            CameraPosition parkPosition = new CameraPosition.Builder()
                    .target(parkLocation)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(parkPosition));
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


        Intent intent = new Intent(getActivity(), ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 3000, pendingIntent);

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

    private Drawable createMarkerIcon(Drawable backgroundImage, String text,
                                      int width, int height) {

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // Create a canvas, that will draw on to canvasBitmap.
        Canvas imageCanvas = new Canvas(canvasBitmap);

        // Set up the paint for use with our Canvas
        Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setTextSize(16f);

        // Draw the image to our canvas
        backgroundImage.draw(imageCanvas);

        // Draw the text on top of our image
        imageCanvas.drawText(text, width / 2, height / 2, imagePaint);

        // Combine background and text to a LayerDrawable
        LayerDrawable layerDrawable = new LayerDrawable(
                new Drawable[]{backgroundImage, new BitmapDrawable(canvasBitmap)});
        return layerDrawable;
    }


    // เอาไว้เรียก lat long ของ user ที่ parked อยุ่
    public ArrayList getParkLocation(String uId){
        String[] getInfo;
        String lat,lon;
        ArrayList<HashMap<String, String>> parkLocation = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> latlong;

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getParkLatLong.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(response);
        for(int i = 0; scanner.hasNext(); i++){
            String data = scanner.nextLine();
            getInfo = data.split(",");
            lat = getInfo[0];
            lon = getInfo[1];

            latlong = new HashMap<String, String>();
            latlong.put("la", lat);
            latlong.put("lo", lon);
            parkLocation.add(latlong);
        }
        return parkLocation;
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

    public void showDialogReminder(){


        if(isAdded()) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_floor);

            final EditText number = (EditText) dialog.findViewById(R.id.editText);
            final Button cncl = (Button) dialog.findViewById(R.id.button_cancel);
            final Button ok = (Button) dialog.findViewById(R.id.button_login);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // สร้าง php ใหม่เปลี่ยนจำนวน available
                        response = http.run("http://parkhere.sit.kmutt.ac.th/setFloor.php?uId=" + uId + "&floor=" + number.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            cncl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            dialog.show();
        }
    }

}