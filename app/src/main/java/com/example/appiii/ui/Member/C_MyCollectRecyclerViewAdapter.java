package com.example.appiii.ui.Member;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.ActGoogleMaps;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import java.util.ArrayList;

public class C_MyCollectRecyclerViewAdapter extends RecyclerView.Adapter<C_MyCollectRecyclerViewAdapter.ViewHolder> {

    ViewHolder holder;
    private ArrayList<String> Collect_Node_Name = new ArrayList<>();
    private ArrayList<String> Collect_Node_Describe = new ArrayList<>();
    private ArrayList<Double> Collect_Node_Latitude = new ArrayList<>();
    private ArrayList<Double> Collect_Node_Longitude = new ArrayList<>();
    Context mContext;
    public C_MyCollectRecyclerViewAdapter(Context mContext, ArrayList<String> Collect_Node_Name, ArrayList<String> Collect_Node_Describe, ArrayList<Double> Collect_Node_Latitude, ArrayList<Double> Collect_Node_Longitude) {
        this.mContext = mContext;
        this.Collect_Node_Name = Collect_Node_Name;
        this.Collect_Node_Describe = Collect_Node_Describe;
        this.Collect_Node_Latitude = Collect_Node_Latitude;
        this.Collect_Node_Longitude = Collect_Node_Longitude;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_member_mycollect, parent, false);  //嵌入 RecycleView 的 list item XML
        holder = new C_MyCollectRecyclerViewAdapter.ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull C_MyCollectRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txt_Node_Name.setText(Collect_Node_Name.get(position));
    }

    @Override
    public int getItemCount() {
        return Collect_Node_Name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout getRelativeLayout;
        TextView txt_Node_Name;
        Button btn_deleteCollect;
        C_MySQLite helper;
        SQLiteDatabase SQLiteDB;
        Cursor cursor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Node_Name = itemView.findViewById(R.id.txt_Spot_Name);
            btn_deleteCollect = itemView.findViewById(R.id.btn_deleteCollect);
            getRelativeLayout = itemView.findViewById(R.id.getMyCollect_layout);
            helper = new C_MySQLite(mContext);
            SQLiteDB = helper.getReadableDatabase();
            txt_Node_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(Collect_Node_Name.get(getAdapterPosition()));
                    builder.setMessage("概述:\n\n"+Collect_Node_Describe.get(getAdapterPosition()));
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("查看地圖",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, ActGoogleMaps.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble(C_Dictionary.LOCATION_LATITUDE,Collect_Node_Latitude.get( getAdapterPosition() ));
                            bundle.putDouble(C_Dictionary.LOCATION_LONGITUDE,Collect_Node_Longitude.get( getAdapterPosition() ));
                            bundle.putString(C_Dictionary.SPOT_NAME,Collect_Node_Name.get( getAdapterPosition() ));
//                            Log.i(TAG, "onClick: send bundle :" + bundle);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }).create().show();
                }
            });
            btn_deleteCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("刪除");
                    builder.setMessage("確定刪除 "+Collect_Node_Name.get(getAdapterPosition()) +" 嗎?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDB.delete(C_Dictionary.MY_COLLECTION_TABLE,C_Dictionary.TABLE_SCHEMA_NODE_NAME+"=?",new String[]{Collect_Node_Name.get(getAdapterPosition())});
                            Collect_Node_Name.remove(getAdapterPosition());
                            Collect_Node_Describe.remove(getAdapterPosition());
                            Collect_Node_Latitude.remove(getAdapterPosition());
                            Collect_Node_Longitude.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }).create().show();

                }
            });
        }
    }

}
