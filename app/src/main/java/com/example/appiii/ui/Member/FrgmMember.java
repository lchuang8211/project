package com.example.appiii.ui.Member;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import java.io.File;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrgmMember extends Fragment {
    private static final String TAG = "FrgmMember";
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
    private Uri imageUri; //圖片路徑
    private String filename; //圖片名稱
    private View.OnClickListener myHeadShot_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new String[]{"上傳照片", "拍照"}, new DialogInterface.OnClickListener() {
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
    public String getRealPathFromURI(Context context,Uri uri){
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
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
        switch(budle.getString(C_Dictionary.USER_STATUS)){
            case C_Dictionary.USER_STATUS_MEMBER:
                memberStatus.setText( sharedPreferences.getString(C_Dictionary.USER_NAME_SETTING,"還沒有建立名稱") );
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
        myHeadShot.setOnClickListener(myHeadShot_click);
        btn_mySetting = inflatedView_Member.findViewById(R.id.btn_mySetting);
        btn_mySetting.setOnClickListener(btn_mySetting_click);
        btn_mySchedule = inflatedView_Member.findViewById(R.id.btn_mySchedule);
        btn_mySchedule.setOnClickListener(btn_mySchedule_click);
        btn_myCollect = inflatedView_Member.findViewById(R.id.btn_myCollect);
        btn_myCollect.setOnClickListener(btn_myCollect_click);
        sharedPreferences = getActivity().getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0);

    }
    SharedPreferences sharedPreferences;
    CircleImageView myHeadShot;
    ContentValues values;
    C_MySQLite SQLite_helper;  // helper
    SQLiteDatabase sqLiteDatabase;

    TextView memberStatus;
    Button btn_mySetting;
    Button btn_mySchedule;
    Button btn_myCollect;




}
