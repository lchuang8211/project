package com.example.appiii;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appiii.ui.Member.FrgmMember;
import com.example.appiii.ui.Gmap.FrgGmap;
import com.example.appiii.ui.Hot.FrgHot;
import com.example.appiii.ui.Search.FrgSearch;
import com.example.appiii.ui.Travel.FrgTravel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ActBottomNav extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener bottimNavigation_Listener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment myfragmentselected = null;
            switch (item.getItemId()){
                case R.id.navigation_hotspot:
                    myfragmentselected = new FrgHot();
                    break;
                case R.id.navigation_search:
                    myfragmentselected = new FrgSearch();
                    break;
                case R.id.navigation_travel:
                    myfragmentselected = new FrgTravel();
                    break;
                case R.id.navigation_gmap:
                    myfragmentselected = new FrgGmap();
                    break;
                case R.id.navigation_member:
                    myfragmentselected = new FrgmMember();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myfragmentselected ).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bottom_nav);
        InitialComponent();
    }

    private void InitialComponent() {
        bottimNavigation = findViewById(R.id.bottom_Navigation);     //new 底部導航
        bottimNavigation.setOnNavigationItemSelectedListener(bottimNavigation_Listener);  //監聽底部導航
    }
    BottomNavigationView bottimNavigation;
}
