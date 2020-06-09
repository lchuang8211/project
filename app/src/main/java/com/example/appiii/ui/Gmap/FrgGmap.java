package com.example.appiii.ui.Gmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_dbconectTask;
import com.example.appiii.Interface_AsyncDBTask;
import com.example.appiii.R;
import com.example.appiii.C_MySQLite;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FrgGmap extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "FrgGmap";
    String str_Spot;
    View inflatedView_Gmap;



    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private ArrayList<String> showSpotName = new ArrayList<>();
    private ArrayList<Integer> showSpotDate = new ArrayList<>();
    private ArrayList<Integer> showSpotQueue = new ArrayList<>();
    private ArrayList<Double> showSpotLatitude = new ArrayList<>();
    private ArrayList<Double> showSpotLongitude = new ArrayList<>();
    private ArrayList<String> showSpotDescrbe = new ArrayList<>();

    SharedPreferences sh;
    private View.OnClickListener btn_searchView_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {

//                str_Spot = edTxt_Spot.getText().toString();
                new C_dbconectTask(new Interface_AsyncDBTask() {
                    @Override
                    public void AsyncTaskFinish(String Spot, Double Lat_output, Double Long_output) {    //Double output1, Double output2 String output
//                        txt_getAttraction.setText(String.valueOf(Lat_output) +", "+ String.valueOf(Long_output));
//                        txt_getAttraction.setText(output);
                        LatLng SearchAttraction = new LatLng(Lat_output, Long_output);
                        mMap.addMarker(new MarkerOptions().position(SearchAttraction).title(Spot));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SearchAttraction,16));
                    }
                }).execute(str_Spot);
                Log.i("JSON","執行異步任務");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    static String whichPlanName;
    private View.OnClickListener btn_myPlan_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            sh = getContext().getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
            C_MySQLite helper = new C_MySQLite(getActivity());
            SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME + " from "+ C_Dictionary.TRAVEL_LIST_Table_Name + " WHERE "+C_Dictionary.USER_U_ID+"=?"
                    ,new String[]{sh.getString(C_Dictionary.USER_U_ID,"0") });
            final String[] allPlan;
            int getcount=1;
            if(cursor.getCount()>0) {
                allPlan = new String[cursor.getCount()];
                for (int i=1;i<=cursor.getCount();i++){
                    cursor.moveToNext();
                    allPlan[i-1]=cursor.getString(0);
                }
            }  else{ allPlan = new String[] {"沒有行程"}; }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("請選擇行程");
            builder.setItems(allPlan, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    whichPlanName = allPlan[which];
                    getActivity().setTitle(whichPlanName);
                    btn_myShowDay.setEnabled(true);
                }
            }).create().show();
        }
    };
    private View.OnClickListener btn_myShowDay_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            C_MySQLite helper = new C_MySQLite(getActivity());
            SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select count("+C_Dictionary.TABLE_SCHEMA_DATE+") from "+ C_Dictionary.CREATE_TABLE_HEADER+whichPlanName
                    +" group by "+C_Dictionary.TABLE_SCHEMA_DATE  ,null);
            int getcount=1;
            final String[] allDay;
            if(cursor.getCount()>0){
                allDay = new String[cursor.getCount()];
                for (int i=1;i<=cursor.getCount();i++){
                    cursor.moveToNext();
                    allDay[i-1]="第 "+i+" 天的行程";
                }
            }else{ allDay = new String[]{"第 1 天的行程"}; }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("請選擇一天行程");
            builder.setItems(allDay, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    C_MySQLite helper = new C_MySQLite(getActivity());
                    SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.CREATE_TABLE_HEADER+whichPlanName
                            +" where "+C_Dictionary.TABLE_SCHEMA_DATE+"="+(which+1)+" order by COLUMN_NAME_QUEUE asc" ,null);
                    Log.i(TAG, "onClick: setItems "+ whichPlanName);
                    showSpotDate.clear();
                    showSpotQueue.clear();
                    showSpotName.clear();
                    showSpotLatitude.clear();
                    showSpotLongitude.clear();
                    showSpotDescrbe.clear();
                    NodeGps.clear();
                    if(cursor.getCount()>0){
                        while(cursor.moveToNext()){
                            showSpotDate.add(cursor.getInt(0));
                            showSpotQueue.add(cursor.getInt(1));
                            showSpotName.add(cursor.getString(2));
                            NodeGps.add( new LatLng(cursor.getDouble(3), cursor.getDouble(4)) );
                            showSpotLatitude.add(cursor.getDouble(3));
                            showSpotLongitude.add(cursor.getDouble(4));
                            showSpotDescrbe.add(cursor.getString(5));
                        }
                        getActivity().setTitle(whichPlanName+" : "+allDay[which]);
                        showOneDay(cursor.getCount());
                    }
                }
            }).create().show();
        }
    };
    private ArrayList<LatLng> NodeGps = new ArrayList<>();
    private void showOneDay(int count) {
 // new LatLng(showSpotLatitude.get(i),showSpotLongitude.get(i))

        mMap.clear();
        for (int i=0; i<count;i++){
            Log.i(TAG, "onClick: showOneDay  :"+ NodeGps.get(i) );
            mMap.addMarker(new MarkerOptions().position(NodeGps.get(i)).title(showSpotName.get(i)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NodeGps.get(count-1),14));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Gmap = inflater.inflate(R.layout.frg_gmap,container,false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
//        mapFragment.getMapAsync(this);
        InitialComponent();
        return inflatedView_Gmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings ui_set = mMap.getUiSettings();
        ui_set.setZoomControlsEnabled(true);
        ui_set.setCompassEnabled(true);


    }
    private void InitialComponent() {
        btn_myPlan = inflatedView_Gmap.findViewById(R.id.btn_myPlan);
        btn_myPlan.setOnClickListener(btn_myPlan_click);
        btn_myShowDay = inflatedView_Gmap.findViewById(R.id.btn_myShowDay);
        btn_myShowDay.setOnClickListener(btn_myShowDay_click);
        btn_myShowDay.setEnabled(false);
    }
    Button btn_myPlan, btn_myShowDay;
    EditText edTxt_Spot, edTxt_EndSpot;
    TextView txt_getAttraction;
}
