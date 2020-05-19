package com.example.appiii.ui.Gmap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appiii.C_dbconectTask;
import com.example.appiii.Interface_AsyncDBTask;
import com.example.appiii.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FrgGmap extends Fragment implements OnMapReadyCallback {
    String str_Spot;
    View inflatedView_Gmap;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView_Gmap = inflater.inflate(R.layout.frg_gmap,container,false);
//        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mMapFragment.getMapAsync(this);

//        mapFragment.getMapAsync(this);
        InitialComponent();
        return inflatedView_Gmap;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void InitialComponent() {
        btn_searchView = inflatedView_Gmap.findViewById(R.id.btn_searchView);
        btn_searchView.setOnClickListener(btn_searchView_click);
        edTxt_Spot = inflatedView_Gmap.findViewById(R.id.edTxt_StartSpot);
        edTxt_EndSpot = inflatedView_Gmap.findViewById(R.id.edTxt_EndSpot);
        txt_getAttraction = inflatedView_Gmap.findViewById(R.id.txt_getSpot);
    }
    Button btn_searchView;
    EditText edTxt_Spot, edTxt_EndSpot;
    TextView txt_getAttraction;
}
