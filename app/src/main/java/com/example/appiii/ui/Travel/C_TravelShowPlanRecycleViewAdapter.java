package com.example.appiii.ui.Travel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.C_Member_SQLite;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_TravelRecycleViewAdapter extends RecyclerView.Adapter<C_TravelRecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "RecyclerViewAdapter";
    private ArrayList<String> myPlanName = new ArrayList<>();
    private ArrayList<String> myPlanDate = new ArrayList<>();
    private ArrayList<Integer> myPlanTotalDay = new ArrayList<>();
    private Context mContext;
    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結



    public C_TravelRecycleViewAdapter(Context context, ArrayList<String> myPlanName, ArrayList<String> myPlanDate, ArrayList<Integer> myPlanTotalDay) {
        this.myPlanName = myPlanName;
        this.myPlanDate = myPlanDate;
        this.myPlanTotalDay = myPlanTotalDay;
        this.mContext = context;
    }
    ViewHolder holder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_travel, parent, false);  //嵌入 RecycleView 的 list item XML
        holder = new ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {  // part 2 : 複製 RecyclerView 的 XML && 定義 XML 的設定
        Log.i(TAG, "onBindViewHolder: called");
        holder.setIsRecyclable(false);
        Log.i(TAG, "onBindViewHolder: holder.getAdapterPosition():"+ holder.getAdapterPosition());
        Log.i(TAG, "onBindViewHolder: myPlanName :"+ myPlanName.size());
        Log.i(TAG, "onBindViewHolder: myPlanDate :"+ myPlanDate.size());
        Log.i(TAG, "onBindViewHolder: myPlanTotalDay :"+ myPlanTotalDay.size());

        holder.txt_Plan_Name.setText(myPlanName.get(position));
        holder.txt_Plan_date.setText(myPlanDate.get(position) +" "+myPlanTotalDay.get(position)+" 天");
//        Glide.with(mContext).asBitmap().load( uri ).into(holder.getItem_image);  // Gilde : 圖片 library
//        holder.txt_Name_Address.setText(myPlanName.get(position));
        Log.i(TAG, "onBindViewHolder: myPlanName.get(position): " + myPlanName.get(position));
        Log.i(TAG, "onBindViewHolder: myPlanTotalDay.get(position): " + myPlanTotalDay.get(position));
    }

    @Override
    public int getItemCount() { // part 3 :
        return myPlanName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用
        Cursor cursor;
        C_Member_SQLite SQLite_helper;
        SQLiteDatabase sqLiteDatabase;
        LinearLayout layout;
        CircleImageView getItem_image;
        TextView txt_Plan_date;
        TextView txt_Plan_Name;
        RelativeLayout getParentLayout;
        public ViewHolder(@NonNull View itemView) {   // 設置 item onclisk 定義 UI的動作
            super(itemView);
            txt_Plan_date = itemView.findViewById(R.id.txt_Plan_date);
            txt_Plan_Name = itemView.findViewById(R.id.txt_Plan_Name);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_showPlan);

            layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActShowTravelPlan.class);
                    Bundle bundle = new Bundle();
                    SQLite_helper = new C_Member_SQLite(mContext);
                    sqLiteDatabase = SQLite_helper.getReadableDatabase();
                    
                    String newTablename = "["+ C_Dictionary.CREATE_TABLE_HEADER+myPlanName.get( getAdapterPosition())+"]";

                    cursor=sqLiteDatabase.rawQuery("select exists (select 1 from "+ newTablename +" )",null);
                    cursor.moveToLast();
                    int empty = cursor.getInt(0);
                    if (empty==0){
                        Log.i("on click","layout : empty :" + empty);
                        bundle.putString(C_Dictionary.TRAVEL_PLAN_IS_EMPTY, myPlanName.get( getAdapterPosition()) );
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    if (empty==1){
                        Log.i("on click","layout : empty :" + empty);
                        cursor=sqLiteDatabase.rawQuery("select COLUMN_NAME_DATE  from " + newTablename +" group by COLUMN_NAME_DATE order by COLUMN_NAME_DATE desc LIMIT 1",null);
                        cursor.moveToLast();
                        int maxDay = cursor.getInt(0);
                        Log.i("on click","layout : maxDay :" + maxDay);
                        bundle.putInt(C_Dictionary.TRAVEL_MAX_PLAN_DAY, maxDay);
                        bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, myPlanName.get( getAdapterPosition()) );
                        Log.i("on click","layout : myPlanName.get( getAdapterPosition()) :" + myPlanName.get( getAdapterPosition()));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }


//                    Toast.makeText(v.getContext(),"click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "onClick: ViewHolder on getAdapterPosition() :"+ getAdapterPosition());
                }
            });




            getItem_image = itemView.findViewById(R.id.getCirlceImage);
//            getItem_txt = itemView.findViewById(R.id.getItem_txt);
            getParentLayout = itemView.findViewById(R.id.getSearchForParent_Layout);  // RelativeLayout 的 ID

        }
    }
}
