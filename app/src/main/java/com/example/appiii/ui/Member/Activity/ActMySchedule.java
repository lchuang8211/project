package com.example.appiii.ui.Member.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_MySQLite;
import com.example.appiii.R;
import com.example.appiii.ui.Member.Adaoter.C_MemberRecycleViewAdapter;
import com.example.appiii.ui.Member.AsyncTask.C_AsyncLoadPlanList;
import com.example.appiii.ui.Member.C_LoadPlanList;
import com.example.appiii.ui.Member.AsyncTask.Interface_AsyncLoadPlanList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActMySchedule extends AppCompatActivity {

    View inflatedView;
    private ArrayList<String> plan_Name_List = new ArrayList<>();
    private ArrayList<String> Plan_Date_List = new ArrayList<>();
    private ArrayList<Integer> Plan_MaxDay_List = new ArrayList<>();

    private View.OnClickListener btn_addMyPlan_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActMySchedule.this, ActAddTravelPlan.class);
            startActivityForResult(intent, C_Dictionary.TRAVEL_ADD_PLAN_REQUEST_CODE);
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: TRAVEL_ADD_PLAN_REQUEST_CODE 1" );

        switch(requestCode){
            case C_Dictionary.TRAVEL_ADD_PLAN_REQUEST_CODE :                //sqlite
                if (resultCode==RESULT_OK){
                    Log.i(TAG, "onActivityResult: in switch");
                    cursor = sqLiteDatabase.rawQuery("select "
                            +C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+C_Dictionary.VALUE_COMMA_SEP
                            +C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_COMMA_SEP
                            +C_Dictionary.TABLE_SCHEMA_DATE_END
                            +" from " + C_Dictionary.TRAVEL_LIST_Table_Name,null);
                    while (cursor.moveToNext()){
                        String planName = cursor.getString(0);
                        Log.i(TAG, "onActivityResult: planName:"+planName );
                        String dateTime = cursor.getString(1)+" ~ "+cursor.getString(2);
                        Log.i(TAG, "onActivityResult: dateTime:"+dateTime );
                        plan_Name_List.add(planName);
                        Plan_Date_List.add(dateTime);
                    }
                    Log.i(TAG, "onActivityResult: TRAVEL_ADD_PLAN_REQUEST_CODE ok" );
                    InitRecyclerView();                
                    }
                break;
            case C_Dictionary.TRAVEL_DEL_PLAN_REQUEST_CODE:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_schedule);
        setTitle("行程表");
//        InsertRecyclerView();
        InitRecyclerView();
        InitialComponent();
    }

    private void InitialComponent() {
        values = new ContentValues();  // insert 用
        SQLite_helper = new C_MySQLite(this);  // helper
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
        btn_addMyPlan = findViewById(R.id.btn_addMyPlan);
        btn_addMyPlan.setOnClickListener(btn_addMyPlan_click);
        sh = getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
        userbundle = new Bundle();
        userbundle.putString(C_Dictionary.USER_U_ID, sh.getString(C_Dictionary.USER_U_ID,"visitor"));
        ///////
        // load database
        new C_AsyncLoadPlanList(ActMySchedule.this,new Interface_AsyncLoadPlanList() {
            @Override
            public void LoadPlanListFinish(ArrayList<C_LoadPlanList> C_LoadPlanList) {
                int len = C_LoadPlanList.size();
                Log.i(TAG, "LoadPlanListFinish: len : "+len);
                newRecyclerView();
            }
        }).execute(userbundle);
//        if (plan_Name_List.size()>0 || Plan_Date_List.size()>0 || Plan_MaxDay_List.size()>0 ){   // 如果有上一筆資料 即刪除
//            plan_Name_List.clear();
//            Plan_Date_List.clear();
//            Plan_MaxDay_List.clear();
//        }
        ///////
    }

    private void newRecyclerView() {
        recyclerView = findViewById(R.id.recycle_view_member);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_MemberRecycleViewAdapter(this, plan_Name_List, Plan_Date_List, Plan_MaxDay_List);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    SharedPreferences sh;
    Bundle userbundle;
    Button btn_addMyPlan;
    Cursor cursor;
    ContentValues values;
    C_MySQLite SQLite_helper;
    SQLiteDatabase sqLiteDatabase;
    C_MemberRecycleViewAdapter adapter;
    RecyclerView recyclerView;


    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i(TAG, "InitRecyclerView: init recyclerview");
        if (plan_Name_List.size()>0 || Plan_Date_List.size()>0 || Plan_MaxDay_List.size()>0 ){   // 如果有上一筆資料 即刪除
            plan_Name_List.clear();
            Plan_Date_List.clear();
            Plan_MaxDay_List.clear();
        }
        SharedPreferences sh = getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
        SQLite_helper = new C_MySQLite(this);  // helper
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("select "
                + C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+C_Dictionary.VALUE_COMMA_SEP
                +C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_COMMA_SEP
                +C_Dictionary.TABLE_SCHEMA_DATE_END
                +" from " + C_Dictionary.TRAVEL_LIST_Table_Name
                +" where "+C_Dictionary.USER_U_ID+"='"+sh.getString(C_Dictionary.USER_U_ID,"visitor")+"'",null);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    if (cursor.getCount()>0){
        while (cursor.moveToNext()){
            String planName = cursor.getString(0);
            Log.i(TAG, "onActivityResult: planName:"+planName );
            String dateTime = cursor.getString(1)+" ~ "+cursor.getString(2);
            Log.i(TAG, "onActivityResult: dateTime:"+dateTime );
            plan_Name_List.add(planName);
            Plan_Date_List.add(dateTime);
            try {
                Date startdate  = df.parse(cursor.getString(1));
                Date enddate =  df.parse(cursor.getString(2));
                Plan_MaxDay_List.add( (int)(Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24))+1 );
                Log.i("Plan_MaxDay_List","Plan_MaxDay_List : "+(int)(Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
        recyclerView = findViewById(R.id.recycle_view_member);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_MemberRecycleViewAdapter(this, plan_Name_List, Plan_Date_List, Plan_MaxDay_List);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
    }


}
