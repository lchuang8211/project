package com.example.appiii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.widget.FrameLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActMain extends AppCompatActivity {
    private ActionBar toolbar;

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
    private View.OnClickListener btn_Gmap_click = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActMain.this,ActGoogleMaps.class);
            startActivity(intent);   //沒有回傳值得呼叫新 Activity
        }
    };
    private View.OnClickListener btn_app_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent(ActMain.this,ActBottomNav.class);
            startActivity(intent1);
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
        btn_Gmap = findViewById(R.id.btn_Gmap);
        btn_Gmap.setOnClickListener(btn_Gmap_click);
        btn_app = findViewById(R.id.btn_app);
        btn_app.setOnClickListener(btn_app_click);

    }
    Button btn_app;
    Button btn_Gmap;
    Button btn_Gps;
    Button btn_DBconect;
    BottomNavigationView Bot_NavigationView; // 導航列
    NavController navController;
    AppBarConfiguration main_appBarConfiguration;
}
