package com.example.appiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActMain extends AppCompatActivity {

    private View.OnClickListener btn_Gps_click= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActMain.this,ActGps.class);
            startActivity(intent);   //沒有回傳值得呼叫新 Activity
        }
    };
    private View.OnClickListener btn_DBconect_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActMain.this,ActDBconect.class);
            startActivity(intent);   //沒有回傳值得呼叫新 Activity
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialComponent();
    }

    private void InitialComponent() {
        btn_Gps = findViewById(R.id.btn_Gps);
        btn_Gps.setOnClickListener(btn_Gps_click);
        btn_DBconect = findViewById(R.id.btn_DBconect);
        btn_DBconect.setOnClickListener(btn_DBconect_click);
    }


    Button btn_Gps;
    Button btn_DBconect;
}
