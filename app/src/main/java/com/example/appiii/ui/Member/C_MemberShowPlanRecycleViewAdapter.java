package com.example.appiii.ui.Member;

import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.ActGoogleMaps;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_MemberShowPlanRecycleViewAdapter extends RecyclerView.Adapter<C_MemberShowPlanRecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "C_TravelShowPlanRecycleViewAdapter";
    private ArrayList<String> showSpotName = new ArrayList<>();
    private ArrayList<Integer> showSpotDate = new ArrayList<>();
    private ArrayList<Integer> showSpotQueue = new ArrayList<>();
    private ArrayList<Double> showSpotLatitude = new ArrayList<>();
    private ArrayList<Double> showSpotLongitude = new ArrayList<>();
    private ArrayList<String> showSpotDescrbe = new ArrayList<>();
    private String planName;
    private int WhichDay;
    private Context mContext;


    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結


    public C_MemberShowPlanRecycleViewAdapter(Context context, ArrayList<Integer> showSpotDate, ArrayList<Integer> showSpotQueue, ArrayList<String> showSpotName, ArrayList<Double> showSpotLatitude, ArrayList<Double> showSpotLongitude, ArrayList<String> showSpotDescrbe, String planName, int WhichDay) {
        this.showSpotDate = showSpotDate;
        this.showSpotQueue = showSpotQueue;
        this.showSpotName = showSpotName;
        this.showSpotLatitude = showSpotLatitude;
        this.showSpotLongitude = showSpotLongitude;
        this.showSpotDescrbe=showSpotDescrbe;
        this.planName = planName;
        this.WhichDay = WhichDay;
        this.mContext = context;
    }
    ViewHolder holder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_member_show_plan, parent, false);  //嵌入 RecycleView 的 list item XML
        holder = new ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {  // part 2 : 複製 RecyclerView 的 XML && 定義 XML 的設定
        holder.setIsRecyclable(false);
        holder.txt_Spot_Name.setText("第 "+(position+1)+" 個行程 :\n "+ showSpotName.get(position));
//        holder.txt_Plan_info.setText( String.valueOf(showSpotLatitude.get(position))+","+String.valueOf(showSpotLongitude.get(position)) );
////        Glide.with(mContext).asBitmap().load( uri ).into(holder.getItem_image);  // Gilde : 圖片 library

    }

    @Override
    public int getItemCount() { // part 3 :
        return showSpotDate.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用
        Cursor cursor;
        C_MySQLite SQLite_helper;
        SQLiteDatabase sqLiteDatabase;
        LinearLayout layout;
        CircleImageView getItem_image;
        TextView txt_Spot_Name;
        Button btn_delete;
        CheckBox cbox_pushToCloud;
        RelativeLayout getParentLayout;  // recyclerView 的 使用的 RelativeLayout 排版

        public ViewHolder(@NonNull View itemView) {   // 設置 item onclisk 定義 UI的動作
            super(itemView);
            cbox_pushToCloud = itemView.findViewById(R.id.cbox_pushToCloud);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            txt_Spot_Name = itemView.findViewById(R.id.txt_Spot_Name);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_showPlan);
            getItem_image = itemView.findViewById(R.id.getCirlceImage);
            getParentLayout = itemView.findViewById(R.id.getTravelShowPlanForParent_Layout);
            // RelativeLayout.XML 的 layout 的 Layout ID
            // RelativeLayout getParentLayout = itemView.findViewById(R.id.getSearchInfoForParent_Layout);
            // 把 RelativeLayout 當作 View 並用 findViewById(R.id.RelativeLayout_ID) 尋找 Layout 的 XML 排版
            btn_delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mContext);
                    deleteDialog.setTitle("刪除");
                    deleteDialog.setMessage("確定刪除 "+ showSpotName.get(getAdapterPosition())+" 嗎?");
                    deleteDialog.setNegativeButton("取消",null);
                    deleteDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            C_MySQLite sqLitehelper = new C_MySQLite(mContext);
                            SQLiteDatabase sqLiteDatabase = sqLitehelper.getReadableDatabase();
                            sqLiteDatabase.delete("["+ C_Dictionary.CREATE_TABLE_HEADER+ planName +"]",
                                    C_Dictionary.TABLE_SCHEMA_NODE_NAME+"=? AND "+C_Dictionary.TABLE_SCHEMA_DATE +"=? AND "+C_Dictionary.TABLE_SCHEMA_QUEUE +"=?",
                                    new String[] { showSpotName.get(getAdapterPosition()) , String.valueOf(WhichDay), String.valueOf( showSpotQueue.get(getAdapterPosition()) ) });
                            showSpotName.remove(getAdapterPosition());
                            showSpotDate.remove(getAdapterPosition());
                            showSpotQueue.remove(getAdapterPosition());
                            showSpotLatitude.remove(getAdapterPosition());
                            showSpotLongitude.remove(getAdapterPosition());
                            showSpotDescrbe.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }).create().show();

                    getAdapterPosition();

                 }
                 });

            txt_Spot_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder showbuilder = new AlertDialog.Builder(mContext);
                    showbuilder.setTitle( showSpotName.get(getAdapterPosition()) );
                    showbuilder.setMessage("概述:\n\n"+showSpotDescrbe.get(getAdapterPosition()));
                    showbuilder.setNegativeButton("取消",null);
                    showbuilder.setPositiveButton("查看地圖",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, ActGoogleMaps.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble(C_Dictionary.LOCATION_LATITUDE, showSpotLatitude.get( getAdapterPosition() ));
                            Log.i(TAG, "onClick: Longitude 顛倒 : "+showSpotLatitude.get( getAdapterPosition()));
                            bundle.putDouble(C_Dictionary.LOCATION_LONGITUDE, showSpotLongitude.get( getAdapterPosition() ));
                            Log.i(TAG, "onClick: sLatitude 顛倒 : "+showSpotLongitude.get( getAdapterPosition()));
                            bundle.putString(C_Dictionary.SPOT_NAME, showSpotName.get( getAdapterPosition() ));
                            Log.i(TAG, "onClick: send bundle :" + bundle);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }).create().show();
                }
            });

        }
    }
}
