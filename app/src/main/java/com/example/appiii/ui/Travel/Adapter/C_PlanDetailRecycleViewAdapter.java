package com.example.appiii.ui.Travel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.R;
import com.example.appiii.ui.Travel.C_PlanDetail;
import com.example.appiii.ui.Travel.C_UserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_PlanDetailRecycleViewAdapter extends RecyclerView.Adapter<C_PlanDetailRecycleViewAdapter.ViewHolder>{
    private ArrayList<Integer> AL_Date = new ArrayList<>();
//    private ArrayList<Integer> AL_UserName = new ArrayList<>();
    private ArrayList<Integer> AL_Queue = new ArrayList<>();
    private ArrayList<String> AL_Name = new ArrayList<>();
    private ArrayList<Double> AL_Lat = new ArrayList<>();
    private ArrayList<Double> AL_Long = new ArrayList<>();
    private ArrayList<String> AL_Describe = new ArrayList<>();

    private C_UserInfo userInfos;
    private ArrayList<C_PlanDetail> planDetails = new ArrayList<>();

//    private ArrayList<URL> AL_HeadImgURL = new ArrayList<>();
    private String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用

    private Context mContext;

    private static final  String TAG = "TravelPlanList - RecyclerViewAdapter ";
//    URL url = new URL("http://hhlc.ddnsking.com/");


    public C_PlanDetailRecycleViewAdapter(Context context, C_UserInfo userInfos, ArrayList<C_PlanDetail> planDetails   ) {
//        this.AL_Date = AL_Date;
//        this.AL_UserName = AL_UserName;
//        this.AL_Queue = AL_Queue;
//        this.AL_Name = AL_Name;
//        this.AL_Lat = AL_Lat;
//        this.AL_Long = AL_Long;
//        this.AL_Describe = AL_Describe;
        this.userInfos = userInfos;
        this.planDetails = planDetails;
        this.mContext = context;

    }

    ViewHolder holder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_travel_plandetail, parent, false);  //嵌入 RecycleView 的 list item XML
        holder = new ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {  // part 2 : 複製 RecyclerView 的 XML && 定義 XML 的設定
        Log.i(TAG, "onBindViewHolder: called");
        holder.setIsRecyclable(false);
        Log.i(TAG, "onBindViewHolder: planDetails NodeName : " + planDetails.get(position).getNodeName());
        holder.txt_NodeDate_Queue.setText("第 "+planDetails.get(position).getNodeDate()+" 天，第 "+planDetails.get(position).getNodeQueue()+" 個行程");
        holder.txt_NodeName.setText("目的地名稱 : "+planDetails.get(position).getNodeName());
        holder.txt_NodeDescribe.setText("簡介 : \n"+planDetails.get(position).getNodeDescribe());
//        holder.txt_userName.setText(AL_UserName.get(position));uri
    }

    @Override
    public int getItemCount() { // part 3 :
        return planDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用

        TextView txt_NodeDate_Queue;
        TextView txt_NodeName;
        TextView txt_NodeDescribe;
        LinearLayout planlist_Layout;

        public ViewHolder(@NonNull View itemView) {   // 設置 item onclisk 定義 UI的動作
            super(itemView);
            planlist_Layout = itemView.findViewById(R.id.planlist_Layout);

            txt_NodeDate_Queue = itemView.findViewById(R.id.txt_NodeDate_Queue);
            txt_NodeName = itemView.findViewById(R.id.txt_NodeName);
            txt_NodeDescribe = itemView.findViewById(R.id.txt_NodeDescribe);

            Log.i(TAG, "ViewHolder: planDetails.size() : "+planDetails.size());
//            planlist_Layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME, AL_Queue.get( getAdapterPosition() ));
//                    bundle.putString(C_Dictionary.USER_ACCOUNT, AL_Date.get( getAdapterPosition() ));
//                    Intent itent = new Intent(mContext, ActPlanDetail.class);
//                    itent.putExtras(bundle);
//                    mContext.startActivity(itent);
//                    /////////////////////////////////////////////////
////                    new C_AsyncGetPlanDetail(
//                            /////////////////////////////////////////////////
//                            // How to put the ArrayList into bundle
//                            // https://stackoverflow.com/questions/42436012/how-to-put-the-arraylist-into-bundle
//                            /////////////////////////////////////////////////
//                            // Parcelable and Serializable
//                            // https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
////                    ).execute(bundle);
//                    /////////////////////////////////////////////////
//                }
//            });

        }
    }
}
