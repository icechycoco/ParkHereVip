package com.example.icechycoco.parkherevip;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveinfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveinfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveinfoFragment extends Fragment {


    String root;
    String fname;


    // gui
    Button btn, btnC, btnS;
    EditText etGuestN, etGuestS, etLicen, etEmail, etPhone, etDate;
    TextView textView;
    RadioGroup radioGroup;

    String setName, setSur, setLicen, setEmail, setPhone, setgId;
    String setDate, setQR, setTimeRes, setuId, setDateRes;
    String getCode, setpId, setInterval, setStatus;
    int getDate;
    String[] getGInfo;
    String gId, gEmail, gLicen, gPhone;

    //setpId ต้องรับค่าจากปุ่มที่กดเลือกแอเรีย
    // setuId ต้องรับค่ามาจากหน้าลอกอิน
    // setQR รับค่ามาจากตัว generate

    String response = null;
    getHttp http = new getHttp();

    private static final String KEY_ID = "uId";
    private String uId;
    private static final String KEY_PID = "pId";
    String pId;

    Calendar myCalendar = Calendar.getInstance();

    private OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;

    public ReserveinfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReserveinfoFragment newInstance(String uId,String pId) {
        ReserveinfoFragment fragment = new ReserveinfoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, uId);
        args.putString(KEY_PID,pId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uId = bundle.getString(KEY_ID);
        }
        Toast.makeText(getContext(), "uId : " + uId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_reserve, container, false);
        btn = (Button) v.findViewById(R.id.btn_reserve);
        btnC = (Button) v.findViewById(R.id.btn_check);
        btnS = (Button) v.findViewById(R.id.btn_select);

        etGuestN = (EditText) v.findViewById(R.id.etGuestN);
        etGuestS = (EditText) v.findViewById(R.id.etGuestS);
        etLicen = (EditText) v.findViewById(R.id.etLicen);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPhone = (EditText) v.findViewById(R.id.etPhone);
        etDate = (EditText) v.findViewById(R.id.etDate);

        textView = (TextView) v.findViewById(R.id.tv_result);

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), Mydate,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
               RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                switch(radioButton.getText().toString()) {
                    case "06:00 - 12:00":
                        setInterval = "00";
                        break;
                    case "13:00 - 18:00":
                        setInterval = "10";
                        break;
                    case "6:00 - 18:00":
                        setInterval = "11";
                        break;
                }
            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName = etGuestN.getText().toString();
                setSur = etGuestS.getText().toString();
                try {
                    response = http.run("http://parkhere.sit.kmutt.ac.th/checkG.php?gFirstN=" + setName + "&gLastN=" + setSur);
                    //Log.wtf("eie",response);
                } catch (IOException e) {
                    // TODO Auto-generat-ed catch block
                    e.printStackTrace();
                }

                if (response.equals("0")) {
                    textView.setText("please fill in a guest infomation");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setLicen = etLicen.getText().toString();
                            setEmail = etEmail.getText().toString();
                            setDate = etDate.getText().toString();
                            setPhone = etPhone.getText().toString();
                            //current time
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                            setTimeRes = sdf2.format(cal.getTime());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            setDateRes = sdf.format(new Date());
                            setuId = uId;
                            //setpId = "1"; //รับค่าจากการเลือกแอเรีย
                            setpId = pId;
                            //setgId = "73"; // ตารางต้องเหมือนกันอะ reserve and guest ถึงจะสร้างได้ ยัง bug อยุ่
                            setQR = randCode();
                            setStatus = "0"; // 0 = จองอยู่ 1=จอด อาจจะไม่ต้องมีก็ได้
                            //ทำไมมันไม่ insert ข้อมูลในตาราง reserve , guest ก็ไม่ขึ้นละสาส
                            //แก้ php เรื่องหักลบ amount บางทีมันเป็น -1
                            //ให้มันคืนค่าamountทุกๆเที่ยงคืน

                            sendEmail(setEmail);

                            try {
                                response = http.run("http://parkhere.sit.kmutt.ac.th/newguest.php?gFirstN=" + setName + "&gLastN=" + setSur + "&gEmail=" + setEmail + "&gLicense=" + setLicen + "&gPhone=" + setPhone);
                                Log.wtf("eie",response);
                                //response = http.run("http://parkhere.sit.kmutt.ac.th/UpdateLevelUser.php?level="+1+"&uId="+10002);
                                //Log.wtf("eie",response);
                            } catch (IOException e) {
                                // TODO Auto-generat-ed catch block
                                e.printStackTrace();
                            }
                            // ติดตรงดาต้าเบส gId
                            reserve(setuId, setpId, setgId, setDate, setInterval, setTimeRes, setQR, setStatus,setDateRes);
                            BlankFragment blankFragment = new BlankFragment().newInstance(uId);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, blankFragment);
                            transaction.commit();
                        }
                    });
                } else {
                    textView.setText("Existed Guest Infomation");

                    getGInfo = response.split(" ");
                    gId = getGInfo[0];
                    gEmail = getGInfo[1];
                    gLicen = getGInfo[2];
                    gPhone = getGInfo[3];

                    etEmail.setText(gEmail);
                    etLicen.setText(gLicen);
                    etPhone.setText(gPhone);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            setDate = etDate.getText().toString();
                            //current time
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                            setTimeRes = sdf2.format(cal.getTime());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            setDateRes = sdf.format(new Date());
                            setuId = uId; //รับค่ามาจากหน้าลอคอิน
                            //setpId = "1"; //รับค่าจากการเลือกแอเรีย
                            setpId = pId;
                            setQR = randCode();
                            setStatus = "0"; // 0 = จองอยู่ 1=จอด อาจจะไม่ต้องมีก็ได้
                            setgId = gId;
                            //ทำไมมันไม่ insert ข้อมูลในตาราง reserve , guest ก็ไม่ขึ้นละสาส
                            //แก้ php เรื่องหักลบ amount บางทีมันเป็น -1
                            //ให้มันคืนค่าamountทุกๆเที่ยงคืน

                            reserve(setuId, setpId, setgId, setDate, setInterval, setTimeRes, setQR, setStatus,setDateRes);
                            try {
                                Bitmap bitmap = TextToImageEncode(setQR);
                                saveImage(bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }

                            sendEmail(gEmail);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    delImage(fname);
                                }
                            }, 5000);

                            ReserveFragment reserveFragment = new ReserveFragment().newInstance(uId);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, reserveFragment);
                            transaction.commit();

                        }
                    });
                }
            }
        });

        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    DatePickerDialog.OnDateSetListener Mydate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateDisplay(dayOfMonth,monthOfYear,year);
        }
    };

    private  void updateDisplay(int year,int month,int day){
        etDate.setText(new StringBuilder()
                .append(day).append("-")
                .append(month+1).append("-")
                .append(year).append(" "));
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

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    public void reserve(String uId,String pId, String gId, String date, String interval, String time, String code,String status,String dateRes){
        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/reserve.php?uId=" + uId + "&pId=" + pId + "&gId=" + gId + "&date=" + date + "&timeInterval=" + interval + "&timeRes=" + time + "&code=" + code + "&status=" + status + "&dateRes="+ dateRes);
            //response = http.run("http://parkhere.sit.kmutt.ac.th/UpdateLevelUser.php?level="+1+"&uId="+10002);
            //Log.wtf("eie",response);
        } catch (IOException e) {
            // TODO Auto-generat-ed catch block
            e.printStackTrace();
        }
    }

    public String randCode(){
        int min = 10000;
        int max = 99999;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;

        try {
            response = http.run("http://parkhere.sit.kmutt.ac.th/checkCode.php?code="+i1);

        } catch (IOException e) {

            // TODO Auto-generat-ed catch block

            e.printStackTrace();
        }

        getCode = response;

        if(getCode.equals("n")) {

            return "" + i1;
        }
        return randCode();

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(android.R.color.black):getResources().getColor(android.R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void saveImage(Bitmap bitmap) {


        root = Environment.getExternalStorageDirectory().toString();
//        File myDir=new File(root+"/saved_images");
//        myDir.mkdirs();
//        Random generator = new Random();
        fname = "Image-500.jpg";
        File file = new File (root, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delImage(String name){

        File file = new File(root+"/"+name);
        file.delete();

    }

    public void sendEmail(final String email){

        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender(
                            "vipsmartpark@gmail.com",
                            "villicepark");




                    sender.addAttachment(root+"/"+fname);
                    Log.wtf("mail",root);
                    sender.sendMail("Confirm Park Reservation at KMUTT",
                            "To: " + setName + setSur + "\n\n" +
                                    "this is your code : " + setQR + "\n\n" +
                                    "This mail has been sent from ParkHere application.",
                            "vipsmartpark@gmail.com",
                            email);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }




}
