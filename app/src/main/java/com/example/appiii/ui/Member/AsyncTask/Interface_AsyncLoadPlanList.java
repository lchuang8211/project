package com.example.appiii.ui.Member.AsyncTask;

import com.example.appiii.ui.Member.C_LoadPlanList;

import java.util.ArrayList;

public interface Interface_AsyncLoadPlanList {
//    void AsyncTaskFinish(String output);
//    void GetDBTaskFinish(int ID, String Name, String cityNumber, String address, Double Lcation_lat, Double Lcation_long, int arraysize);
//    void PlanListFinish(String UserAccount, String UserName, String PlanName, String StartDate, String EndDate); //, URL head_img
    void  LoadPlanListFinish(ArrayList<C_LoadPlanList> C_LoadPlanList);  //getUerName
//    void  PlanListFinish(Bundle bundle);
}
