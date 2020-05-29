package com.example.appiii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appiii.ui.Member.C_MySQLite;

public class ActEntrance extends AppCompatActivity {
    static Boolean SignInAuto;
    static Boolean RemamberAccount;
    private View.OnClickListener btn_Visitors_Signin_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ActEntrance.this, ActBottomNav.class);
            bundle.putString(C_Dictionary.USER_STATUS,C_Dictionary.USER_STATUS_VISITORS);
            intent.putExtras(bundle);
            startActivity(intent);
            Toast toast = Toast.makeText(ActEntrance.this,"訪客登入",Toast.LENGTH_LONG);
            toast.show();

        }
    };
    private View.OnClickListener btn_Member_Signin_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (edtxt_account.getText().toString().matches("") || edtxt_passwd.getText().toString().matches("")) {
                Toast toast = Toast.makeText(ActEntrance.this, "請輸入帳號密碼", Toast.LENGTH_LONG);
                toast.show();
                return;
            }else {

                Bundle bundle = new Bundle();
                bundle.putString(C_Dictionary.USER_ACCOUNT, edtxt_account.getText().toString());
                bundle.putString(C_Dictionary.USER_PASSWORD, edtxt_passwd.getText().toString());
                bundle.putInt(C_Dictionary.USER_SIGNIN_OR_SIGNUP,1);
                new C_EntranceTask(new Interface_AsyncEntrance() {
                    @Override
                    public void memberCheckFinish(Boolean checkMember) {
                        System.out.println(checkMember);
                        if (checkMember) {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(ActEntrance.this, ActBottomNav.class);
                            bundle.putString(C_Dictionary.USER_STATUS,C_Dictionary.USER_STATUS_MEMBER);
                            intent.putExtras(bundle);
                            startActivity(intent);

                            if (RemamberAccount) {
                                writeSetting.putString(C_Dictionary.TABLE_SCHEMA_ACCOUNT,edtxt_account.getText().toString()).apply();
                                if(SignInAuto) {
                                    writeSetting.putString(C_Dictionary.TABLE_SCHEMA_PASSWORD, edtxt_passwd.getText().toString()).apply();
                                }
                            }
                            Toast toast = Toast.makeText(ActEntrance.this, "登入成功", Toast.LENGTH_LONG);
                            toast.show();
                        } else{
                            Toast toast = Toast.makeText(ActEntrance.this, "帳號密碼輸入錯誤", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }).execute(bundle);
            }
        }
    };

    private View.OnClickListener btn_SignupMember_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

//            final View view = LayoutInflater.from(ActEntrance.this).inflate(R.layout.signup_member,null);
            LayoutInflater inflater = LayoutInflater.from(ActEntrance.this);
            final View view = inflater.inflate(R.layout.signup_member, null);
            final TextView txt_signupAccount = (TextView) view.findViewById(R.id.txt_signupAccount);
            final TextView txt_signupPassword = (TextView) view.findViewById(R.id.txt_signupPassword);
            final EditText edtxt_SingupAccount = (EditText) view.findViewById(R.id.estxt_SingupAccount);
            final EditText edtxt_SingupPassword = (EditText) view.findViewById(R.id.estxt_SingupPassword);
            AlertDialog.Builder builder = new AlertDialog.Builder(ActEntrance.this);
            builder.setTitle("申請會員");
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("加入",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(edtxt_SingupAccount.getText().toString().length()<3){
                        Toast toast = Toast.makeText(ActEntrance.this, "帳號太短", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(C_Dictionary.USER_SIGNUP_ACCOUNT,edtxt_SingupAccount.getText().toString());
                    bundle.putString(C_Dictionary.USER_SIGNUP_PASSWORD,edtxt_SingupPassword.getText().toString());
                    bundle.putInt(C_Dictionary.USER_SIGNIN_OR_SIGNUP,2);
                    new C_EntranceTask(new Interface_AsyncEntrance() {
                        @Override
                        public void memberCheckFinish(Boolean checkMember) {
                            if(checkMember){
                                Toast toast = Toast.makeText(ActEntrance.this, "加入會員成功", Toast.LENGTH_SHORT);
                                toast.show();
                            }else{
                                Toast toast = Toast.makeText(ActEntrance.this, "帳號重複", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }).execute(bundle);
                }
            });

            builder.setView(view);
//            builder.show();
            Dialog message = builder.create();
            message.show();
        }
    };

    private CompoundButton.OnCheckedChangeListener cbox_rememberAccount_select = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                cbox_Autosignin.setVisibility(View.VISIBLE);
                RemamberAccount = true;
                writeSetting.putBoolean("RemamberAccountSetting",true).apply();
            }else{
                cbox_Autosignin.setVisibility(View.GONE);
                RemamberAccount = false;
                writeSetting.putBoolean("RemamberAccountSetting",false).apply();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener cbox_Autosignin_select = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                SignInAuto = true;
                RemamberAccount = true;
                writeSetting.putBoolean("AutoSignInSetting",true).apply();
            }else{
                SignInAuto = false;
                RemamberAccount = false;
                writeSetting.putBoolean("AutoSignInSetting",false).apply();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_entrance);
        InitialComponent();
    }
    private void InitialComponent() {
        btn_Visitors_Signin = findViewById(R.id.btn_Visitors_Signin);
        btn_Visitors_Signin.setOnClickListener(btn_Visitors_Signin_click);
        btn_Member_Signin = findViewById(R.id.btn_Member_Signin);
        btn_Member_Signin.setOnClickListener(btn_Member_Signin_click);
        btn_SignupMember = findViewById(R.id.btn_SignupMember);
        btn_SignupMember.setOnClickListener(btn_SignupMember_click);
        edtxt_account = findViewById(R.id.edtxt_account);
        edtxt_passwd = findViewById(R.id.edtxt_passwd);
        txt_account = findViewById(R.id.txt_account);
        txt_passwd = findViewById(R.id.txt_passwd);
        cbox_rememberAccount = findViewById(R.id.cbox_rememberAccount);
        cbox_rememberAccount.setOnCheckedChangeListener(cbox_rememberAccount_select);
        cbox_Autosignin = findViewById(R.id.cbox_Autosignin);
        cbox_Autosignin.setOnCheckedChangeListener(cbox_Autosignin_select);
        setAccount = getSharedPreferences("AccountSetting", Activity.MODE_PRIVATE);
        writeSetting = setAccount.edit();
        RememberSet();
    }

    private void RememberSet() {
        sqliteHelper = new C_MySQLite(this);
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        RemamberAccount = setAccount.getBoolean("RemamberAccountSetting", false);
        SignInAuto = setAccount.getBoolean("AutoSignInSetting", false);
        Log.i("RememberSet","RemamberAccount : "+RemamberAccount);
        if(RemamberAccount){
            String account = setAccount.getString(C_Dictionary.TABLE_SCHEMA_ACCOUNT,"");
            cbox_rememberAccount.setChecked(true);
            edtxt_account.setText(account);
            if(SignInAuto){
                cbox_Autosignin.setChecked(true);
                Toast toast = Toast.makeText(ActEntrance.this, "自動登入中...", Toast.LENGTH_LONG);
                String password = setAccount.getString(C_Dictionary.TABLE_SCHEMA_PASSWORD,"");
                Bundle bundle = new Bundle();
                bundle.putString(C_Dictionary.USER_ACCOUNT, account );
                bundle.putString(C_Dictionary.USER_PASSWORD, password );
                bundle.putInt(C_Dictionary.USER_SIGNIN_OR_SIGNUP,1);
                new C_EntranceTask(new Interface_AsyncEntrance() {
                    @Override
                    public void memberCheckFinish(Boolean checkMember) {
                        System.out.println(checkMember);
                        if (checkMember) {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(ActEntrance.this, ActBottomNav.class);
                            bundle.putString(C_Dictionary.USER_STATUS,C_Dictionary.USER_STATUS_MEMBER);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            Toast toast = Toast.makeText(ActEntrance.this, "登入成功", Toast.LENGTH_LONG);
                            toast.show();
                        } else{
                            Toast toast = Toast.makeText(ActEntrance.this, "帳號密碼輸入錯誤", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }).execute(bundle);
            }
        }else{
            cbox_rememberAccount.setChecked(false);
            return;
        }
    }
    SharedPreferences.Editor writeSetting;
    SharedPreferences setAccount;
    Button btn_Visitors_Signin;
    Button btn_Member_Signin;
    Button btn_SignupMember;
    EditText edtxt_account;
    EditText edtxt_passwd;
    TextView txt_account;
    TextView txt_passwd;
    CheckBox cbox_rememberAccount;
    CheckBox cbox_Autosignin;
    Cursor cursor;

    C_MySQLite sqliteHelper;
    SQLiteDatabase sqLiteDatabase;
}
