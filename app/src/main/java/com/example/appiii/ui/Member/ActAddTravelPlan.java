package com.example.appiii.ui.Member;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import android.icu.util.Calendar;
import android.widget.Toast;
//import java.util.Calendar;
import java.text.ParseException;
import java.util.Date;

public class ActAddTravelPlan extends AppCompatActivity {
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int start_year=0, start_month=0, start_day=0;
    int end_year=0, end_month=0, end_day=0;
    String str_startdate;
    String str_enddate;
    Date startdate;
    Date enddate;
    private View.OnClickListener txt_startDate_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(ActAddTravelPlan.this,
                    new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txt_startDate.setText( "起始:\n"+ year +"/"+ (month+1) +"/"+ dayOfMonth );
                            start_year = year;
                            start_month = month+1;
                            start_day = dayOfMonth;
                        }
                    },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    };


    private View.OnClickListener txt_endDate_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            calendar = Calendar.getInstance();
//            int current_year = calendar.get(Calendar.YEAR);
//            int current_month = calendar.get(Calendar.MONTH);
//            int current_day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog2 = new DatePickerDialog(ActAddTravelPlan.this,
                new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txt_endDate.setText( "結束:\n"+year +"/"+ (month+1) +"/"+ dayOfMonth);
                    end_year = year;
                    end_month = month+1;
                    end_day = dayOfMonth;
                }
            }, start_year, start_month-1, start_day);  // 起始 日期
            datePickerDialog2.show();



        }
    };
    private View.OnClickListener btn_Createe_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if ( start_year==0 || start_month==0 || start_day==0 || end_year==0 || end_month==0 || end_day==0 ) {
                Toast.makeText(ActAddTravelPlan.this,"請輸入時間",Toast.LENGTH_LONG).show();
                return;
            }
            if (edtxt_PlanName.getText().toString().matches("")) {
                Toast.makeText(ActAddTravelPlan.this,"請輸入計畫名稱",Toast.LENGTH_LONG).show();
                return;
            }

                try {
                    str_startdate = start_year+"-"+start_month+"-"+start_day;
                    str_enddate = end_year+"-"+end_month+"-"+end_day;
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    startdate  = df.parse(str_startdate);
                    enddate =  df.parse(str_enddate);
                    if (enddate.getTime() < startdate.getTime()){
                        Toast.makeText(ActAddTravelPlan.this,"日期輸入不正確",Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24) 天數
                Log.i("Date","txt_Day:"+ Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            String rawTableName = edtxt_PlanName.getText().toString().trim();
            String TableName = "plan_"+ rawTableName;
            SQLite_helper = new C_MySQLite(ActAddTravelPlan.this);
            sqLiteDatabase = SQLite_helper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_LIST_Table_Name+" where "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+"=?",new String[]{rawTableName});
            if (cursor.getCount()>0){
                Log.i("cursor","TRAVEL_LIST_SCHEMA_PLAN_NAME : 表單已存在" );
                Toast.makeText(ActAddTravelPlan.this,edtxt_PlanName.getText().toString().trim()+" 已存在 ",Toast.LENGTH_LONG).show();
                return;
            }else{
                Log.i("cursor","TRAVEL_LIST_SCHEMA_PLAN_NAME : 表單沒有重複" );

                // select COLUMN_NAME_DATE  from 耶呼 group by COLUMN_NAME_DATE order by COLUMN_NAME_DATE desc LIMIT 1  // 找最後的天數
                // select count(COLUMN_NAME_DATE)  from 耶呼 Where COLUMN_NAME_DATE = 2 group by COLUMN_NAME_DATE // 找當天的最後一個行程
                ContentValues contentValues = new ContentValues();
                contentValues.put(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME,rawTableName);
                contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE_START,str_startdate);
                contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE_END,str_enddate);
                contentValues.put(C_Dictionary.TRAVEL_SCHEMA_TABLE_VISIBILITY,0);
                sqLiteDatabase.insert(C_Dictionary.TRAVEL_LIST_Table_Name,null,contentValues);
            }
            String newPlanTable = C_Dictionary.CREATE_TABLE_if_not_exists +"["+TableName + "] ("
                    + C_Dictionary.TABLE_SCHEMA_DATE+C_Dictionary.VALUE_TYPE_INT+C_Dictionary.VALUE_COMMA_SEP
                    + C_Dictionary.TABLE_SCHEMA_QUEUE+C_Dictionary.VALUE_TYPE_INT+C_Dictionary.VALUE_COMMA_SEP
                    + C_Dictionary.TABLE_SCHEMA_NODE_NAME+C_Dictionary.VALUE_TYPE_STRING+C_Dictionary.VALUE_COMMA_SEP
                    + C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE+C_Dictionary.VALUE_TYPE_DOUBLE+C_Dictionary.VALUE_COMMA_SEP
                    + C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE+C_Dictionary.VALUE_TYPE_DOUBLE +C_Dictionary.VALUE_COMMA_SEP
                    + C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE+C_Dictionary.VALUE_TYPE_STRING +" )";

            sqLiteDatabase.execSQL( newPlanTable );
            Toast.makeText(ActAddTravelPlan.this,"建立完成 "+edtxt_PlanName.getText().toString().trim(),Toast.LENGTH_LONG).show();
            int maxplanDay = (int)Math.abs( enddate.getTime()-startdate.getTime() )/(60*60*1000*24);
            SharedPreferences sharedPreferences = getSharedPreferences(C_Dictionary.PLAN_DAYS_RECORD,0);
            SharedPreferences.Editor w = sharedPreferences.edit();
            w.putInt(edtxt_PlanName.getText().toString().trim(), maxplanDay+1 ).commit();   // 紀錄計畫有幾天
            setResult(RESULT_OK); // RESULT_OK 不回傳常數
            finish();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_member_add_travelplan_list_add);  // 建立行程表的 Activity
        setTitle("建立行程表");
        InitialComponent();
    }

    private void InitialComponent() {
        edtxt_PlanName = findViewById(R.id.edtxt_PlanName);
        txt_startDate = findViewById(R.id.txt_startDate);
        txt_startDate.setOnClickListener(txt_startDate_click);
        txt_endDate = findViewById(R.id.txt_endDate);
        txt_endDate.setOnClickListener(txt_endDate_click);
        btn_Create = findViewById(R.id.btn_Create);
        btn_Create.setOnClickListener(btn_Createe_click);
//        btn_endDate = findViewById(R.id.btn_endDate);
//        btn_endDate.setOnClickListener(btn_endDate_click);

    }
    C_MySQLite SQLite_helper;  // helper
    SQLiteDatabase sqLiteDatabase;
    EditText edtxt_PlanName;
    TextView txt_startDate;
    TextView txt_endDate;
    Button  btn_Create;

}
