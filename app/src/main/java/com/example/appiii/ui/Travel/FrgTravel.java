package com.example.appiii.ui.Travel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;
import com.example.appiii.ui.Member.ActAddTravelPlan;
import com.example.appiii.ui.Member.C_MemberRecycleViewAdapter;
import com.example.appiii.ui.Member.C_MySQLite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FrgTravel extends Fragment {
    View inflatedView;
    private ArrayList<String> AL_UserAccount = new ArrayList<>();
    private ArrayList<String> AL_UserName = new ArrayList<>();
    private ArrayList<String> AL_PlanName = new ArrayList<>();
    private ArrayList<String> AL_StartDate = new ArrayList<>();
    private ArrayList<String> AL_EndDate = new ArrayList<>();
    private ArrayList<URL> AL_HandImgURL = new ArrayList<>();
    private View.OnClickListener btn_addPlan_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("Search_requirement",null);
            new C_AsyncTravelPlanList(new Interface_AsyncPlanList() {
                @Override
                public void PlanListFinish(String UserAccount, String UserName, String PlanName, String StartDate, String EndDate) {  // URL head_img
                    AL_UserAccount.add(UserAccount);
                    AL_UserName.add(UserName);
                    AL_PlanName.add(PlanName);
                    AL_StartDate.add(StartDate);
                    AL_EndDate.add(EndDate);
//                    AL_HandImgURL.add(head_img);
                    InitialRecycler();
                }
            }).execute(bundle);
            if (AL_UserAccount.size()>0 || AL_UserName.size()>0  || AL_PlanName.size()>0  || AL_StartDate.size()>0  || AL_EndDate.size()>0 ){   // 如果有上一筆資料 即刪除
                AL_UserAccount.clear();
                AL_UserName.clear();
                AL_PlanName.clear();
                AL_StartDate.clear();
                AL_EndDate.clear();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_travel,container,false);
        InitialComponent();
        InitialRecycler();
        return inflatedView;
    }



Button btn_addPlan;

//  TRAVEL_LIST_SCHEMA_PLAN_NAME
    private void InitialComponent() {
        btn_addPlan = inflatedView.findViewById(R.id.btn_addPlan);
        btn_addPlan.setOnClickListener(btn_addPlan_click);
        Bundle bundle = new Bundle();
        bundle.putString("Search_requirement",null);
        new C_AsyncTravelPlanList(new Interface_AsyncPlanList() {
            @Override
            public void PlanListFinish(String UserAccount, String UserName, String PlanName, String StartDate, String EndDate) {
                AL_UserAccount.add(UserAccount);
                AL_UserName.add(UserName);
                AL_PlanName.add(PlanName);
                AL_StartDate.add(StartDate);
                AL_EndDate.add(EndDate);
                InitialRecycler();
            }
        }).execute(bundle);
        if (AL_UserAccount.size()>0 || AL_UserName.size()>0  || AL_PlanName.size()>0  || AL_StartDate.size()>0  || AL_EndDate.size()>0 ){   // 如果有上一筆資料 即刪除
            AL_UserAccount.clear();
            AL_UserName.clear();
            AL_PlanName.clear();
            AL_StartDate.clear();
            AL_EndDate.clear();
        }
    }

    RecyclerView recyclerView;
    C_TravelPlanListRecycleViewAdapter adapter;
    private void InitialRecycler() {
        Log.i(TAG, "InitialRecycler: size :  " + AL_UserAccount.size());
        recyclerView = inflatedView.findViewById(R.id.travel_recycler_layout_planlist);
        adapter = new C_TravelPlanListRecycleViewAdapter(getActivity(), AL_UserAccount, AL_UserName, AL_PlanName, AL_StartDate, AL_EndDate);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)

    }

}
