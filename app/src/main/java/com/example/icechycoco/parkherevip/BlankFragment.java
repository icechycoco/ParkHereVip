package com.example.icechycoco.parkherevip;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


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
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_ID = "uId";
    private String uId;

    //para
    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView txt,txt2,txt3,txt4;
    Button btn;
    public ActivityRecognizedService activityRecognition = new ActivityRecognizedService();
    boolean inside;
    static int vehicle;
    boolean park = false;
    static String parktxt = "not park";
    static int status = 999;
    // connect db
    String response = null;
    getHttp http = new getHttp();

    String parkId,timeIn,timeOut;
    String pName,available,reserved;

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
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_blank, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        txt = (TextView) v.findViewById(R.id.textView);
        txt2 = (TextView) v.findViewById(R.id.textView2);
        txt3 = (TextView) v.findViewById(R.id.textView3);
        txt4 = (TextView) v.findViewById(R.id.textView4);

//        btn = (Button) v.findViewById(R.id.btn_park);


//        txt.setOnClickListener((new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                //Fragment
//                MapParkFragment mapParkFragment = new MapParkFragment().newInstance(uId);
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, mapParkFragment);
//                transaction.commit();
//            }
//        }));
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

    public void showStatus(){

        int lev = getLev(uId);

        //if(checkActivity() && !inside && !park){
        if(true){
            status=1;
            //String output = updateLev(1,"10003");
            try {
                response = http.run("http://parkhere.sit.kmutt.ac.th/uplevel.php?uId="+uId+"&level="+status);
            } catch (IOException e) {
                e.printStackTrace();
            }
            txt4.setText(status+"");
            Log.wtf("test","im here");
            Log.wtf("test",uId);
            Toast.makeText(getContext(),response, Toast.LENGTH_SHORT).show();
        }
        if(lev==1 && inside && !park){
            status=2;
            //updateLev(2,"10002");
            try {
                response = http.run("http://parkhere.sit.kmutt.ac.th/uplevel.php?uId="+uId+"&level="+status);
            } catch (IOException e) {
                e.printStackTrace();
            }
            txt3.setText(parktxt);
        }
        if(lev==2){
            if(!checkActivity() && inside && !park){
                status=3;
                updateLev(3,"10002");
                txt3.setText(parktxt);
            }else if(checkActivity() && !inside && park){
                status=1;
                park=false;
                parktxt="Not Park";
                updateLev(1,"10002");
                parkId = getParkId(uId);
                timeOut = getCurrentTime();
                updateStatusNotPark("10002","1",timeOut,parkId);
                txt3.setText(parktxt);
            }
        }
        if(lev==3){
            if(!checkActivity() && !inside && !park){
                status=4;
                park=true;
                parktxt="Park";
                updateLev(4,"10002");
                timeIn = getCurrentTime();
                updateStatusPark("10002","1",timeIn,"2017-08-18");
                txt3.setText(parktxt);
            }else if(checkActivity() && inside && park){
                status=2;
                updateLev(2,uId);
                txt3.setText(parktxt);
            }
        }

        if(lev==4){
            if (inside && park){
                status=3;
                updateLev(3,"10002");
                txt3.setText(parktxt);
            }
        }
        txt3.setText(parktxt);
        txt4.setText("Status = "+status);
    }

    public String getCurrentTime(){
        //current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf2.format(cal.getTime());
    }

    // get level มาเชคว่าอยู่ขั้นไหนแล้ว
    public int getLev(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/Level.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.wtf("555",response);
        return Integer.parseInt(response);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    // increase available parking lot
    public void updateStatusNotPark(String uId,String pId,String time,String parkId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/UpdateNotParkStatus.php?status="+0+"&pId="+pId+"&timeOut="+time+"&uId="+uId+"&parkId="+parkId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showActivity(){

        txt2.setText(activityRecognition.getActivity()+"");
        vehicle=activityRecognition.getActivity();

        if(vehicle==0){
            txt2.setText("In Vehicle"+vehicle);
        }else{
            txt2.setText("Not in vehicle"+vehicle);
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

        mLastLocation = location;

        float[] distance = new float[2];

                        /*
                        Location.distanceBetween( mMarker.getPosition().latitude, mMarker.getPosition().longitude,
                                mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
                                */

        Location.distanceBetween( location.getLatitude(), location.getLongitude(),
                13.650529, 100.495745, distance);

        String txt = "eieiza55plus";
        if( distance[0] > 30 ){
            Log.e("Outside "+location.getLatitude(),location.getLongitude()+"");
            Toast.makeText(getActivity(), "Outside, distance from center: " + distance[0] + " radius: " + 20, Toast.LENGTH_LONG).show();
            showResult("Outside");
            inside=false;
            txt="Outside"+distance[0];

        } else {
            Log.e("Inside "+location.getLatitude()+"",location.getLongitude()+"");
            Toast.makeText(getActivity(), "Inside, distance from center: " + distance[0] + " radius: " + 20 , Toast.LENGTH_LONG).show();
            showResult("Inside");
            inside=true;
            txt="Inside";
        }

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions.snippet(txt);
        markerOptions.draggable(true);
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        showActivity();
        showStatus();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    // จำนวน available and reserved
    public void getNumParkinglot(){

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAvailable.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = response;
        String[] getInfo;
        String parkName,a,r;
        ArrayList<HashMap<String, String>> parkinglot = null;

        parkinglot = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        Scanner scanner = new Scanner(str);

        for(int i = 0; scanner.hasNext(); i++){
            String data = scanner.nextLine();
            System.out.println(data);

            getInfo = data.split(",");
            parkName = getInfo[0];
            a = getInfo[1];
            r = getInfo[2];


            map = new HashMap<String, String>();
            map.put("pName", parkName);
            map.put("available", a);
            map.put("reserved", r);
            parkinglot.add(map);
        }

        // วิธีเรียกใช้
        for (int i = 0; i < parkinglot.size(); i++) {
            pName = parkinglot.get(i).get("pName").toString();
            available = parkinglot.get(i).get("available").toString();
            reserved = parkinglot.get(i).get("reserved").toString();
        }

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
