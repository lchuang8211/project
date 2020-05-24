package com.example.appiii;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appiii.ui.Member.FrgmMember;
import com.example.appiii.ui.Gmap.FrgGmap;
import com.example.appiii.ui.Hot.FrgHot;
import com.example.appiii.ui.Search.FrgSearch;
import com.example.appiii.ui.Travel.FrgTravel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ActBottomNav extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener bottimNavigation_Listener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment myfragmentselected = null;

            Bundle bundle_bottomTofrg = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle(R.string.hotspot);
                    if (fragment_home.isAdded()) {
                        getSupportFragmentManager().beginTransaction().hide(fragment_search).hide(fragment_travel).hide(fragment_gmap).hide(fragment_member)
                                .show(fragment_home).commit();
                    }
                    break;
                case R.id.navigation_search:
                    setTitle(R.string.search);
                    if (fragment_search.isAdded()){
                        getSupportFragmentManager().beginTransaction().hide(fragment_home).hide(fragment_travel).hide(fragment_gmap).hide(fragment_member)
                                .show(fragment_search).commit();
                    }
                    break;
                case R.id.navigation_travel:
                    setTitle(R.string.travel);
                    if (fragment_travel.isAdded()){
                        getSupportFragmentManager().beginTransaction().hide(fragment_home) .hide(fragment_search).hide(fragment_gmap).hide(fragment_member)
                                .show(fragment_travel).commit();
                    }
                    break;
                case R.id.navigation_gmap:
                    setTitle(R.string.gmap);
                    if (fragment_gmap.isAdded()){
                        getSupportFragmentManager().beginTransaction().hide(fragment_home).hide(fragment_search).hide(fragment_travel).hide(fragment_member)
                                .show(fragment_gmap).commit();
                    }
                    break;
                case R.id.navigation_member:
                    setTitle(R.string.member);
                    if (fragment_member.isAdded()){
                        getSupportFragmentManager().beginTransaction().hide(fragment_home).hide(fragment_search).hide(fragment_travel).hide(fragment_gmap)
                                .show(fragment_member).commit();
                    }
                    break;
            }
            return true;
        }
    };

    Bundle bundle,bot_bundle;
    String getUserStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bottom_nav);

        bundle = this.getIntent().getExtras();  //取得資料
        bot_bundle = new Bundle();
        getUserStatus = bundle.getString(C_Dictionary.USER_STATUS);
        bot_bundle.putString(C_Dictionary.USER_STATUS,getUserStatus);
        Log.i("bot_bundle","bot_bundle tostring :"+bot_bundle.toString());
//        System.out.println(R.string.hotspot);
        setTitle(R.string.hotspot);
        checkMyPermission();
        if( getUserStatus == C_Dictionary.USER_STATUS_VISITORS){
//            Toast toast = Toast.makeText(getApplicationContext(), "訪客登入成功", Toast.LENGTH_LONG);
//            toast.show();
        }
        InitialComponent();
    }
    static String USER;
    private void InitialComponent() {

        bottimNavigation = findViewById(R.id.bottom_Navigation);     //new 底部導航
        bottimNavigation.setOnNavigationItemSelectedListener(bottimNavigation_Listener);  //監聽底部導航
        fragment_home = new FrgHot();  //首頁
        fragment_search = new FrgSearch();
        fragment_travel = new FrgTravel();
        fragment_gmap = new FrgGmap();
        fragment_member = new FrgmMember();

        fragment_home.setArguments(bot_bundle);
        fragment_search.setArguments(bot_bundle);
        fragment_travel.setArguments(bot_bundle);
        fragment_gmap.setArguments(bot_bundle);
        fragment_member.setArguments(bot_bundle);
        //* 第一次建立畫面 *//
        getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_host_fragment, fragment_search)
                .add(R.id.nav_host_fragment, fragment_travel)
                .add(R.id.nav_host_fragment, fragment_member)
                .add(R.id.nav_host_fragment, fragment_gmap)
                .add(R.id.nav_host_fragment, fragment_home)
                .commit();
        //* 第一次建立畫面 *//
    }
    private void checkMyPermission() {
        //取得現在的權限狀態
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        //如果沒有權限則請求權限
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);  //識別碼自訂
    }
    BottomNavigationView bottimNavigation;
    Fragment fragment_home, fragment_search, fragment_travel, fragment_gmap, fragment_member;
}
