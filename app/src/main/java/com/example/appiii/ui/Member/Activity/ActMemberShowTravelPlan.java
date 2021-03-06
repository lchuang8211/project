package com.example.appiii.ui.Member.Activity;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_MySQLite;
import com.example.appiii.C_PlanInfo;
import com.example.appiii.R;
import com.example.appiii.ui.Member.Adapter.C_MemberShowPlanRecycleViewAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ActMemberShowTravelPlan extends AppCompatActivity {
    //// 顯示單一行程表的詳細內容
    private ArrayList<C_PlanInfo> planInfos = new ArrayList<>();
    private ArrayList<Button> Btn_changDate_list = new ArrayList<>();
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
            Log.i("ActMemberShowTravelPlan","我要的 "+ButtonID +" = days");
            Log.i("ActMemberShowTravelPlan","我要的 "+bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME));
            Intent intent = new Intent(ActMemberShowTravelPlan.this, ActAddSpotInShowTravelPlan.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,200);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if( planInfos.size()>0 ){
                planInfos.clear();
            }
            // 判斷當天有沒有行程
            cursor = sqLiteDatabase.rawQuery("select exists ( select 1 from "+table_planname+" where COLUMN_NAME_DATE = '"+ButtonID+"') ",null)  ;
            cursor.moveToLast();
            int empty = cursor.getInt(0);
            if (empty==0){
                InitRecyclerView();
                Toast.makeText(ActMemberShowTravelPlan.this,"這天尚未加入任何景點",Toast.LENGTH_SHORT).show();
            }
            if(empty==1){
                cursor = sqLiteDatabase.rawQuery("select * from "+table_planname+" where "+C_Dictionary.TABLE_SCHEMA_DATE+" ='"+ ButtonID + "' order by "+ C_Dictionary.TABLE_SCHEMA_QUEUE +" asc" ,null);
                while(cursor.moveToNext()){
                    planInfos.add(new C_PlanInfo( cursor.getInt(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_DATE)),
                            cursor.getInt(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_QUEUE)),
                            cursor.getString(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_NAME)),
                            cursor.getDouble(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE)),
                            cursor.getString(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE)),
                            cursor.getString(cursor.getColumnIndex(C_Dictionary.SPOT_TYPE))
                    ));
                }
                InitRecyclerView();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_member_showtravelplan);
        InitialComponent();
        showPlan();
        InitRecyclerView();
    }

    private void showPlan() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///// show day button /////


        SQLite_helper = new C_MySQLite(ActMemberShowTravelPlan.this);
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
                    if( planInfos.size()>0){
                        planInfos.clear();
                    }
                        cursor = sqLiteDatabase.rawQuery("select exists ( select 1 from "+table_planname+" where COLUMN_NAME_DATE = '"+getbuttonID+"') ",null)  ;
                        cursor.moveToLast();
                        int empty = cursor.getInt(0);
                        if (empty==0){
                            InitRecyclerView();
                            Toast.makeText(ActMemberShowTravelPlan.this,"這天尚未加入任何景點",Toast.LENGTH_SHORT).show();
                            ButtonID = getbuttonID;
                        }
                        if(empty==1){
                            cursor = sqLiteDatabase.rawQuery("select * from "+table_planname+" where "+C_Dictionary.TABLE_SCHEMA_DATE+" ='"+ getbuttonID + "' order by "+ C_Dictionary.TABLE_SCHEMA_QUEUE +" asc" ,null);
                                while(cursor.moveToNext()){
                                    planInfos.add(new C_PlanInfo( cursor.getInt(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_DATE)),
                                            cursor.getInt(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_QUEUE)),
                                            cursor.getString(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_NAME)),
                                            cursor.getDouble(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE)),
                                            cursor.getDouble(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE)),
                                            cursor.getString(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE)),
                                            cursor.getString(cursor.getColumnIndex(C_Dictionary.SPOT_TYPE))
                                    ));
                            }
                            ButtonID = getbuttonID;
                            InitRecyclerView();
                        }
//                        Toast.makeText(ActMemberShowTravelPlan.this, Btn_changDate_list.get(getbuttonID).getText().toString(),Toast.LENGTH_SHORT).show();
//                    }
                    txt_showday.setText("第 "+ButtonID+" 天");
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
        SQLite_helper = new C_MySQLite(this);  // helper
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout_showplan);
        txt_showday = findViewById(R.id.txt_showday);
        btn_add_spot = findViewById(R.id.btn_add_spot);
        btn_add_spot.setOnClickListener(btn_add_spot_click);

        raw_planname = bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME);
        Log.i("shoePlan","raw_planname : " +raw_planname);
        table_planname = "["+C_Dictionary.CREATE_TABLE_HEADER + bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME)+"]";

        if(bundleFromCreate.getBoolean(C_Dictionary.TRAVEL_PLAN_IS_EMPTY)) {  //有行程就秀行程
            Log.i("TAG", "in shoePlan: null");
            Toast.makeText(ActMemberShowTravelPlan.this,"尚未加入任何景點",Toast.LENGTH_SHORT).show();
        }else{
            cursor = sqLiteDatabase.rawQuery("select * from " + table_planname+" where "+C_Dictionary.TABLE_SCHEMA_DATE+" ='"+ 1 + "' order by "+ C_Dictionary.TABLE_SCHEMA_QUEUE +" asc" ,null);
            while(cursor.moveToNext()){
                planInfos.add(new C_PlanInfo( cursor.getInt(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_DATE)),
                        cursor.getInt(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_QUEUE)),
                        cursor.getString(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndex(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE)),
                        cursor.getString(cursor.getColumnIndex(C_Dictionary.SPOT_TYPE))
                ));
            }
            ButtonID=1;  //只秀第一天
            InitRecyclerView();
        }

    }
    LinearLayout linearLayout;
    TextView txt_showday;
    Cursor cursor;
    ContentValues values;
    Button btn_dayth;
    Button btn_add_spot;

//    TextView txt_showplan;
    C_MySQLite SQLite_helper;
    SQLiteDatabase sqLiteDatabase;
    Bundle bundleFromCreate;

    C_MemberShowPlanRecycleViewAdapter adapter;

    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i("TAG", "InitRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.mamber_actshowtravelplan_planInfo_RecyclerView);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_MemberShowPlanRecycleViewAdapter(this, planInfos ,bundleFromCreate.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME), ButtonID);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
//        recyclerView.setOnItemClickListener();
    }


}
