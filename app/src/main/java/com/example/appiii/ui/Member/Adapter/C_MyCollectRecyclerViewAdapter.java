package com.example.appiii.ui.Member.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.appiii.C_MySQLite;
import com.example.appiii.C_NodeInfo;
import com.example.appiii.R;

import java.util.ArrayList;

public class C_MyCollectRecyclerViewAdapter extends RecyclerView.Adapter<C_MyCollectRecyclerViewAdapter.ViewHolder> {

    ViewHolder holder;
    private ArrayList<String> Collect_Node_Name = new ArrayList<>();
    private ArrayList<String> Collect_Node_Describe = new ArrayList<>();
    private ArrayList<Double> Collect_Node_Latitude = new ArrayList<>();
    private ArrayList<Double> Collect_Node_Longitude = new ArrayList<>();
    private ArrayList<C_NodeInfo> CollectInfos = new ArrayList<>();
    Context mContext;
    public C_MyCollectRecyclerViewAdapter(Context mContext, ArrayList<C_NodeInfo> CollectInfos) {
        this.mContext = mContext;
        this.CollectInfos = CollectInfos;

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
        holder.txt_Node_Name.setText(CollectInfos.get(position).getNodeName());
    }

    @Override
    public int getItemCount() {
        return CollectInfos.size();
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
                    builder.setTitle( CollectInfos.get(getAdapterPosition()).getNodeName() );
                    builder.setMessage("概述:\n\n" + CollectInfos.get(getAdapterPosition()).getNodeDescribe() );
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("查看地圖",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, ActGoogleMaps.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble(C_Dictionary.LOCATION_LATITUDE, CollectInfos.get( getAdapterPosition() ).getNodeLat() );
                            bundle.putDouble(C_Dictionary.LOCATION_LONGITUDE, CollectInfos.get( getAdapterPosition() ).getNodeLong());
                            bundle.putString(C_Dictionary.SPOT_NAME, CollectInfos.get( getAdapterPosition() ).getNodeName());
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
                    builder.setMessage("確定刪除 "+CollectInfos.get(getAdapterPosition()).getNodeName() +" 嗎?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDB.delete(C_Dictionary.MY_COLLECTION_TABLE,C_Dictionary.TABLE_SCHEMA_NODE_NAME+"=?",new String[]{CollectInfos.get( getAdapterPosition() ).getNodeName()});
                            CollectInfos.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }).create().show();

                }
            });
        }
    }

}
