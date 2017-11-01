package com.example.icechycoco.parkherevip;

import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
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

/**
 * Created by icechycoco on 10/22/2017 AD.
 */

public class AvailableFragment extends Fragment implements LocationListener{

    private static final String KEY_ID = "uId";
    private static String uId;
    private static final String KEY_PO = "po";
    private static String po;

    //variable
    String[] getInfo;
    String getTime,timeIn;
    int getCost,getMCost;
    String currentTime,getFloor;
    Date date1,date2;
    long realFee;
    long diffHours = 0;
    int level,sta;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    // connect db
    String response = null;
    getHttp http = new getHttp();

    String dis;
    LatLng origin;
    LatLng destination;

    Location cLocation;

    ArrayList sentDistance = null;

    public AvailableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AvailableFragment newInstance(String uId,String po) {
        AvailableFragment fragment = new AvailableFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PO, po);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
            po = bundle.getString(KEY_PO);
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();
        sentDistance = getDistance();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                showActivity();
//                                showStatus2();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_available, container, false);
        CustomAdapterParkArea adapter = new CustomAdapterParkArea(getContext(), getParkArea(), sentDistance);
        ListView listView = (ListView) v.findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });

        return v;
    }

    public void setuId(String uId){
        this.uId = uId;
    }

    public String getuId(){
        return this.uId;
    }

    public void setPo(String po){
        this.po = po;
    }

    public String getPo(){
        return this.po;
    }

    public String getLev(String uId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/Level.php?uId="+uId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public ArrayList getDistance(){
        ArrayList<HashMap<String, String>> distance = null;
        distance = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> parkarea;
        String[] getInfo;
        double lat,lon;

        for(int i=1;i<4;i++){
            String location = getLocation(i);
            getInfo = location.split(",");
            lat = Double.parseDouble(getInfo[0]);
            lon = Double.parseDouble(getInfo[1]);
            String serverKey = "AIzaSyCrvg_MLcS21bt3a11mN9MFKg8FTqBNkkc";

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);

//                locationManager.requestLocationUpdates(provider,1000,0, (android.location.LocationListener) this);

            cLocation = locationManager.getLastKnownLocation(provider);
            cLocation = new Location(locationManager.getBestProvider(criteria, false));
            cLocation.setLatitude(currentLatitude);
            cLocation.setLongitude(currentLongitude);
//            LatLng origin = new LatLng(cLocation.getLatitude(), cLocation.getLongitude());
            LatLng origin = new LatLng(13, 100);

            parkarea = new HashMap<String, String>();

            //Log.wtf("Current Location",currentLatitude+" "+currentLongitude);
            Log.wtf("Current Location",origin.toString());
            LatLng destination = new LatLng(lat, lon);
            Log.wtf("Current Location",destination.toString());
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
//                                Log.wtf("Direction Status",leg.getDistance().getText());
                                setDistance(leg.getDistance().getText());
                                Log.wtf("dis in",getDis());
                                //parkarea.put("d",dis);

                            }
                        }
                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Log.wtf("onDirectiom.0nFailure", t);
                        }
                    });
            Log.wtf("dis out1",getDis());
            parkarea.put("d",getDis());
            //parkarea.put("d","1");
            Log.wtf("dis out2",getDis());
            distance.add(parkarea);
            //Log.wtf("distance = ",distance.get(i).get("d").toString());
        }
        return distance;
    }

    public void setDistance(String d){
        this.dis = d;
    }

    public String getDis(){
        return dis;
    }

    public ArrayList getParkArea(){

        ArrayList<HashMap<String, String>> reserve = null;
        reserve = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map2;
        String[] getInfo2;
        int pId2;
        int res,remain;
        String[] getInfo;
        String parkName,a,pId;

        String str = getAvailable();
        String str2 = getCountRes();

        Scanner scanner = new Scanner(str);
        Scanner scan = new Scanner(str2);

        for(int j = 0; scan.hasNext(); j++){
            String data = scan.nextLine();
            getInfo2 = data.split(",");
            pId2 = Integer.parseInt(getInfo2[0]);
            res = Integer.parseInt(getInfo2[1]);
            //nRes.add(pId2, res);

            String data2 = scanner.nextLine();
            getInfo = data2.split(",");
            pId = getInfo[0];
            parkName = getInfo[1];
            remain = Integer.parseInt(getInfo[2]);

            map2 = new HashMap<String, String>();
            map2.put("pId", pId);
            map2.put("pName", parkName);
            map2.put("available", remain - res + "" );
            reserve.add(map2);
        }

        return reserve;
    }

    public String getLocation(int pId){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getLocation.php?pId="+pId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getCountRes(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/countRes.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getAvailable(){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/getAvailable.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getCurrentTime(){
        //current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf2.format(cal.getTime());
    }

    @Override
    public void onLocationChanged(Location location) {
        cLocation = location;
    }

    public interface OnFragmentInteractionListener {
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
