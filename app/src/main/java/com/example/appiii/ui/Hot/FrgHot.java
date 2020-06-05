package com.example.appiii.ui.Hot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appiii.C_Dictionary;
import com.example.appiii.C_GetDataFromDatabase;
import com.example.appiii.Interface_AsyncGetDBTask;
import com.example.appiii.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FrgHot extends Fragment {

    View inflatedView;
    private LocationManager lm;
    private Location location;
    private int MIN_TIME_BW_UPDATES =5000, MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 判斷GPS更新的最小時間建閣與忽略距離
    private String myLocation_horizontal,myLocation_latitude ;  //記錄我的裝置的GPS位置
    private ArrayList<String> database_Name = new ArrayList<>();
    private ArrayList<String> database_address = new ArrayList<>();
    private ArrayList<String> mySpotToldescribe = new ArrayList<>();
    private ArrayList<Double> database_lat = new ArrayList<>();
    private ArrayList<Double> database_long = new ArrayList<>();
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.frg_hot,container,false);
        bundle = this.getArguments();  // get bundle in fragment;
        InitialComponent();
        return inflatedView;
    }

    private void InitialComponent() {
        InitinalSearch();
//        btn_home = inflatedView.findViewById(R.id.btn_hot);
//        txt_home = inflatedView.findViewById(R.id.txt_hotspot);
    }

    private void InitinalSearch() {
        Bundle bundleToDb = new Bundle();
        bundleToDb.putString(C_Dictionary.CITY_NAME_REQUEST,"臺南市");
        bundleToDb.putString(C_Dictionary.SPOT_TYPE_REQUEST,"1");
        new C_GetDataFromDatabase(new Interface_AsyncGetDBTask(){
            @Override
//                public void GetDBTaskFinish(int ID, String Name, String cityNumber, String address, Double Lcation_lat, Double Lcation_long, int arraysize){
            public void GetDBTaskFinish(String Name, String address, String Toldescribe ,Double Lcation_lat, Double Lcation_long){
                database_Name.add(Name.trim());
                database_address.add(address.trim());
                mySpotToldescribe.add(Toldescribe);
                database_lat.add(Lcation_lat);
                database_long.add(Lcation_long);
                InitRecyclerView();
            }
        }).execute(bundleToDb);
        if (database_Name.size()>0 || database_address.size()>0 ){   // 如果有上一筆資料 即刪除
            database_Name.clear();
            database_address.clear();
        }
    }


    private void getMyGPS_Location() {
        //請求GPS位置提供者 NET OR GPS
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Log.i("location", "GPS NET都沒有");
            return;
        } else {
            if (isNetworkEnabled) {
                Log.i("location", "NET 定位");
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);  //更新GPS頻率
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("location", "NET 定位1" + location);
            }
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Log.i("location", "GPS 定位");
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

    C_HotRecycleViewAdapter adapter;
    static RecyclerView recyclerView_hot;
    private void InitRecyclerView(){  // 資料載入後才呼叫 RecyclerView 的相關設定
        Log.i(TAG, "InitRecyclerView: init recyclerview");
        recyclerView_hot = inflatedView.findViewById(R.id.recycle_view_hot);  // 放在這個 Acticity 的 XML 下的 RecyclerView.ID  recycle_view_search
        adapter = new C_HotRecycleViewAdapter(getActivity(), database_Name, database_address, mySpotToldescribe, database_lat, database_long);  // 建立 Adapter 來載入資料  // 用 this CLASS 建立 Adapter
        recyclerView_hot.setAdapter(adapter);
        recyclerView_hot.setLayoutManager(new LinearLayoutManager(getActivity()));  // recyclerView.setLayoutManager(LayoutManager layoutManager)  // ( Context context, int orientation, boolean reverseLayout)
//        recyclerView.setOnItemClickListener();
    }
    public void CloseBtnEvent(){
        if (adapter != null) {

        }
    }
    public void OpenBtnEvent(){
        Log.i(TAG, "set hot adapter true : "+database_Name.size());

//        ((C_HotRecycleViewAdapter)recyclerView_hot.getAdapter()).setAdapterClickFlag(true);
        InitRecyclerView();
    }


    Button btn_home;
    TextView txt_home;
}
