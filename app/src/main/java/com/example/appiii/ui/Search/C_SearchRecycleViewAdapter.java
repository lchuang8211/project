package com.example.appiii.ui.Search;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appiii.ActGoogleMaps;
import com.example.appiii.C_Dictionary;
import com.example.appiii.C_NodeInfo;
import com.example.appiii.R;
import com.example.appiii.C_MySQLite;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_SearchRecycleViewAdapter extends RecyclerView.Adapter<C_SearchRecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "RecyclerViewAdapter";

    private ArrayList<C_NodeInfo> searchInfos = new ArrayList<>();
    private Context mContext;
    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結

    private String Type;
    private ArrayList<String> NodeImg = new ArrayList<>();


    public C_SearchRecycleViewAdapter(Context context, ArrayList<C_NodeInfo> searchInfos, ArrayList<String> NodeImg, String Type) {
        this.Type=Type;
        this.NodeImg=NodeImg;
        this.searchInfos = searchInfos;
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

        holder.cursorForBind = holder.sqLiteDB.rawQuery("select 1 from "+C_Dictionary.MY_COLLECTION_TABLE+" where "+C_Dictionary.TABLE_SCHEMA_NODE_NAME+" = '"+ searchInfos.get(position).getNodeName() +"'",null);
        if(holder.cursorForBind.getCount()==0){
            Glide.with(mContext).asBitmap().load( R.drawable.heart_64px ).into(holder.img_Collect);
        }else{
            Glide.with(mContext).asBitmap().load( R.drawable.heart_fill_64px ).into(holder.img_Collect);
        }
        if(Type==C_Dictionary.SPOT_TYPE_HOTEL){
            Glide.with(mContext).asBitmap().load( R.drawable.hotel_128px ).into(holder.getItem_image);
        }
        if(Type==C_Dictionary.SPOT_TYPE_EAT){
            Glide.with(mContext).asBitmap().load( R.drawable.food_128px ).into(holder.getItem_image);
        }
        if(!NodeImg.get(position).equals(""))
            Glide.with(mContext).asBitmap().load( NodeImg.get(position)).into(holder.getItem_image);
        holder.txt_Name_Address.setText(searchInfos.get(position).getNodeName()+"\n"+searchInfos.get(position).getNodeAddress());
//        Log.i(TAG, "onBindViewHolder: txt_Name_Address.get(position): " + mySpotName.get(position)+":"+mySpotAddress.get(position));
    }

    @Override
    public int getItemCount() { // part 3 :
        return searchInfos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用
        CircleImageView getItem_image,img_Collect;
        TextView txt_Name_Address;
        Button getbtn_addTravel;
        Cursor cursorForBind;
        C_MySQLite helper;
        SQLiteDatabase sqLiteDB;
//        Button btn_getItem;
        RelativeLayout getParentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            getItem_image = itemView.findViewById(R.id.getCirlceImage);
            getParentLayout = itemView.findViewById(R.id.getSearchInfoForParent_Layout);
            // RelativeLayout.XML 的 layout 的 Layout ID
            // RelativeLayout getParentLayout = itemView.findViewById(R.id.getSearchInfoForParent_Layout);
            // 把 RelativeLayout 當作 View 並用 findViewById(R.id.RelativeLayout_ID) 尋找 Layout 的 XML 排版
            helper = new C_MySQLite(mContext);
            sqLiteDB = helper.getWritableDatabase();
            img_Collect = itemView.findViewById(R.id.img_Collect);
            getbtn_addTravel = itemView.findViewById(R.id.getbtn_addTravel);
            txt_Name_Address = itemView.findViewById(R.id.txt_Name_Address);
//            btn_getItem = itemView.findViewById(R.id.btn_getItem);
            txt_Name_Address.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle( searchInfos.get( getAdapterPosition() ).getNodeName() );
                    builder.setMessage( "概述:\n" + searchInfos.get( getAdapterPosition() ).getNodeDescribe() );
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("查看位置",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, ActGoogleMaps.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble(C_Dictionary.LOCATION_LATITUDE,searchInfos.get( getAdapterPosition() ).getNodeLat());
                            bundle.putDouble(C_Dictionary.LOCATION_LONGITUDE,searchInfos.get( getAdapterPosition() ).getNodeLong());
                            bundle.putString(C_Dictionary.SPOT_NAME,searchInfos.get( getAdapterPosition() ).getNodeName());
                            Log.i(TAG, "onClick: send bundle :" + bundle);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }).create().show();
                }
            });

            getbtn_addTravel.setOnClickListener(new View.OnClickListener(){
                Cursor cursor;
                ContentValues values;
                C_MySQLite SQLite_helper;
                SQLiteDatabase sqLiteDatabase;
                String[] planName;
                @Override
                public void onClick(View v) {
                    values = new ContentValues();  // insert 用
                    SQLite_helper = new C_MySQLite(mContext);  // helper
                    sqLiteDatabase = SQLite_helper.getReadableDatabase();
                    cursor = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_LIST_Table_Name+" WHERE "+C_Dictionary.USER_U_ID+"=?"+";"
                            ,new String[]{mContext.getSharedPreferences(C_Dictionary.ACCOUNT_SETTING,0).getString(C_Dictionary.USER_U_ID,"0")});
//                    Cursor cursor2 = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_Table_Name +";",null);
                    Log.i("getbtn_addTravel","TRAVEL_LIST_Table_Name:"+cursor.getCount());
                    Log.i("getbtn_addTravel","MY_Table_Name"+cursor.getCount());
                    planName = new String[cursor.getCount()];
                    int count = 0 ;
                    while (cursor.moveToNext()){
                        planName[count] = cursor.getString(cursor.getColumnIndex(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME)).trim();
                        count++;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("選擇要加入行程");
                    builder.setItems(planName, new DialogInterface.OnClickListener(){    // addItemToSchedule_selected 點擊加入
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // select exists (select 1 from TRAvel_list_table_name);  // 檢查表是否為 empty
                            // select COLUMN_NAME_DATE  from 耶呼 group by COLUMN_NAME_DATE order by COLUMN_NAME_DATE desc LIMIT 1  // 找最後的天數
                            // select count(COLUMN_NAME_DATE)  from 耶呼 Where COLUMN_NAME_DATE = 2 group by COLUMN_NAME_DATE // 找當天的最後一個行程
                            String newTablename = "["+ C_Dictionary.CREATE_TABLE_HEADER+planName[which]+"]";

                            SharedPreferences sp = mContext.getSharedPreferences(C_Dictionary.PLAN_DAYS_RECORD,0);
                            int days = sp.getInt(planName[which],1);
                            final int whichplan = which;
                            String[] SingleDay=new String[days];
                            for(int i = 1; i<=days ; i++){
                                SingleDay[i-1]="第 "+i+" 天";
                            }
                            AlertDialog.Builder subbuilder = new AlertDialog.Builder(mContext);
                            subbuilder.setTitle("選一天");
                            subbuilder.setItems(SingleDay, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int innerwhich) {
                                    // get which
                                    cursor=sqLiteDatabase.rawQuery("select count(COLUMN_NAME_DATE)  from "+"["+C_Dictionary.CREATE_TABLE_HEADER+planName[whichplan]+"]"+" Where COLUMN_NAME_DATE = "+(innerwhich+1)+" group by COLUMN_NAME_DATE",null);
                                    int maxQueue=0;
                                    if(cursor.getCount()>0){
                                        cursor.moveToLast();
                                        maxQueue = cursor.getInt(0);
                                    }
                                    values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, searchInfos.get( getAdapterPosition() ).getNodeName() );  //地點名稱
                                    values.put(C_Dictionary.TABLE_SCHEMA_DATE,innerwhich+1);
                                    values.put(C_Dictionary.TABLE_SCHEMA_QUEUE,(maxQueue+1));
                                    values.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE, searchInfos.get(getAdapterPosition()).getNodeDescribe());
                                    values.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,searchInfos.get(getAdapterPosition()).getNodeLat() );
                                    values.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,searchInfos.get(getAdapterPosition()).getNodeLong());
                                    values.put(C_Dictionary.SPOT_TYPE, Type);
                                    sqLiteDatabase.insert( "["+C_Dictionary.CREATE_TABLE_HEADER+planName[whichplan]+"]" ,null,values);
                                }
                            }).create().show(); //第二個

                        }
                    }).create().show();  //第一個
                }
            });
            img_Collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    C_MySQLite SQLite_helper = new C_MySQLite(mContext);
                    SQLiteDatabase sqLiteDatabase= SQLite_helper.getWritableDatabase();
                    Cursor cursor;
                    cursor = sqLiteDatabase.rawQuery("select "+ C_Dictionary.TABLE_SCHEMA_NODE_NAME
                            +" from "+C_Dictionary.MY_COLLECTION_TABLE
                            +" WHERE "+C_Dictionary.TABLE_SCHEMA_NODE_NAME+" = '"+searchInfos.get(getAdapterPosition()).getNodeName() +"'"  ,null);
                    Log.i("cursor","cursor : "+cursor.getCount());
                    if (cursor.getCount()==0){
                        Boolean changed_collat = true;
                        ContentValues values = new ContentValues();
                        values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, searchInfos.get( getAdapterPosition() ).getNodeName());
                        //Toast.makeText(mContext,"getNodeName"+searchInfos.get( getAdapterPosition() ).getNodeName(),Toast.LENGTH_SHORT).show();
                        values.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE, searchInfos.get(getAdapterPosition()).getNodeDescribe());
                        values.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE, searchInfos.get(getAdapterPosition()).getNodeLat());
                        values.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE, searchInfos.get(getAdapterPosition()).getNodeLong());
                        values.put(C_Dictionary.SPOT_TYPE, "");
                        sqLiteDatabase.insert(C_Dictionary.MY_COLLECTION_TABLE, null, values);
                        Glide.with(mContext).asBitmap().load(  R.drawable.heart_fill_64px ).into(img_Collect);
                    }
                    if (cursor.getCount()>=1){
                        sqLiteDatabase.delete(C_Dictionary.MY_COLLECTION_TABLE,C_Dictionary.TABLE_SCHEMA_NODE_NAME+"=?",new String[]{searchInfos.get( getAdapterPosition() ).getNodeName()});
                        Glide.with(mContext).asBitmap().load(  R.drawable.heart_64px ).into(img_Collect);
                        return;
                    }
                }
            });





        }
    }
}
