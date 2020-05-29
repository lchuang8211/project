package com.example.appiii.ui.Travel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.C_Member_SQLite;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ActShowTravelPlan extends AppCompatActivity {
    private ArrayList<Button> Btn_changDate_list = new ArrayList<>();
    private ArrayList<Integer> showSpotDate = new ArrayList<>();
    private ArrayList<Integer> showSpotQueue = new ArrayList<>();
    private ArrayList<String> showSpotName = new ArrayList<>();
    private ArrayList<Double> showSpotLatitude = new ArrayList<>();
    private ArrayList<Double> showSpotLongitude = new ArrayList<>();
    private ArrayList<String> showSpotDescribe = new ArrayList<>();
    static int ButtonID = 0;
    String table_planname;
    String raw_planname;

    private View.OnClickListener btn_add_spot_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(ButtonID==0) ButtonID=1;
            Bundle bundle = new Bundle();
            bundle.putInt("DAYS",ButtonID);
            bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME));
            Log.i("ActShowTravelPlan","我要的 "+ButtonID +" = days");
            Log.i("ActShowTravelPlan","我要的 "+bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME));
            Intent intent = new Intent(ActShowTravelPlan.this, Act_addSpotInShowTravelPlan.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,200);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if( showSpotDate.size()>0 || showSpotQueue.size()>0 || showSpotName.size()>0 || showSpotLatitude.size()>0 || showSpotLongitude.size()>0 || showSpotDescribe.size()>0){
                showSpotDate.clear(); showSpotQueue.clear(); showSpotName.clear(); showSpotLatitude.clear(); showSpotLongitude.clear(); showSpotDescribe.clear();
            }
            // 判斷當天有沒有行程
            cursor = sqLiteDatabase.rawQuery("select exists ( select 1 from "+table_planname+" where COLUMN_NAME_DATE = '"+ButtonID+"') ",null)  ;
            cursor.moveToLast();
            int empty = cursor.getInt(0);
            if (empty==0){
                InitRecyclerView();
                Toast.makeText(ActShowTravelPlan.this,"This Day 尚未加入任何景點",Toast.LENGTH_SHORT).show();
            }
            if(empty==1){
                cursor = sqLiteDatabase.rawQuery("select * from "+table_planname+" where "+C_Dictionary.TABLE_SCHEMA_DATE+" ='"+ ButtonID + "' order by "+ C_Dictionary.TABLE_SCHEMA_QUEUE +" asc" ,null);
                while(cursor.moveToNext()){
                    showSpotDate.add(cursor.getInt(0));
                    Log.i("while","showPlanDate"+cursor.getInt(0));
                    showSpotQueue.add(cursor.getInt(1));
                    Log.i("while","showPlanQueue"+cursor.getInt(1));
                    showSpotName.add(cursor.getString(2));
                    Log.i("while","showPlanName"+cursor.getString(2));
                    showSpotLatitude.add(cursor.getDouble(3));
                    Log.i("while","showPlanLatitude"+cursor.getDouble(3));
                    showSpotLongitude.add(cursor.getDouble(4));
                    Log.i("while","showPlanLongitude"+cursor.getDouble(4));
                    showSpotDescribe.add(cursor.getString(5));
                    Log.i("while","showPlanLongitude"+cursor.getString(5));
                }
                InitRecyclerView();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_travelshowplan);
        InitialComponent();
        showPlan();
        InitRecyclerView();
    }

    private void showPlan() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///// show day button /////


        SQLite_helper = new C_Member_SQLite(ActShowTravelPlan.this);
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
        String sql = "select "
                +C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_COMMA_SEP
                +C_Dictionary.TABLE_SCHEMA_DATE_END
                +" from "+C_Dictionary.TRAVEL_LIST_Table_Name +" where "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+" = '"+raw_planname+"'";
        Log.i("shoePlan","sql : " + sql);
        cursor = sqLiteDatabase.rawQuery("select "
                        +C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_COMMA_SEP
                        +C_Dictionary.TABLE_SCHEMA_DATE_END
                        +" from "+C_Dictionary.TRAVEL_LIST_Table_Name +" where "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+" = '"+raw_planname+"'"
                ,null);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int getDays = 0;
        try {
            cursor.moveToLast();
            Date startdate = df.parse(cursor.getString(0));
            Date enddate =  df.parse(cursor.getString(1));
            getDays = ( (int)(Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24))+1 );
            Log.i("TAG", "in shoePlan: getDays "+getDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(int i=0 ; i<getDays ; i++){
            btn_dayth = new Button(this );
            btn_dayth.setLayoutParams(new LinearLayout.LayoutParams(200,150));
            btn_dayth.setId(i+1);
            btn_dayth.setText("第 "+ (i+1) +" 天");
            Btn_changDate_list.add(btn_dayth);
            linearLayout.addView(btn_dayth);
        }
        int i=1;
        for (Button btn : Btn_changDate_list ) {
            final int getbuttonID = i;
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if( showSpotDate.size()>0 || showSpotQueue.size()>0 || showSpotName.size()>0 || showSpotLatitude.size()>0 || showSpotLongitude.size()>0 || showSpotDescribe.size()>0 ){
                        showSpotDate.clear(); showSpotQueue.clear(); showSpotName.clear(); showSpotLatitude.clear(); showSpotLongitude.clear(); showSpotDescribe.clear();
                    }
                        cursor = sqLiteDatabase.rawQuery("select exists ( select 1 from "+table_planname+" where COLUMN_NAME_DATE = '"+getbuttonID+"') ",null)  ;
                        cursor.moveToLast();
                        int empty = cursor.getInt(0);
                        if (empty==0){
                            InitRecyclerView();
                            Toast.makeText(ActShowTravelPlan.this,"This Day 尚未加入任何景點",Toast.LENGTH_SHORT).show();
                            ButtonID = getbuttonID;
                        }
                        if(empty==1){
                            cursor = sqLiteDatabase.rawQuery("select * from "+table_planname+" where "+C_Dictionary.TABLE_SCHEMA_DATE+" ='"+ getbuttonID + "' order by "+ C_Dictionary.TABLE_SCHEMA_QUEUE +" asc" ,null);
                                while(cursor.moveToNext()){
                                    showSpotDate.add(cursor.getInt(0));
                                    Log.i("while","showPlanDate"+cursor.getInt(0));
                                    showSpotQueue.add(cursor.getInt(1));
                                    Log.i("while","showPlanQueue"+cursor.getInt(0));
                                    showSpotName.add(cursor.getString(2));
                                    Log.i("while","showPlanName"+cursor.getString(0));
                                    showSpotLatitude.add(cursor.getDouble(3));
                                    Log.i("while","showPlanLatitude"+cursor.getDouble(0));
                                    showSpotLongitude.add(cursor.getDouble(4));
                                    Log.i("while","showPlanLongitude"+cursor.getDouble(0));
                                    showSpotDescribe.add(cursor.getString(5));
                                    Log.i("while","showPlanLongitude"+cursor.getString(5));
                            }
                            ButtonID = getbuttonID;
                            InitRecyclerView();
                        }
//                        Toast.makeText(ActShowTravelPlan.this, Btn_changDate_list.get(getbuttonID).getText().toString(),Toast.LENGTH_SHORT).show();
//                    }
                }
            });
            i++;
        }
        ///// show day button /////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    private void InitialComponent() {
        bundleFromCreate = getIntent().getExtras();
        setTitle(bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME));
//        txt_showplan = findViewById(R.id.txt_showplan);
        values = new ContentValues();  // insert 用
        SQLite_helper = new C_Member_SQLite(this);  // helper
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout_showplan);
        btn_add_spot = findViewById(R.id.btn_add_spot);
        btn_add_spot.setOnClickListener(btn_add_spot_click);
        raw_planname = bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME);
        Log.i("shoePlan","raw_planname : " +raw_planname);
        table_planname = "["+C_Dictionary.CREATE_TABLE_HEADER + bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME)+"]";

        if(bundleFromCreate.getBoolean(C_Dictionary.TRAVEL_PLAN_IS_EMPTY)) {  //有行程就秀行程
            Log.i("TAG", "in shoePlan: null");
            Toast.makeText(ActShowTravelPlan.this,"尚未加入任何景點",Toast.LENGTH_SHORT).show();
        }else{
            cursor = sqLiteDatabase.rawQuery("select * from " + table_planname+" where "+C_Dictionary.TABLE_SCHEMA_DATE+" ='"+ 1 + "' order by "+ C_Dictionary.TABLE_SCHEMA_QUEUE +" asc" ,null);
            while(cursor.moveToNext()){
                showSpotDate.add(cursor.getInt(0));
                Log.i("while","showPlanDate"+cursor.getInt(0));
                showSpotQueue.add(cursor.getInt(1));
                Log.i("while","showPlanQueue"+cursor.getInt(0));
                showSpotName.add(cursor.getString(2));
                Log.i("while","showPlanName"+cursor.getString(0));
                showSpotLatitude.add(cursor.getDouble(3));
                Log.i("while","showPlanLatitude"+cursor.getDouble(0));
                showSpotLongitude.add(cursor.getDouble(4));
                Log.i("while","showPlanLongitude"+cursor.getDouble(0));
                showSpotDescribe.add(cursor.getString(5));
                Log.i("while","showPlanLongitude"+cursor.getString(5));
            }
            ButtonID=1;  //只秀第一天
            InitRecyclerView();
        }

    }
    LinearLayout linearLayout;

    Cursor cursor;
    ContentValues values;
    Button btn_dayth;
    Button btn_add_spot;

//    TextView txt_showplan;
    C_Member_SQLite SQLite_helper;
    SQLiteDatabase sqLiteDatabase;
    Bundle bundleFromCreate;

    C_TravelShowPlanRecycleViewAdapter adapter;

    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i("TAG", "InitRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.travel_planInfo_RecyclerView);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_TravelShowPlanRecycleViewAdapter(this, showSpotDate, showSpotQueue, showSpotName, showSpotLatitude, showSpotLongitude, showSpotDescribe, bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME), ButtonID);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
//        recyclerView.setOnItemClickListener();
    }


}
