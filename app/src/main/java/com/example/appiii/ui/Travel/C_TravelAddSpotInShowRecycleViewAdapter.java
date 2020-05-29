package com.example.appiii.ui.Travel;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.ActGoogleMaps;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.C_MySQLite;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_TravelAddSpotInShowRecycleViewAdapter extends RecyclerView.Adapter<C_TravelAddSpotInShowRecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mySpotName = new ArrayList<>();
    private ArrayList<String> mySpotAddress = new ArrayList<>();
    private ArrayList<String> mySpotToldescribe = new ArrayList<>();
    private ArrayList<Double> mySpotLatitude = new ArrayList<>();
    private ArrayList<Double> mySpotLongitude = new ArrayList<>();
    static int gettheDays;
    static String rawTablename;
    static String new_Tablename;
    private Context mContext;
    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結



    public C_TravelAddSpotInShowRecycleViewAdapter(Context context, ArrayList<String> mySpotName, ArrayList<String> mySpotAddress, ArrayList<String> mySpotToldescribe, ArrayList<Double> mySpotLatitude, ArrayList<Double> mySpotLongitude, int getDays, String Tablename) {
        this.mySpotName = mySpotName;
        this.mySpotAddress = mySpotAddress;
        this.mySpotToldescribe = mySpotToldescribe;
        this.mySpotLatitude = mySpotLatitude;
        this.mySpotLongitude = mySpotLongitude;
        this.gettheDays = getDays;
        this.rawTablename=Tablename;
        this.mContext = context;
    }
    ViewHolder holder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_search_showinfo, parent, false);  //嵌入 RecycleView 的 list item XML
        holder = new ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {  // part 2 : 複製 RecyclerView 的 XML && 定義 XML 的設定
        Log.i(TAG, "onBindViewHolder: called");
        holder.setIsRecyclable(false);
        Log.i(TAG, "onBindViewHolder: holder.getAdapterPosition():"+ holder.getAdapterPosition());
//        Glide.with(mContext).asBitmap().load( uri ).into(holder.getItem_image);  // Gilde : 圖片 library
        holder.txt_Name_Address.setText(mySpotName.get(position)+"\n"+mySpotAddress.get(position));
        Log.i(TAG, "onBindViewHolder: txt_Name_Address.get(position): " + mySpotName.get(position)+":"+mySpotAddress.get(position));
    }

    @Override
    public int getItemCount() { // part 3 :
        return mySpotName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用
        CircleImageView getItem_image;
        TextView txt_Name_Address;
        Button getbtn_addTravel;
        Button btn_finish;
//        Button btn_getItem;
        RelativeLayout getParentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            getbtn_addTravel = itemView.findViewById(R.id.getbtn_addTravel);
            txt_Name_Address = itemView.findViewById(R.id.txt_Name_Address);
//            btn_getItem = itemView.findViewById(R.id.btn_getItem);
            txt_Name_Address.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle( mySpotName.get( getAdapterPosition() ) );
                    builder.setMessage( "概述:\n" + mySpotToldescribe.get( getAdapterPosition() ) );
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("查看位置",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, ActGoogleMaps.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble(C_Dictionary.LOCATION_LATITUDE,mySpotLatitude.get( getAdapterPosition() ));
                            bundle.putDouble(C_Dictionary.LOCATION_LONGITUDE,mySpotLongitude.get( getAdapterPosition() ));
                            bundle.putString(C_Dictionary.SPOT_NAME,mySpotName.get( getAdapterPosition() ));
                            Log.i(TAG, "onClick: send bundle :" + bundle);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            });
            new_Tablename = C_Dictionary.CREATE_TABLE_HEADER+rawTablename;
            getbtn_addTravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cursor;
                    C_MySQLite sqLiteHelper = new C_MySQLite(mContext);
                    SQLiteDatabase sqLiteDatabase =sqLiteHelper.getReadableDatabase();

                    int maxQueue=0;
                    cursor=sqLiteDatabase.rawQuery("select count(COLUMN_NAME_DATE)  from ["+new_Tablename +"] Where COLUMN_NAME_DATE = "+gettheDays+" group by COLUMN_NAME_DATE",null);
                    Log.i("C_TravelAddSpotInShowRecycleViewAdapter","for search table name : "+new_Tablename);
                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                        maxQueue = cursor.getInt(0);
                    }else{
                        cursor.moveToFirst();
                        maxQueue=0;
                    }
                    Log.i("C_TravelAddSpotInShowRecycleViewAdapter","maxQueue : "+ maxQueue);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, mySpotName.get( getAdapterPosition() ) );  //地點名稱
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_DATE,gettheDays);
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_QUEUE,maxQueue+1);
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,mySpotLongitude.get(getAdapterPosition()) );
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,mySpotLatitude.get(getAdapterPosition()));
                    contentValues.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE,mySpotToldescribe.get(getAdapterPosition()));
                    sqLiteDatabase.insert( "["+new_Tablename+"]",null,contentValues);
                }
            });



            getItem_image = itemView.findViewById(R.id.getCirlceImage);
//            getItem_txt = itemView.findViewById(R.id.getItem_txt);
            getParentLayout = itemView.findViewById(R.id.getSearchInfoForParent_Layout);
            // RelativeLayout.XML 的 layout 的 Layout ID
            // RelativeLayout getParentLayout = itemView.findViewById(R.id.getSearchInfoForParent_Layout);
            // 把 RelativeLayout 當作 View 並用 findViewById(R.id.RelativeLayout_ID) 尋找 Layout 的 XML 排版


        }
    }
}
