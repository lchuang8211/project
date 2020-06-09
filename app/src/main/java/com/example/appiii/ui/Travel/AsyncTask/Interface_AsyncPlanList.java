package com.example.appiii.ui.Travel.AsyncTask;

import android.os.Bundle;

import java.net.URL;

public interface Interface_AsyncPlanList {
//    void AsyncTaskFinish(String output);
//    void GetDBTaskFinish(int ID, String Name, String cityNumber, String address, Double Lcation_lat, Double Lcation_long, int arraysize);
//    void PlanListFinish(String UserAccount, String UserName, String PlanName, String StartDate, String EndDate); //, URL head_img
    void  PlanListFinish(String getUesrAccount,String getUserName, String getPlanName, String getStarDate, String getEndDate, String getHead_img);  //getUerName
//    void  PlanListFinish(Bundle bundle);
}
