package com.example.appiii.ui.Travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.appiii.C_Dictionary;
import com.example.appiii.R;

import java.util.ArrayList;

public class ActPlanDetail extends AppCompatActivity {

    private static final String TAG = "ActPlanDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_plan_detail);
        InitialComponent();
    }

    private void InitialComponent() {
        bundle=getIntent().getExtras();
        Log.i(TAG, "InitialComponent: bundle size : "+ getIntent().getExtras().size());
//        int getDays = bundle.getInt(C_Dictionary.TABLE_SCHEMA_DATE);
//        int getQueue = bundle.getInt(C_Dictionary.TABLE_SCHEMA_QUEUE);
//        String bundle.getString(C_Dictionary.TABLE_SCHEMA_NODE_NAME);
//        bundle.getDouble(C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE);
//        bundle.getDouble(C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE);
//        bundle.getString(C_Dictionary.TABLE_SCHEMA_NODE_DESCRIBE);
    }
    Bundle bundle;
}
