package com.example.appiii.ui.Member;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import javax.security.auth.login.LoginException;

public class FrgmMember extends Fragment {
    View inflatedView_Member;
    Bundle budle;

    private View.OnClickListener btn_mySchedule_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            values = new ContentValues();
            values.put(C_Dictionary.TABLE_SCHEMA_DATE,C_Dictionary.TABLE_SCHEMA_DATE);
            values.put(C_Dictionary.TABLE_SCHEMA_QUEUE,C_Dictionary.TABLE_SCHEMA_DATE);
            values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME,C_Dictionary.TABLE_SCHEMA_DATE);
            values.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,"2.22");
            values.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,"2.1");
            Log.i("in btn SQLITE","values : " + values);
//            long newInsert = sqLiteDatabase.insert(C_Dictionary.MY_Table_Name,null,values);

            Cursor c1 = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_LIST_Table_Name,null);
            Cursor c2 = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.MY_Table_Name,null);
            Log.i("in btn", "onClick: "+ c.getCount() );
            Log.i("in btn", "onClick: c1:"+ c1.getCount() );
            Log.i("in btn", "onClick: c2:"+ c2.getCount() );
            int i =0;
//            Log.i("in btn", "onClick: First :"+ c.getPosition() );
//            while(c.moveToNext()){
//                show_sqlite.append(c.getString(0)+","+c.getString(1)+","+c.getString(2)+","+c.getString(3)+","+c.getString(4)+"\n");
//            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Member = inflater.inflate(R.layout.frg_member,container,false);
        show_sqlite = inflatedView_Member.findViewById(R.id.show_sqlite);
        SQLite_helper = new C_Member_SQLite(getContext());
        sqLiteDatabase = SQLite_helper.getReadableDatabase();
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


    }

    ContentValues values;
    C_Member_SQLite SQLite_helper;  // helper
    SQLiteDatabase sqLiteDatabase;
    TextView memberStatus;
    TextView show_sqlite;
    Button btn_mySchedule;
}
