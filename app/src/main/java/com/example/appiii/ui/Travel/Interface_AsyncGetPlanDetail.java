package com.example.appiii.ui.Travel;

import android.os.Bundle;

import java.util.ArrayList;

public interface Interface_AsyncGetPlanDetail {
// COLUMN_NAME_DATE
// COLUMN_NAME_QUEUE
// TABLE_SCHEMA_NODE_NAME
// TABLE_SCHEMA_NODE_LATITUDE
// TABLE_SCHEMA_NODE_LONGITUDE
// TABLE_SCHEMA_NODE_DESCRIBE`
//    void  GetPlanDetailFinish(int COLUMN_NAME_DATE, int COLUMN_NAME_QUEUE, String TABLE_SCHEMA_NODE_NAME, Double TABLE_SCHEMA_NODE_LATITUDE ,Double TABLE_SCHEMA_NODE_LONGITUDE, String TABLE_SCHEMA_NODE_DESCRIBE);  //getUerName
    void  GetPlanDetailFinish( C_UserInfo userInfo, ArrayList<C_PlanDetail> planDetail);
}
