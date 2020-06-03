package com.example.appiii;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActGoogleMaps extends FragmentActivity implements OnMapReadyCallback{

    private static final String TAG = "ActGoogleMaps";
    String str_Spot;
    private GoogleMap mMap;
    private double currentLatitude;
    private double currentLongitude;
    private LocationManager mLocationManager;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 5000;
    private static final int LOCATION_UPDATE_MIN_TIME = 50;
    private Location location;
    Double My_LATITUDE;
    Double My_LONGITUDE;

    private void getCurrentLocation() {

        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Log.i("location", "GPS NET都沒有");
            return;
        } else {
            if (isNetworkEnabled) {
                Log.i("location", "NET 定位");
                if (ActivityCompat.checkSelfPermission(ActGoogleMaps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActGoogleMaps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_DISTANCE, LOCATION_UPDATE_MIN_TIME, locationListener);  //更新GPS頻率
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("location", "NET 定位1" + location);
            }
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(ActGoogleMaps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActGoogleMaps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Log.i("location", "GPS 定位");
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_DISTANCE, LOCATION_UPDATE_MIN_TIME, locationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        updateLocation(location);
    }

    // 位置監聽器
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
            My_LONGITUDE= location.getLatitude();
            My_LATITUDE=location.getLongitude();
            Log.i("location", "成功取得經緯度");
        } else {
            Log.i("location", "沒有獲取到定位物件Location");
        }
    }

    public double getLatitude() {
        return currentLatitude;
    }

    public double getLongitude() {
        return currentLongitude;
    }


    private View.OnClickListener btn_searchView_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
//                C_dbconectTask dbconectTask = new C_dbconectTask();
//                Log.i("JSON","建立異步任務"+dbconectTask);
                str_Spot = edTxt_Spot.getText().toString();
//                Log.i("JSON","str_Attraction:" + str_Attraction);
//                dbconectTask.execute(str_Attraction);
                //
                new C_dbconectTask(new Interface_AsyncDBTask() {
                    @Override
                    public void AsyncTaskFinish(String Spot, Double Lat_output, Double Long_output) {    //Double output1, Double output2 String output
                        txt_getAttraction.setText(String.valueOf(Lat_output) +", "+ String.valueOf(Long_output));
//                        txt_getAttraction.setText(output);
                        LatLng SearchAttraction = new LatLng(Lat_output, Long_output);
                        mMap.addMarker(new MarkerOptions().position(SearchAttraction).title(Spot));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SearchAttraction,14));
                    }
                }).execute(str_Spot);
                Log.i("JSON","執行異步任務");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();   // ACTIVITY 的 getIntent
        Log.i(TAG, "onCreate: bundle :" + bundle);
        setContentView(R.layout.act_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        InitialComponent();
        myCriteria();
        checkMyPermission();
        getCurrentLocation();
    }

    private void queryMap() {

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    String SpotName;
    Double LOCATION_LATITUDE;
    Double LOCATION_LONGITUDE;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings ui_set = mMap.getUiSettings();
        ui_set.setZoomControlsEnabled(true);
        ui_set.setCompassEnabled(true);
        LatLng My_Location = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(My_Location).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(My_Location,14));
        if(bundle!=null){
             LOCATION_LATITUDE = bundle.getDouble(C_Dictionary.LOCATION_LATITUDE);
             LOCATION_LONGITUDE = bundle.getDouble(C_Dictionary.LOCATION_LONGITUDE);
            SpotName = bundle.getString(C_Dictionary.SPOT_NAME);
            Log.i(TAG, "onCreate: ActGoogleMaps bundle :"+ LOCATION_LONGITUDE + "," +LOCATION_LATITUDE);
            LatLng SearchAttraction = new LatLng(LOCATION_LATITUDE, LOCATION_LONGITUDE);
            mMap.addMarker(new MarkerOptions().position(SearchAttraction).title(SpotName).snippet(SpotName) );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SearchAttraction,12));
        }
    }
    private void InitialComponent() {
        txt_getAttraction = findViewById(R.id.txt_getSpot);
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
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }
    private void checkMyPermission() {
        //取得現在的權限狀態
        int permissionCheck = ContextCompat.checkSelfPermission(ActGoogleMaps.this, Manifest.permission.ACCESS_FINE_LOCATION);
        //如果沒有權限則請求權限
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(ActGoogleMaps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);  //識別碼自訂
    }
    Button btn_searchView;
    EditText edTxt_Spot, edTxt_EndSpot;
    TextView txt_getAttraction;


}
