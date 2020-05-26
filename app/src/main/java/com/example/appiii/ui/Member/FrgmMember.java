package com.example.appiii.ui.Member;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import com.example.appiii.R;

import java.util.ArrayList;

public class FrgmMember extends Fragment {
    View inflatedView_Member;
    Bundle budle;

    private View.OnClickListener btn_mySchedule_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            values = new ContentValues();
            values.put(C_Dictionary.TABLE_SCHEMA_DATE,C_Dictionary.TABLE_SCHEMA_DATE);
            values.put(C_Dictionary.TABLE_SCHEMA_QUEUE,C_Dictionary.TABLE_SCHEMA_QUEUE);
            values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME,C_Dictionary.TABLE_SCHEMA_NODE_NAME);
            values.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,"2.22");
            values.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,"2.1");
            Log.i("in btn SQLITE","values : " + values);
//            long newInsert = sqLiteDatabase.insert(C_Dictionary.MY_Table_Name,null,values);

            Cursor c1 = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_LIST_Table_Name,null);
            Cursor c2 = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_Table_Name,null);

            Log.i("in btn", "onClick: c1:"+ c1.getCount() );
            Log.i("in btn", "onClick: c2:"+ c2.getCount() );
            int i =0;
//            Log.i("in btn", "onClick: First :"+ c.getPosition() );
//            while(c.moveToNext()){
//                show_sqlite.append(c.getString(0)+","+c.getString(1)+","+c.getString(2)+","+c.getString(3)+","+c.getString(4)+"\n");
//            }

        }
    };
    private int countinputMySchedule = 0;
    private ArrayList<ContentValues> inputMySchedule = new ArrayList<>();
    private View.OnClickListener btn_myInsertSchedule_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String tableName = edtxt_createTableName.getText().toString();
            if (edtxt_createTableName.getText().toString().matches("")) {

            }
            else{

                SQLite_helper = new C_Member_SQLite(getContext());
                sqLiteDatabase = SQLite_helper.getReadableDatabase();
                // create table for new travel plan
                String create_newPlan = C_Dictionary.CREATE_TABLE_if_not_exists + tableName
                        +" ( "+ C_Dictionary.TABLE_SCHEMA_DATE+C_Dictionary.VALUE_TYPE_INT + C_Dictionary.VALUE_COMMA_SEP
                        + C_Dictionary.TABLE_SCHEMA_QUEUE+C_Dictionary.VALUE_TYPE_INT + C_Dictionary.VALUE_COMMA_SEP
                        + C_Dictionary.TABLE_SCHEMA_NODE_NAME+C_Dictionary.VALUE_TYPE_STRING + C_Dictionary.VALUE_COMMA_SEP
                        + C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE+C_Dictionary.VALUE_TYPE_DOUBLE + C_Dictionary.VALUE_COMMA_SEP
                        + C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE+C_Dictionary.VALUE_TYPE_DOUBLE
                        +" );";
                Log.i("in btn","create newplan : "+create_newPlan);
                sqLiteDatabase.execSQL( create_newPlan );
                Log.i("in btn","sqLiteDatabase create : "+sqLiteDatabase);
                ContentValues contentValues = new ContentValues();
                        //get count of table of database  DatabaseUtils.queryNumEntries(sqLiteDatabase, C_Dictionary.TRAVEL_LIST_Table_Name)
                contentValues.put(C_Dictionary.TABLE_SCHEMA_id, DatabaseUtils.queryNumEntries(sqLiteDatabase, C_Dictionary.TRAVEL_LIST_Table_Name));
                contentValues.put(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME,tableName);
                contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE_START,1);
                contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE_END,2);
                contentValues.put(C_Dictionary.TRAVEL_TABLE_VISIBILITY,3);
                sqLiteDatabase.insert(C_Dictionary.TRAVEL_LIST_Table_Name,null,contentValues);

                Cursor cursor = sqLiteDatabase.rawQuery("select * from "+ C_Dictionary.TRAVEL_LIST_Table_Name ,null);
                while (cursor.moveToNext()) {
                    show_sqlite.append(cursor.getString(0)+C_Dictionary.VALUE_COMMA_SEP
                                    +cursor.getString(1)+C_Dictionary.VALUE_COMMA_SEP
                                    +cursor.getString(2)+C_Dictionary.VALUE_COMMA_SEP
                                    +cursor.getString(3)+C_Dictionary.VALUE_COMMA_SEP+"\n"
                            );
                }
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Member = inflater.inflate(R.layout.frg_member,container,false);



//        sqLiteDatabase_write = SQLite_helper.getWritableDatabase();
        Log.i("SQLITE","sqLiteDatabase : "+sqLiteDatabase);

        budle = this.getArguments();   // 在fragment 中拿到 bundle
        InitialComponent();
        switch(budle.getString(C_Dictionary.USER_STATUS)){
            case C_Dictionary.USER_STATUS_MEMBER:
                memberStatus.setText("你已是會員");
                break;
            case C_Dictionary.USER_STATUS_VISITORS:
                memberStatus.setText("你還不是會員");
                break;

        }
        return inflatedView_Member;
    }

    private void InitialComponent() {
        memberStatus = inflatedView_Member.findViewById(R.id.memberStatus);
        btn_mySchedule = inflatedView_Member.findViewById(R.id.btn_mySchedule);
        btn_mySchedule.setOnClickListener(btn_mySchedule_click);
        btn_myInsertSchedule = inflatedView_Member.findViewById(R.id.btn_myInsertSchedule);
        btn_myInsertSchedule.setOnClickListener(btn_myInsertSchedule_click);

        show_sqlite = inflatedView_Member.findViewById(R.id.show_sqlite);
        edtxt_createTableName = inflatedView_Member.findViewById(R.id.edtxt_createTableName);
    }

    ContentValues values;
    C_Member_SQLite SQLite_helper;  // helper
    SQLiteDatabase sqLiteDatabase, sqLiteDatabase_write;
    TextView memberStatus;
    TextView show_sqlite;
    EditText edtxt_createTableName;
    Button btn_mySchedule;
    Button btn_myInsertSchedule;
}
