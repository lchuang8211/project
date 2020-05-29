package com.example.appiii.ui.Travel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.C_Member_SQLite;
import com.example.appiii.ui.Search.C_SearchRecycleViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FrgTravel extends Fragment {
    View inflatedView;
    private ArrayList<String> plan_Name_List = new ArrayList<>();
    private ArrayList<String> Plan_Date_List = new ArrayList<>();
    private ArrayList<Integer> Plan_MaxDay_List = new ArrayList<>();
    private View.OnClickListener btn_addPlan_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActAddTravelPlan.class);
            startActivityForResult(intent, C_Dictionary.TRAVEL_ADD_PLAN_REQUEST_CODE);
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: TRAVEL_ADD_PLAN_REQUEST_CODE 1" );

        switch(requestCode){
            case C_Dictionary.TRAVEL_ADD_PLAN_REQUEST_CODE:                //sqlite
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
                break;
            case C_Dictionary.TRAVEL_DEL_PLAN_REQUEST_CODE:

                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_travel,container,false);
        InitialComponent();
        InitRecyclerView();
        return inflatedView;
    }

    private void InitialComponent() {

        btn_addPlan = inflatedView.findViewById(R.id.btn_addPlan);
        btn_addPlan.setOnClickListener(btn_addPlan_click);

        values = new ContentValues();  // insert 用
        SQLite_helper = new C_Member_SQLite(getContext());  // helper
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
    }
    Cursor cursor;
    ContentValues values;
    C_Member_SQLite SQLite_helper;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton fab;
    Button btn_addPlan;

    C_TravelRecycleViewAdapter adapter;
    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i(TAG, "InitRecyclerView: init recyclerview");
        if (plan_Name_List.size()>0 || Plan_Date_List.size()>0 || Plan_MaxDay_List.size()>0 ){   // 如果有上一筆資料 即刪除
            plan_Name_List.clear();
            Plan_Date_List.clear();
            Plan_MaxDay_List.clear();
        }
        cursor = sqLiteDatabase.rawQuery("select "
                +C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+C_Dictionary.VALUE_COMMA_SEP
                +C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_COMMA_SEP
                +C_Dictionary.TABLE_SCHEMA_DATE_END
                +" from " + C_Dictionary.TRAVEL_LIST_Table_Name,null);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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
        RecyclerView recyclerView = inflatedView.findViewById(R.id.recycle_view_travel);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_TravelRecycleViewAdapter(getActivity(), plan_Name_List, Plan_Date_List, Plan_MaxDay_List);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
//        recyclerView.setOnItemClickListener();
    }
}
