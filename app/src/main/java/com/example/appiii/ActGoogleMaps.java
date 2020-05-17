package com.example.appiii;

import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActGoogleMaps extends FragmentActivity implements OnMapReadyCallback{

    String str_Spot;
    private GoogleMap mMap;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        InitialComponent();
//        txt_getAttraction.setText(trd);
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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng myHome = new LatLng(22.9951915, 120.2325259);
//        mMap.addMarker(new MarkerOptions().position(myHome).title("myHome"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myHome,14));
    }
    private void InitialComponent() {
        btn_searchView = findViewById(R.id.btn_searchView);
        btn_searchView.setOnClickListener(btn_searchView_click);
        edTxt_Spot = findViewById(R.id.edTxt_StartSpot);
        edTxt_EndSpot = findViewById(R.id.edTxt_EndSpot);
        txt_getAttraction = findViewById(R.id.txt_getSpot);
    }
    Button btn_searchView;
    EditText edTxt_Spot, edTxt_EndSpot;
    TextView txt_getAttraction;


}
