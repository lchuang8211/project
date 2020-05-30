package com.example.appiii.ui.Member;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgmMember extends Fragment {
    View inflatedView_Member;
    Bundle budle;

    private View.OnClickListener btn_mySetting_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("設定");
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.member_setting, null);
            final TextView txt_setting = (TextView) view.findViewById(R.id.txt_setting);
            final RadioButton rdb_notAgreeAutoSignIn = (RadioButton)view.findViewById(R.id.rdb_notAgreeAutoSignIn);
            final RadioButton rdb_AgreeAutoSignIn = (RadioButton)view.findViewById(R.id.rdb_AgreeAutoSignIn);
            final RadioGroup radioGroup_SignIn  = (RadioGroup)view.findViewById(R.id.radioGroup_SignIn);
            radioGroup_SignIn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(radioGroup_SignIn.getCheckedRadioButtonId()){
                        case R.id.rdb_notAgreeAutoSignIn:
//                            txt_setting.setText("notAutoSignIn");
                            break;
                        case R.id.rdb_AgreeAutoSignIn:
//                            txt_setting.setText("AutoSignIn");
                            break;
                    }
                }
            });
            builder.setView(view);
            builder.setPositiveButton("確定",null).create().show();
        }
    };
    private View.OnClickListener btn_mySchedule_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ActMySchedule.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener btn_myCollect_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ActMyCollect.class);
            startActivity(intent);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Member = inflater.inflate(R.layout.frg_member,container,false);
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
        myHeadShot = inflatedView_Member.findViewById(R.id.myHeadShot);
        edtxt_createTableName = inflatedView_Member.findViewById(R.id.edtxt_createTableName);
        btn_mySetting = inflatedView_Member.findViewById(R.id.btn_mySetting);
        btn_mySetting.setOnClickListener(btn_mySetting_click);
        btn_mySchedule = inflatedView_Member.findViewById(R.id.btn_mySchedule);
        btn_mySchedule.setOnClickListener(btn_mySchedule_click);
        btn_myCollect = inflatedView_Member.findViewById(R.id.btn_myCollect);
        btn_myCollect.setOnClickListener(btn_myCollect_click);
    }

    CircleImageView myHeadShot;
    ContentValues values;
    C_MySQLite SQLite_helper;  // helper
    SQLiteDatabase sqLiteDatabase, sqLiteDatabase_write;
    TextView memberStatus;
    EditText edtxt_createTableName;
    Button btn_mySetting;
    Button btn_mySchedule;
    Button btn_myCollect;




}
