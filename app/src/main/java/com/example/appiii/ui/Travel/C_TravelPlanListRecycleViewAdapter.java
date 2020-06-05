package com.example.appiii.ui.Travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.C_MySQLite;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_TravelPlanListRecycleViewAdapter extends RecyclerView.Adapter<C_TravelPlanListRecycleViewAdapter.ViewHolder>{
    private ArrayList<String> AL_UserAccount = new ArrayList<>();
    private ArrayList<String> AL_UserName = new ArrayList<>();
    private ArrayList<String> AL_PlanName = new ArrayList<>();
    private ArrayList<String> AL_StartDate = new ArrayList<>();
    private ArrayList<String> AL_EndDate = new ArrayList<>();
    private ArrayList<String> AL_HeadImg = new ArrayList<>();
//    private ArrayList<URL> AL_HeadImgURL = new ArrayList<>();
    private String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用

    URL url;

    {
        try {
            url = new URL("http://hhlc.ddnsking.com/getImg/u00004.jpeg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Context mContext;

    private static final  String TAG = "TravelPlanList - RecyclerViewAdapter ";
//    URL url = new URL("http://hhlc.ddnsking.com/");


    public C_TravelPlanListRecycleViewAdapter(Context context, ArrayList<String> AL_UserAccount, ArrayList<String> AL_PlanName, ArrayList<String> AL_StartDate, ArrayList<String> AL_EndDate, ArrayList<String> AL_HeadImg) {
        this.AL_UserAccount = AL_UserAccount;
//        this.AL_UserName = AL_UserName;
        this.AL_PlanName = AL_PlanName;
        this.AL_StartDate = AL_StartDate;
        this.AL_EndDate = AL_EndDate;
        this.AL_HeadImg = AL_HeadImg;
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
        holder.txt_planName.setText(AL_PlanName.get(position));
        Log.i(TAG, "onBindViewHolder: AL_PlanName :"+AL_PlanName.get(position));
        holder.txt_planDate.setText(AL_StartDate.get(position)+"~"+AL_EndDate.get(position));

        holder.txt_userAccount.setText(AL_UserAccount.get(position));
        Log.i(TAG, "onBindViewHolder: AL_UserAccount : " + AL_UserAccount.get(position));
        Log.i(TAG, "onBindViewHolder: HeadImg : " + AL_HeadImg.get(position));
//        holder.txt_userName.setText(AL_UserName.get(position));uri

        if(AL_HeadImg.get(position).toString().trim().matches("")){

        }else{
            Glide.with(mContext).asBitmap().load( "http://hhlc.ddnsking.com"+AL_HeadImg.get(position) )
                .skipMemoryCache(true)// 跳過記憶體緩衝
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不要在硬碟儲存緩衝
                .into(holder.getHeadImage);
        }
//        if(AL_HeadImg.get(position) == "" || AL_HeadImg.get(position) == null){

//        }
//       holder.txt_Plan_Name.setText(myPlanName.get(position));

//       Log.i(TAG, "onBindViewHolder: "+ holder.cursor.getInt(0));


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
        CircleImageView getHeadImage;
        TextView txt_userAccount;
        TextView txt_userName;
        TextView txt_planName;
        TextView txt_planDate;
        RelativeLayout planlist_Layout;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor write;
        public ViewHolder(@NonNull View itemView) {   // 設置 item onclisk 定義 UI的動作
            super(itemView);
            planlist_Layout = itemView.findViewById(R.id.planlist_Layout);
            getHeadImage = itemView.findViewById(R.id.getHeadImage);
            txt_userName = itemView.findViewById(R.id.txt_userName);
            txt_userAccount = itemView.findViewById(R.id.txt_userAccount);
            txt_planName = itemView.findViewById(R.id.txt_planName);
            txt_planDate = itemView.findViewById(R.id.txt_planDate);

            planlist_Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, AL_PlanName.get( getAdapterPosition() ));
                    bundle.putString(C_Dictionary.USER_ACCOUNT, AL_UserAccount.get( getAdapterPosition() ));
                    /////////////////////////////////////////////////
                    new C_AsyncGetPlanDetail(
                            /////////////////////////////////////////////////
                            // How to put the ArrayList into bundle
                            // https://stackoverflow.com/questions/42436012/how-to-put-the-arraylist-into-bundle
                            /////////////////////////////////////////////////
                            // Parcelable and Serializable
                            // https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
                    ).execute(bundle);
                    /////////////////////////////////////////////////
                }
            });

        }
    }
}
