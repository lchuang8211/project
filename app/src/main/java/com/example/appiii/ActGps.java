package com.example.appiii;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebSettings;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ActGps extends Activity {



    private LocationManager locationmanager;
    private Location location;
    private int MIN_TIME_BW_UPDATES =5000, MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 判斷GPS更新的最小時間建閣與忽略距離
    private Criteria criteria;
    private String myLocation_horizontal,myLocation_latitude ;  //記錄我的裝置的GPS位置

    ////  google map(先緯, 後經) 表示
    private View.OnClickListener btn_101_location_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) { //101經緯 (25.033493, 121.564101)
            webView.loadUrl("https://www.google.com.tw/maps/dir/"+myLocation_horizontal+","+myLocation_latitude+"/" + 25.033493  +","+ 121.564101);
        }};
    private View.OnClickListener btn_85_location_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) { //85經緯 (22.611666666667 ,120.3)
            webView.loadUrl("https://www.google.com.tw/maps/dir/"+myLocation_horizontal+","+myLocation_latitude+"/" + 22.611666666667  +","+ 120.3);
        }};
    private View.OnClickListener btn_tower_location_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) { //額堧鼻(21.901880, 120.851948
            webView.loadUrl("https://www.google.com.tw/maps/dir/"+myLocation_horizontal+","+myLocation_latitude+"/" + 21.901880  +","+ 120.851948);
        }};

    private View.OnClickListener btn_sample_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {//成大(23.002067, 120.220185)   急診(24.180126, 120.648465)
            webView.loadUrl("https://www.google.com.tw/maps/dir/22.996783,120.218093/" + 23.002067  +","+ 120.220185);  //成大學校 -> 成大急診
        }
    };
    private View.OnClickListener btn_go_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) { //撈資料庫的經緯度去取代 locationhorizontal / locationlatitude
//            String locationlatitude = latitude.getText().toString();   //  景點去資料庫搜尋，撈同名的資料(LIKE %XXX% or = xxx)進一步選擇，再取他的經緯度，帶入運算
//            String locationhorizontal = horizontal.getText().toString();
            String getdes_location = des_location.getText().toString();
            webView.loadUrl("https://www.google.com.tw/maps/dir/"+myLocation_horizontal+","+myLocation_latitude+"/" + getdes_location);


        }
    };
    private View.OnClickListener btn_location_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Gmap.setText("我的經度：" + location.getLongitude() + " 緯度："+ location.getLatitude());
        }
    };
    // 位置監聽器





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gps);
        checkMyPermission();
        InitialComponent();
        getMyGPS_Location();
    }

    private void getMyGPS_Location() {
        //請求GPS位置提供者 NET OR GPS
        boolean isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Log.i("location", "GPS NET都沒有");
            return;
        } else {
            if (isNetworkEnabled) {
                Log.i("location", "NET 定位");
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION},0);
                }
                locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);  //更新GPS頻率
                location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("location", "NET 定位1" + location);
            }
            if (isGPSEnabled) {
                if (checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},0);
                }
                Log.i("location", "GPS 定位");
                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }

        updateLocation(location);
    }



    LocationListener locationListener = new LocationListener() {
        // 當位置改變時觸發
        @Override
        public void onLocationChanged(Location location) {
            Log.i("location", location.toString());
            updateLocation(location);
        }
        // Provider失效時觸發
        @Override
        public void onProviderDisabled(String arg0) {
            Log.i("location", arg0);
        }
        // Provider可用時觸發
        @Override
        public void onProviderEnabled(String arg0) {
            Log.i("location", arg0);
        }
        // Provider狀態改變時觸發
        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            Log.i("location", "onStatusChanged");
        }
    };

    private void updateLocation(Location location) {
        Log.i("location", "updateLocation + text");
        if (location != null) {
            myLocation_horizontal= String.valueOf(location.getLatitude());
            myLocation_latitude= String.valueOf(location.getLongitude());
            Log.i("location", "成功取得經緯度");
        } else {
            Log.i("location", "沒有獲取到定位物件Location");
        }
    }






    private void checkMyPermission() {
        //取得現在的權限狀態
        int permissionCheck = ContextCompat.checkSelfPermission(ActGps.this, Manifest.permission.ACCESS_FINE_LOCATION);
        //如果沒有權限則請求權限
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(ActGps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);  //識別碼自訂
    }


    private void InitialComponent() {
        myCriteria();
        webView_all();
        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(btn_location_click);
        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(btn_go_click);
        btn_sample = findViewById(R.id.btn_sample);
        btn_sample.setOnClickListener(btn_sample_click);
        btn_101_location = findViewById(R.id.btn_101_location);
        btn_101_location.setOnClickListener(btn_101_location_click);
        btn_85_location = findViewById(R.id.btn_85_location);
        btn_85_location.setOnClickListener(btn_85_location_click);
        btn_tower_location = findViewById(R.id.btn_tower_location);
        btn_tower_location.setOnClickListener(btn_tower_location_click);
        des_location = findViewById(R.id.des_location);
//                    latitude = findViewById(R.id.longitude);
//            horizontal = findViewById(R.id.horizontal);
        textlatitude = findViewById(R.id.textlongitude);
//            texthorizontal = findViewById(R.id.texthorizontal);
        Gmap = findViewById(R.id.Gmap);
    }
    private void myCriteria() {  // 精確度函式
        // 定義Criteria物件
        Criteria criteria = new Criteria();
        // 設定定位精確度 Criteria.ACCURACY_COARSE 比較粗略， Criteria.ACCURACY_FINE則比較精細
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 設定是否需要海拔資訊 Altitude
        criteria.setAltitudeRequired(true);
        // 設定是否需要方位資訊 Bearing
        criteria.setBearingRequired(true);
        // 設定對電源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //獲取LocationManager物件
        locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }
    private void webView_all() {
        webView = findViewById(R.id.webview);
        webView.loadUrl("https://www.google.com.tw/maps");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // 取得網頁JS效果
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setBuiltInZoomControls(true);       //是否支持手指縮放
    }

    EditText latitude, horizontal;
    WebView webView;
    Button btn_go, btn_sample, btn_location, btn_101_location, btn_85_location, btn_tower_location;
    TextView textlatitude, texthorizontal, Gmap,des_location;
}
