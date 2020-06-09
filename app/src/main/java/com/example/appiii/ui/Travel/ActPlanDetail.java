package com.example.appiii.ui.Travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Travel.Adapter.C_PlanDetailRecycleViewAdapter;
import com.example.appiii.ui.Travel.AsyncTask.C_AsyncGetPlanDetail;
import com.example.appiii.ui.Travel.AsyncTask.Interface_AsyncGetPlanDetail;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActPlanDetail extends AppCompatActivity {

    private static final String TAG = "ActPlanDetail";
    private ArrayList<Integer> getNodeDate = new ArrayList<>();
    private ArrayList<Integer> getNodeQueue = new ArrayList<>();
    private ArrayList<String> getNodeName = new ArrayList<>();
    private ArrayList<Double> getNodeLat = new ArrayList<>();
    private ArrayList<Double> getNodeLong = new ArrayList<>();
    private ArrayList<String> getNodeDescribe = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_plan_detail);
        InitialComponent();
    }

    private void InitialComponent() {
        txt_Linear_userNickName = findViewById(R.id.txt_Linear_userNickName);
        txt_Linear_userAccount = findViewById(R.id.txt_Linear_userAccount);
        txt_Linear_PlanName = findViewById(R.id.txt_Linear_PlanName);
        myHeadShot = findViewById(R.id.myHeadShot);
        bundle=getIntent().getExtras();
        Log.i(TAG, "InitialComponent: bundle size : "+ getIntent().getExtras().size());
        new C_AsyncGetPlanDetail(new Interface_AsyncGetPlanDetail(){
            @Override
            public void GetPlanDetailFinish(C_UserInfo userInfo, ArrayList<C_PlanDetail> planDetail) {
                setTitle(userInfo.getUserNickName()+"的"+userInfo.getUserPlan());
                String headImg = "http://hhlc.ddnsking.com"+userInfo.getUserHeadImg();
                Log.i(TAG, "GetPlanDetailFinish: headImg : "+ headImg);
                if(userInfo.getUserHeadImg().toString().trim().matches("")){
                }else {
                    Glide.with(ActPlanDetail.this).asBitmap().load(headImg).into(myHeadShot);
                }
                txt_Linear_userNickName.setText(userInfo.getUserAccount());
                txt_Linear_userAccount.setText(userInfo.getUserNickName());
                txt_Linear_PlanName.setText(bundle.getString(C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME));
                RecyclerView recyclerView;
                C_PlanDetailRecycleViewAdapter adapter;
                recyclerView = findViewById(R.id.plandetail_recycler);
                adapter = new C_PlanDetailRecycleViewAdapter(ActPlanDetail.this, userInfo, planDetail);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ActPlanDetail.this));
            }
        }
                /////////////////////////////////////////////////
                // How to put the ArrayList into bundle
                // https://stackoverflow.com/questions/42436012/how-to-put-the-arraylist-into-bundle
                /////////////////////////////////////////////////
                // Parcelable and Serializable
                // https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
                /////////////////////////////////////////////////
        ).execute(bundle);
        if( getNodeDate.size() > 0 || getNodeQueue.size() > 0 || getNodeName.size() > 0 || getNodeLat.size() > 0 || getNodeLong.size() > 0 || getNodeDescribe.size() > 0 ){
            getNodeDate.clear(); getNodeQueue.clear(); getNodeName.clear(); getNodeLat.clear(); getNodeLong.clear(); getNodeDescribe.clear();
        }
//        int getDays = bundle.getInt(C_Dictionary.TABLE_SCHEMA_DATE);
//        int getQueue = bundle.getInt(C_Dictionary.TABLE_SCHEMA_QUEUE);
//        String bundle.getString(C_Dictionary.TABLE_SCHEMA_NODE_NAME);
//        bundle.getDouble(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE);
//        bundle.getDouble(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE);
//        bundle.getString(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE);
    }
    CircleImageView myHeadShot;
    TextView txt_Linear_userNickName, txt_Linear_userAccount, txt_Linear_PlanName;
    Bundle bundle;
}

