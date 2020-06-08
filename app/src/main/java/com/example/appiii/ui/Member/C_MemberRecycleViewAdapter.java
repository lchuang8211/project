package com.example.appiii.ui.Member;

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
import com.example.appiii.C_MySQLite;
import com.example.appiii.R;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_MemberRecycleViewAdapter extends RecyclerView.Adapter<C_MemberRecycleViewAdapter.ViewHolder>{
    private ArrayList<String> myPlanName = new ArrayList<>();
    private ArrayList<String> myPlanDate = new ArrayList<>();
    private ArrayList<Integer> myPlanTotalDay = new ArrayList<>();
    private Context mContext;
    File file = new File("D:\\Appiii_project\\app\\src\\main\\res\\drawable\\tedros.png");  // 開啟本地檔案
    Uri uri = Uri.fromFile(file);  //建立超連結
    private static final  String TAG = "RecyclerViewAdapter";



    public C_MemberRecycleViewAdapter(Context context, ArrayList<String> myPlanName, ArrayList<String> myPlanDate, ArrayList<Integer> myPlanTotalDay) {
        this.myPlanName = myPlanName;
        this.myPlanDate = myPlanDate;
        this.myPlanTotalDay = myPlanTotalDay;
        this.mContext = context;
    }

    ViewHolder holder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_member_myschedule, parent, false);  //嵌入 RecycleView 的 list item XML
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

        holder.cursorForBind = holder.sqLiteDatabase.rawQuery("select "+C_Dictionary.TRAVEL_SCHEMA_TABLE_VISIBILITY+" from "+C_Dictionary.TRAVEL_LIST_Table_Name
                +" where "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+"='"+myPlanName.get(position)+"'",null);
//        Log.i(TAG, "onBindViewHolder: "+ holder.cursor.getInt(0));
        if(holder.cursorForBind.moveToNext()){
            if (holder.cursorForBind.getInt(0)==1) {
                holder.cbox_pushToCloud.setChecked(true);
            }
            else
                holder.cbox_pushToCloud.setChecked(false);
        }

//        if(cursor){
//            cbox_pushToCloud.setChecked(true);
//        }else{
//            cbox_pushToCloud.setChecked(false);
//        }

//        holder.txt_Plan_date.setText(myPlanDate.get(position) +" "+myPlanTotalDay.get(position)+" 天");
//        Glide.with(mContext).asBitmap().load( uri ).into(holder.getItem_image);  // Gilde : 圖片 library
//        holder.txt_Name_Address.setText(myPlanName.get(position));
//        Log.i(TAG, "onBindViewHolder: myPlanName.get(position): " + myPlanName.get(position));
//        Log.i(TAG, "onBindViewHolder: myPlanTotalDay.get(position): " + myPlanTotalDay.get(position));
    }

    @Override
    public int getItemCount() { // part 3 :
        return myPlanName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用


        Cursor cursor, cursorForBind;
        C_MySQLite SQLite_helper;
        SQLiteDatabase sqLiteDatabase;
        LinearLayout layout;
        CircleImageView getItem_image;
        TextView txt_Plan_date;
        TextView txt_Plan_Name;
        CheckBox cbox_pushToCloud;
        RelativeLayout getParentLayout;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor write;
        public ViewHolder(@NonNull View itemView) {   // 設置 item onclisk 定義 UI的動作
            super(itemView);
            SQLite_helper = new C_MySQLite(mContext);
            sqLiteDatabase = SQLite_helper.getReadableDatabase();
            getItem_image = itemView.findViewById(R.id.getCirlceImage);
            getParentLayout = itemView.findViewById(R.id.getReLativeLayoutID_act_myschedule);
            // RelativeLayout.XML 的 layout 的 Layout ID
            // RelativeLayout getParentLayout = itemView.findViewById(R.id.getSearchInfoForParent_Layout);
            // 把 RelativeLayout 當作 View 並用 findViewById(R.id.RelativeLayout_ID) 尋找 Layout 的 XML 排版

            txt_Plan_date = itemView.findViewById(R.id.txt_Plan_info);
            txt_Plan_Name = itemView.findViewById(R.id.txt_Spot_Name);
            cbox_pushToCloud = itemView.findViewById(R.id.cbox_pushToCloud);

            layout = (LinearLayout) itemView.findViewById(R.id.layout_showPlan);
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("刪除");
                    dialog.setMessage("確定要刪除 "+myPlanName.get( getAdapterPosition()) +" 嗎？");
                    dialog.setNeutralButton("取消",null);
                    dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLite_helper = new C_MySQLite(mContext);
                            sqLiteDatabase = SQLite_helper.getReadableDatabase();
                            String newTablename = "["+ C_Dictionary.CREATE_TABLE_HEADER + myPlanName.get( getAdapterPosition())+"]";
                            sqLiteDatabase.delete(C_Dictionary.TRAVEL_LIST_Table_Name,C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+"=?",new String[] {myPlanName.get( getAdapterPosition())});
                            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + newTablename );
                            myPlanName.remove( getAdapterPosition()) ;
                            myPlanDate.remove( getAdapterPosition());
                            myPlanTotalDay.remove( getAdapterPosition());
                            notifyItemRemoved( getAdapterPosition() );
                            notifyDataSetChanged();
                        }
                    }).create().show();  // 右邊

                    return true;
                }
            });

            layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActMemberShowTravelPlan.class);
                    Bundle bundle = new Bundle();
                    SQLite_helper = new C_MySQLite(mContext);
                    sqLiteDatabase = SQLite_helper.getReadableDatabase();

                    String newTablename = "["+ C_Dictionary.CREATE_TABLE_HEADER+myPlanName.get( getAdapterPosition())+"]";

                    cursor=sqLiteDatabase.rawQuery("select exists (select 1 from "+ newTablename +" )",null);
                    cursor.moveToLast();
                    int empty = cursor.getInt(0);
                    if (empty==0){
                        Log.i("on click","layout : empty :" + empty);
                        bundle.putBoolean(C_Dictionary.TRAVEL_PLAN_IS_EMPTY, true );
                        bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, myPlanName.get( getAdapterPosition()) );
                        Log.i("on click","layout : empty myPlanName.get( getAdapterPosition()) :" + myPlanName.get( getAdapterPosition()));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    if (empty==1){
                        Log.i("on click","layout : empty :" + empty);
                        bundle.putBoolean(C_Dictionary.TRAVEL_PLAN_IS_EMPTY, false );
                        bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, myPlanName.get( getAdapterPosition()) );
                        Log.i("on click","layout : myPlanName.get( getAdapterPosition()) :" + myPlanName.get( getAdapterPosition()));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }


                }
            });

            cbox_pushToCloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                ContentValues contentValues = new ContentValues();
                Bundle bundle ;
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SQLite_helper = new C_MySQLite(mContext);
                    sqLiteDatabase = SQLite_helper.getReadableDatabase();
                    cursor = sqLiteDatabase.rawQuery("select "+C_Dictionary.TRAVEL_SCHEMA_TABLE_VISIBILITY
                            +" from "+C_Dictionary.TRAVEL_LIST_Table_Name
                            + " where "+C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME +"=?",new String[]{myPlanName.get( getAdapterPosition())});
                    cursor.moveToFirst();
                    if (isChecked){
                        Log.i("勾","sqLiteDatabase :"+sqLiteDatabase);
                        bundle = new Bundle();
                        // 行程名稱/使用者帳號/使用者名稱
//                        sp.getBoolean("public",true);
                        bundle.putBoolean(C_Dictionary.PUBLIC_TO_CLOUND_SIGNAL,true);
                        bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, myPlanName.get( getAdapterPosition()));  //行程名稱
                        if(cursor.getInt(0)==0){
                            contentValues.put(C_Dictionary.TRAVEL_SCHEMA_TABLE_VISIBILITY,1);
                            sqLiteDatabase.update(C_Dictionary.TRAVEL_LIST_Table_Name,contentValues,C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+"=?",new String[]{ myPlanName.get( getAdapterPosition()) } );
                            new C_AsyncPublicPlan(mContext).execute(bundle);
                        }
                    }else{
                        Log.i("cbox_pushToCloud","create : 沒勾 :");
                        contentValues.put(C_Dictionary.TRAVEL_SCHEMA_TABLE_VISIBILITY,0);
                        sqLiteDatabase.update(C_Dictionary.TRAVEL_LIST_Table_Name,contentValues,C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+"=?",new String[] { myPlanName.get( getAdapterPosition()) });
                        Log.i("沒勾","sqLiteDatabase :"+sqLiteDatabase);
                        bundle = new Bundle();
                        bundle.putBoolean(C_Dictionary.PUBLIC_TO_CLOUND_SIGNAL,false);
                        bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, myPlanName.get( getAdapterPosition()));  //行程名稱
                        new C_AsyncPublicPlan(mContext).execute(bundle);
                        }

                    }

            });


        }
    }
}
