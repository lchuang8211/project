package com.example.appiii.ui.Member;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.appiii.C_Dictionary;
import com.example.appiii.C_MySQLite;
import com.example.appiii.R;
import com.example.appiii.ui.Member.Activity.ActMyCollect;
import com.example.appiii.ui.Member.Activity.ActMySchedule;
import com.example.appiii.ui.Member.AsyncTask.C_AsyncUploadImg;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgMember extends Fragment {
    private static final String TAG = "FrgMember";
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
                            w.putBoolean(C_Dictionary.AUTO_SIGN_IN_SETTING,false).commit();
//                            txt_setting.setText("notAutoSignIn"); AutoSignInSetting
                            break;
                        case R.id.rdb_AgreeAutoSignIn:
                            w.putBoolean(C_Dictionary.AUTO_SIGN_IN_SETTING,true).commit();
//                            txt_setting.setText("AutoSignIn");
                            break;
                        default:

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
            Intent intent = new Intent(getActivity(), ActMySchedule.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener btn_myCollect_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActMyCollect.class);
            startActivity(intent);
        }
    };
    private Uri imageUri; //圖片路徑
    private String filename; //圖片名稱
    private View.OnClickListener myHeadShot_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new String[]{"上傳照片"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case 0:

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                            intent.addCategory(Intent.CATEGORY_OPENABLE);

                            intent.putExtra("return-data", true);
                            intent.putExtra("crop", "true");
                            //設定寬高比例
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            //設定裁剪圖片寬高、
                            intent.putExtra("outputX", 450);
                            intent.putExtra("outputY", 450);
                            startActivityForResult(intent,501);

                            Log.i(TAG, "onClick: which 0 : " +which);
                            break;
                        case 1:
                            Log.i(TAG, "onClick: which 1 : " +which);
                            break;
                    }
                }
            }).create().show();
        }
    };
    private String imagePath;//將要上傳的圖片路徑
    private View.OnLongClickListener userNickName_longclick = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("請輸入暱稱");
            LinearLayout linearLayout = new LinearLayout(getContext());
            final EditText edname = new EditText(getContext());
            edname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(edname);
            builder.setView(linearLayout);
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (edname.getText().toString().matches(""))
                        return;
//                    w.putString(C_Dictionary.USER_NICK_NAME,edname.getText().toString()).commit();
                    // 修改 要 上傳資料庫
                    userNickName.setText(edname.getText().toString());
                }
            }).create().show();

            return true;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 501:
                try{
                    Uri uri = data.getData();
//                    File file = new File(uri.getPath());
                    Log.i(TAG, "onActivityResult: 111 uri " +uri);
                    new C_AsyncUploadImg(getContext()).execute(uri);
                    myHeadShot.setImageURI(uri);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Member = inflater.inflate(R.layout.frg_member,container,false);
        budle = this.getArguments();   // 在fragment 中拿到 bundle
        InitialComponent();
        if(sharedPreferences.getInt(C_Dictionary.USER_STATUS,0)==1);{
            btn_mySetting.setEnabled(true);
            btn_mySchedule.setEnabled(true);
            btn_myCollect.setEnabled(true);
            userNickName.setText(sharedPreferences.getString(C_Dictionary.USER_NICK_NAME,"vister"));
        }
        if (sharedPreferences.getInt(C_Dictionary.USER_STATUS,0)==0){
            btn_mySetting.setEnabled(false);
            btn_mySchedule.setEnabled(false);
            btn_myCollect.setEnabled(false);
            userNickName.setText("Vister");
            myHeadShot.setClickable(false);
            Glide.with(getContext()).asBitmap().load(R.drawable.user_64px).into(myHeadShot);
        }

        return inflatedView_Member;
    }

    private void InitialComponent() {
        sharedPreferences = getActivity().getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);
        w = sharedPreferences.edit();
        userNickName = inflatedView_Member.findViewById(R.id.memberStatus);
        userNickName.setOnLongClickListener(userNickName_longclick);
        myHeadShot = inflatedView_Member.findViewById(R.id.myHeadShot);
        myHeadShot.setOnClickListener(myHeadShot_click);
        Log.i(TAG, "InitialComponent: 12333333 "+ sharedPreferences.getString(C_Dictionary.USER_HEAD_IMG,"1"));

        switch(sharedPreferences.getString(C_Dictionary.USER_HEAD_IMG,"1")){
            case "":
                Glide.with(getContext()).asBitmap().load(R.drawable.user_64px).into(myHeadShot);
                break;
            default:
                Glide.with(getContext()).asBitmap().load(C_Dictionary.MY_SERVER_URL+"/"+sharedPreferences.getString(C_Dictionary.USER_HEAD_IMG,""))
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(myHeadShot);
                break;
        }

        btn_mySetting = inflatedView_Member.findViewById(R.id.btn_mySetting);
        btn_mySetting.setOnClickListener(btn_mySetting_click);
        btn_mySchedule = inflatedView_Member.findViewById(R.id.btn_mySchedule);
        btn_mySchedule.setOnClickListener(btn_mySchedule_click);
        btn_myCollect = inflatedView_Member.findViewById(R.id.btn_myCollect);
        btn_myCollect.setOnClickListener(btn_myCollect_click);
        txt_return = inflatedView_Member.findViewById(R.id.txt_return);
        txt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

    }
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor w;
    CircleImageView myHeadShot;
    ContentValues values;
    C_MySQLite SQLite_helper;  // helper
    SQLiteDatabase sqLiteDatabase;

    TextView userNickName , txt_return;
    Button btn_mySetting;
    Button btn_mySchedule;
    Button btn_myCollect;




}
