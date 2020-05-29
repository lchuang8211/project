package com.example.appiii.ui.Search;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import com.example.appiii.ActGoogleMaps;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.C_MySQLite;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_SearchRecycleViewAdapter extends RecyclerView.Adapter<C_SearchRecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mySpotName = new ArrayList<>();
    private ArrayList<String> mySpotAddress = new ArrayList<>();
    private ArrayList<String> mySpotToldescribe = new ArrayList<>();
    private ArrayList<Double> mySpotLatitude = new ArrayList<>();
    private ArrayList<Double> mySpotLongitude = new ArrayList<>();
    private Context mContext;
    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結



    public C_SearchRecycleViewAdapter(Context context, ArrayList<String> mySpotName, ArrayList<String> mySpotAddress, ArrayList<String> mySpotToldescribe, ArrayList<Double> mySpotLatitude, ArrayList<Double> mySpotLongitude) {
        this.mySpotName = mySpotName;
        this.mySpotAddress = mySpotAddress;
        this.mySpotToldescribe = mySpotToldescribe;
        this.mySpotLatitude = mySpotLatitude;
        this.mySpotLongitude = mySpotLongitude;
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

            getbtn_addTravel.setOnClickListener(new View.OnClickListener(){
                Cursor cursor;
                ContentValues values;
                C_MySQLite SQLite_helper;
                SQLiteDatabase sqLiteDatabase;
                String[] planName;
                @Override
                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString( C_Dictionary.SEARCH_SPOT_INFO_COPY, mySpotName.get( getAdapterPosition() ) );
                    values = new ContentValues();  // insert 用
                    SQLite_helper = new C_MySQLite(mContext);  // helper
                    sqLiteDatabase = SQLite_helper.getReadableDatabase();
                    cursor = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_LIST_Table_Name+";",null);
//                    Cursor cursor2 = sqLiteDatabase.rawQuery("select * from "+C_Dictionary.TRAVEL_Table_Name +";",null);
                    Log.i("getbtn_addTravel","TRAVEL_LIST_Table_Name:"+cursor.getCount());
                    Log.i("getbtn_addTravel","MY_Table_Name"+cursor.getCount());
                    planName = new String[(int)DatabaseUtils.queryNumEntries(sqLiteDatabase,C_Dictionary.TRAVEL_LIST_Table_Name)];
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
                            Log.i("maxdate","exists getAdapterPosition :"+which);
                            Log.i("maxdate","exists planName getAdapterPosition :"+planName[which]);
                            cursor = sqLiteDatabase.rawQuery("select exists (select 1 from "+ newTablename +" )",null);
                            cursor.moveToLast();
                            int empty = cursor.getInt(0);
                            Log.i("maxdate","exists empty :"+empty);
                            if (empty==0){   // 如果 TABLE 是空 TABLE
//                                values.put( C_Dictionary.TABLE_SCHEMA_id, DatabaseUtils.queryNumEntries(sqLiteDatabase,planName[getAdapterPosition()]) );
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, mySpotName.get( getAdapterPosition() ) );  //地點名稱
                                values.put(C_Dictionary.TABLE_SCHEMA_DATE,1);
                                values.put(C_Dictionary.TABLE_SCHEMA_QUEUE,1);
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,mySpotLongitude.get(getAdapterPosition()) );
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,mySpotLatitude.get(getAdapterPosition()));
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE,mySpotToldescribe.get(getAdapterPosition()));
                                sqLiteDatabase.insert( newTablename,null,values);
                            }else if(empty==1){ // 如果 TABLE 不為空 TABLE
                            cursor=sqLiteDatabase.rawQuery("select COLUMN_NAME_DATE  from "+ newTablename +" group by COLUMN_NAME_DATE order by COLUMN_NAME_DATE desc LIMIT 1",null);
                            cursor.moveToLast();
                            int maxdate = cursor.getInt(0);
                            Log.i("maxdate","maxdate:"+maxdate);
                            cursor=sqLiteDatabase.rawQuery("select count(COLUMN_NAME_DATE)  from "+ newTablename +" Where COLUMN_NAME_DATE = "+maxdate+" group by COLUMN_NAME_DATE",null);
                            cursor.moveToLast();
                            int maxQueue = cursor.getInt(0);
                            Log.i("maxQueue","maxQueue:"+maxQueue);
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME, mySpotName.get( getAdapterPosition() ) );  //地點名稱
                                values.put(C_Dictionary.TABLE_SCHEMA_DATE,maxdate);
                                values.put(C_Dictionary.TABLE_SCHEMA_QUEUE,(maxQueue+1));
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE,mySpotLongitude.get(getAdapterPosition()) );
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE,mySpotLatitude.get(getAdapterPosition()));
                                values.put(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE,mySpotToldescribe.get(getAdapterPosition()));
                                sqLiteDatabase.insert( newTablename,null,values);

                            AlertDialog.Builder builderDays = new AlertDialog.Builder(mContext);
                            builderDays.setTitle("select a day");
                            String[] Days=new String[maxdate];
                            for(int i=1; i<=maxdate;i++){
                                Days[i-1] = "第 "+ i +" 天的行程";
                            }
                            builderDays.setItems(Days,null).create().show();


                            }
//                            cursor=sqLiteDatabase.rawQuery("select COLUMN_NAME_DATE  from 耶呼 group by COLUMN_NAME_DATE order by COLUMN_NAME_DATE desc LIMIT 1",null);
//                            values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME,planName[getAdapterPosition()]);
//                            values.put(C_Dictionary.TABLE_SCHEMA_NODE_NAME,planName[getAdapterPosition()]);
                            Toast.makeText(mContext,which+":"+planName[which],Toast.LENGTH_LONG).show();
                        }
                    })
                    .create()
                    .show();
//                    builder.setPositiveButton("ok",null);
//                    Dialog dialog = builder.create();
//                    dialog.show();
//                    Toast toast = Toast.makeText(mContext,mySpotName.get( getAdapterPosition() ),Toast.LENGTH_LONG);
//                    toast.show();
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
