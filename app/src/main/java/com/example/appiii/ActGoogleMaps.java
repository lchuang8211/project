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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

public class ActGoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    String str_Attraction;
    private GoogleMap mMap;


    private View.OnClickListener btn_searchView_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
//                C_dbconectTask dbconectTask = new C_dbconectTask();
//                Log.i("JSON","建立異步任務"+dbconectTask);
                str_Attraction = edTxt_Attraction.getText().toString();
//                Log.i("JSON","str_Attraction:" + str_Attraction);
//                dbconectTask.execute(str_Attraction);
                //
                new C_dbconectTask(new Interface_AsyncDBTask(){
                    @Override
                    public void AsyncTaskFinish(String output) {
                        txt_getAttraction.setText(output);
                    }
                }).execute(str_Attraction);
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
        LatLng myHome = new LatLng(22.9951915, 120.2325259);
        mMap.addMarker(new MarkerOptions().position(myHome).title("myHome"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myHome,14));
    }
    private void InitialComponent() {
        btn_searchView = findViewById(R.id.btn_searchView);

        btn_searchView.setOnClickListener(btn_searchView_click);
        edTxt_Attraction = findViewById(R.id.edTxt_Attraction);
        txt_getAttraction = findViewById(R.id.txt_getAttraction);
    }
    Button btn_searchView;
    EditText edTxt_Attraction;
    TextView txt_getAttraction;


}
