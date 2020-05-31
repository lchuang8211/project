package com.example.appiii.ui.Travel;

import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.ActMemberShowTravelPlan;
import com.example.appiii.ui.Member.C_AsyncPublicPlan;
import com.example.appiii.ui.Member.C_MySQLite;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_TravelPlanListRecycleViewAdapter extends RecyclerView.Adapter<C_TravelPlanListRecycleViewAdapter.ViewHolder>{
    private ArrayList<String> AL_UserAccount = new ArrayList<>();
    private ArrayList<String> AL_UserName = new ArrayList<>();
    private ArrayList<String> AL_PlanName = new ArrayList<>();
    private ArrayList<String> AL_StartDate = new ArrayList<>();
    private ArrayList<String> AL_EndDate = new ArrayList<>();
    private Context mContext;
    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結
    private static final  String TAG = "TravelPlanList - RecyclerViewAdapter ";



    public C_TravelPlanListRecycleViewAdapter(Context context, ArrayList<String> AL_UserAccount, ArrayList<String> AL_UserName, ArrayList<String> AL_PlanName, ArrayList<String> AL_StartDate, ArrayList<String> AL_EndDate) {
        this.AL_UserAccount = AL_UserAccount;
        this.AL_UserName = AL_UserName;
        this.AL_PlanName = AL_PlanName;
        this.AL_StartDate = AL_StartDate;
        this.AL_EndDate = AL_EndDate;
        this.mContext = context;
    }

    ViewHolder holder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_travel_planlist, parent, false);  //嵌入 RecycleView 的 list item XML
        holder = new ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {  // part 2 : 複製 RecyclerView 的 XML && 定義 XML 的設定
        Log.i(TAG, "onBindViewHolder: called");
        holder.setIsRecyclable(false);
        Log.i(TAG, "onBindViewHolder: holder.getAdapterPosition():"+ holder.getAdapterPosition());
        holder.txt_planDate.setText(AL_StartDate.get(position)+"~"+AL_EndDate.get(position));
        holder.txt_userAccount.setText(AL_UserAccount.get(position));
        holder.txt_userName.setText(AL_UserName.get(position));
        holder.txt_planName.setText(AL_PlanName.get(position));
//        holder.txt_Plan_Name.setText(myPlanName.get(position));

//        Log.i(TAG, "onBindViewHolder: "+ holder.cursor.getInt(0));


    }

    @Override
    public int getItemCount() { // part 3 :
        return AL_UserAccount.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用

        Cursor cursor, cursorForBind;
        C_MySQLite SQLite_helper;
        SQLiteDatabase sqLiteDatabase;
        LinearLayout layout;
        CircleImageView getItem_image;
        TextView txt_userAccount;
        TextView txt_userName;
        TextView txt_planName;
        TextView txt_planDate;
        RelativeLayout getParentLayout;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor write;
        public ViewHolder(@NonNull View itemView) {   // 設置 item onclisk 定義 UI的動作
            super(itemView);
            getParentLayout = itemView.findViewById(R.id.planlist_Layout);
            txt_userName = itemView.findViewById(R.id.txt_userName);
            txt_userAccount = itemView.findViewById(R.id.txt_userAccount);
            txt_planName = itemView.findViewById(R.id.txt_planName);
            txt_planDate = itemView.findViewById(R.id.txt_planDate);
        }
    }
}
