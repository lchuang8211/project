package com.example.appiii.ui.Gmap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.C_MySQLite;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
//import com.google.android.gms.location.places.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


public class FrgGmap extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "FrgGmap";
    String str_Spot;
    View inflatedView_Gmap;



    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private ArrayList<LatLng> NodeGps = new ArrayList<>();
    private ArrayList<String> showSpotName = new ArrayList<>();
    private ArrayList<Integer> showSpotDate = new ArrayList<>();
    private ArrayList<Integer> showSpotQueue = new ArrayList<>();
    private ArrayList<Double> showSpotLatitude = new ArrayList<>();
    private ArrayList<Double> showSpotLongitude = new ArrayList<>();
    private ArrayList<String> showSpotDescrbe = new ArrayList<>();

    SharedPreferences sh;

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
            }  else{
                allPlan = new String[] {"尚未建立行程表，請前往會員中心建立"};
                btn_myShowDay.setEnabled(false);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("請選擇行程");
            builder.setItems(allPlan, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    whichPlanName = allPlan[which];
                    getActivity().setTitle(whichPlanName);
                    if(!allPlan[0].equals("尚未建立行程表，請前往會員中心建立") && sf.getInt(C_Dictionary.USER_STATUS,0)==1 )
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
    private View.OnClickListener btn_web_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ActWebView.class);
            Bundle bundle = new Bundle();
            bundle.putString("search",search_bar.getQuery().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };



    private void showOneDay(int count) {
        mMap.clear();
        LatLng[] drawline = new LatLng[count];
        for (int i=0; i<count;i++){
            Log.i(TAG, "onClick: showOneDay  :"+ NodeGps.get(i) );
            mMap.addMarker(new MarkerOptions().position(NodeGps.get(i)).title(showSpotName.get(i)));
            drawline[i]=NodeGps.get(i);

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NodeGps.get(count-1),12));

        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add( drawline )
                .color(Color.BLUE)
                .width(3)
        );

    }

    private ArrayList<Address> addressList = new ArrayList<>();
    private ArrayList<Marker> markerName = new ArrayList<>();
    private SearchView.OnQueryTextListener search_bar_query = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mMap.clear();
            String location = search_bar.getQuery().toString();
            addressList = null;
            if (location!=null|| !location.equals("")){
                Geocoder geocoder = new Geocoder(getContext());
                try{
                    addressList = (ArrayList)geocoder.getFromLocationName(location,1);
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (addressList.size()>0){
                    LatLng latLng = new LatLng( addressList.get(0).getLatitude(),addressList.get(0).getLongitude() );
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
    //                Log.i(TAG, "onQueryTextSubmit: Latitude:" + addressList.get(0).getLatitude()+" Longitude:"+addressList.get(0).getLongitude());
    //                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
            }
            if(addressList.size()>=1 && sf.getInt(C_Dictionary.USER_STATUS,0)==1 )
                btn_addPrivateNode.setEnabled(true);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private View.OnClickListener btn_addPrivateNode_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sh = getContext().getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
            C_MySQLite sqlhelper = new C_MySQLite(getActivity());
            final SQLiteDatabase sqlDB = sqlhelper.getReadableDatabase();
            Cursor cursor;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            final View view = layoutInflater.inflate(R.layout.gmap_addprivatenode,null);
            final EditText edtxt_NodeName = view.findViewById(R.id.edtxt_NodeName);
            final EditText edtxt_NodeDescribe = view.findViewById(R.id.edtxt_NodeDescribe);
            final Spinner sp_plan = view.findViewById(R.id.sp_plan);

            final Spinner sp_day = view.findViewById(R.id.sp_day);
            final RadioGroup rb_type_group = (RadioGroup) view.findViewById(R.id.rb_type_group);
            final RadioButton rb_NTypeView = (RadioButton) view.findViewById(R.id.rb_NTypeView);
            final RadioButton rb_NTypeHotel = (RadioButton) view.findViewById(R.id.rb_NTypeHotel);
            final RadioButton rb_NTypeOther = (RadioButton) view.findViewById(R.id.rb_NTypeEat);
            final String[] Type = {""};
            final String[] nodeType = {C_Dictionary.SPOT_TYPE_VIEW};
            rb_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId ==  R.id.rb_NTypeView){
                        nodeType[0] = C_Dictionary.SPOT_TYPE_VIEW;
                        //Toast.makeText(getActivity(),"View",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: nodeType[0] : "+ nodeType[0]);
                    }else if(checkedId == R.id.rb_NTypeHotel){
                        nodeType[0] = C_Dictionary.SPOT_TYPE_HOTEL;
                        //Toast.makeText(getActivity(),"Hotel",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: nodeType[0] : "+ nodeType[0]);
                    }else if (checkedId ==  R.id.rb_NTypeEat){
                        nodeType[0] =C_Dictionary.SPOT_TYPE_EAT;
                        //Toast.makeText(getActivity(),"Other",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: nodeType[0] : "+ nodeType[0]);
                    }
                    Type[0] = nodeType[0];
                }
            });// RadioGroup
            Log.i(TAG, "onClick: nodeType[0] : "+ nodeType[0]);
            cursor = sqlDB.rawQuery("select "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+C_Dictionary.VALUE_COMMA_SEP
                    +C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_COMMA_SEP
                    +C_Dictionary.TABLE_SCHEMA_DATE_END
                    +" from "+ C_Dictionary.TRAVEL_LIST_Table_Name
                    +" WHERE "+C_Dictionary.USER_U_ID+"=?",new String[]{sh.getString(C_Dictionary.USER_U_ID,"")});

            final int[] days = new int[cursor.getCount()];
            String[] pName = new String[cursor.getCount()];
            final String[] inPname = {""};
            if(cursor.getCount()>0) {
                pName = new String[cursor.getCount()];
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                for (int i=1;i<=cursor.getCount();i++){
                    cursor.moveToNext();

                    Date startdate  = null;
                    try {
                        startdate = df.parse(cursor.getString(1));
                        Date enddate =  df.parse(cursor.getString(2));
                        days[i-1]= (int)(Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24))+1 ;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    pName[i-1] = cursor.getString(0);
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("尚未建立行程表，請前往會員中心建立");
                builder.setPositiveButton("確定",null).create().show();
                return;
//                pName = new String[] {"------"};
            }
            ArrayAdapter<String> planlist = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, pName);
            final int[] getDay = {1};
            sp_plan.setAdapter(planlist);
            sp_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String getItemName = parent.getItemAtPosition(position).toString();
                    inPname[0] = getItemName;
                    String[] pDay = new String[days[position]];
                    Log.i(TAG, "onItemSelected: days.length:"+days.length);
                    for (int i=0 ; i<days[position] ; i++) {
                        pDay[i] = "第 " + (i+1) + " 天的行程";
                        Log.i(TAG, "onItemSelected: pDay : "+ pDay[i] );
                    }
                    ArrayAdapter<String> daylist = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,pDay);
                    sp_day.setAdapter(daylist);
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });

            sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   int getItemName = position+1;
                    getDay[0] = getItemName;
                    //Toast.makeText(getActivity(),"getDay[0] :" + getDay[0],Toast.LENGTH_SHORT).show();
                    }
                @Override    public void onNothingSelected(AdapterView<?> parent) { }

            });
            final String Nodename = edtxt_NodeName.getText().toString();
            final String NodeDescribe = edtxt_NodeDescribe.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);
            builder.setTitle("請描述景點");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     C_MySQLite sqlhelper = new C_MySQLite(getActivity());
                     SQLiteDatabase sqlDB = sqlhelper.getReadableDatabase();
                    Cursor cursor = sqlDB.rawQuery("select count("+C_Dictionary.TABLE_SCHEMA_DATE+") from "+ C_Dictionary.CREATE_TABLE_HEADER+inPname[0]
                            +" WHERE "+C_Dictionary.TABLE_SCHEMA_DATE + "=?" + " group by "+C_Dictionary.TABLE_SCHEMA_DATE ,new String[]{ String.valueOf(getDay[0])});
                    ContentValues cv = new ContentValues();

                    if(cursor.getCount()>0) {
                        cursor.moveToFirst();
                        cv.put(C_Dictionary.TABLE_SCHEMA_DATE, getDay[0]);
                        cv.put(C_Dictionary.TABLE_SCHEMA_QUEUE, cursor.getInt(0)+1);
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, edtxt_NodeName.getText().toString());
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE, addressList.get(0).getLatitude());
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE, addressList.get(0).getLongitude());
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE, edtxt_NodeDescribe.getText().toString());
                        cv.put(C_Dictionary.SPOT_TYPE, nodeType[0]);
                    }else{
                        cv.put(C_Dictionary.TABLE_SCHEMA_DATE, getDay[0]);
                        cv.put(C_Dictionary.TABLE_SCHEMA_QUEUE, 1);
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, edtxt_NodeName.getText().toString());
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE, addressList.get(0).getLatitude());
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE, addressList.get(0).getLongitude());
                        cv.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE, edtxt_NodeDescribe.getText().toString());
                        cv.put(C_Dictionary.SPOT_TYPE, nodeType[0]);
                    }
                    sqlDB.insert(C_Dictionary.CREATE_TABLE_HEADER+inPname[0],null,cv);
                    //Toast.makeText(getActivity(),Nodename +" : "+ NodeDescribe+" : "+ nodeType[0],Toast.LENGTH_LONG).show();
                }
            }).create().show();  // builder
        }  // onClick
    }; // btn click




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Places.initialize(getActivity(), String.valueOf(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(getActivity());

        inflatedView_Gmap = inflater.inflate(R.layout.frg_gmap,container,false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
//        mapFragment.getMapAsync(this);
        InitialComponent();
        if (sf.getInt(C_Dictionary.USER_STATUS,0)==0){
            btn_myPlan.setEnabled(false);
            btn_myShowDay.setEnabled(false);
        }
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
        search_bar = inflatedView_Gmap.findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(search_bar_query);
        btn_addPrivateNode = inflatedView_Gmap.findViewById(R.id.btn_addPrivateNode);
        btn_addPrivateNode.setOnClickListener(btn_addPrivateNode_click);
        btn_web = inflatedView_Gmap.findViewById(R.id.btn_web);
        btn_web.setOnClickListener(btn_web_click);
        if(addressList.size()==0)
            btn_addPrivateNode.setEnabled(false);
        sf = getActivity().getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
    }
    SharedPreferences sf;
    Button btn_myPlan, btn_myShowDay, btn_addPrivateNode, btn_web;
    EditText edTxt_Spot, edTxt_EndSpot;
    TextView txt_getAttraction;
    SearchView search_bar;
}
