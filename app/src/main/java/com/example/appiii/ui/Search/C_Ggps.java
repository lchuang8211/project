package com.example.appiii.ui.Search;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.appiii.ActGps;
import com.google.android.gms.location.LocationListener;

  //extends AsyncTask<Void, Void, Void>
public class C_Ggps  implements LocationListener {
    private double currentLatitude;
    private double currentLongitude;
    private LocationManager mLocationManager;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 1000;
    private static final int LOCATION_UPDATE_MIN_TIME = 50;
    private Context context;

    C_Ggps(Context context) {
        this.context = context;
        getCurrentLocation(context);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    private void getCurrentLocation(Context context) {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            // location_provider error
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, (android.location.LocationListener) C_Ggps.this);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, (android.location.LocationListener) C_Ggps.this);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("location",String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
        }
    }

        public void onLocationChanged(Location location) {
            if (location != null) {
                String msg = String.format("%f, %f", location.getLatitude(), location.getLongitude());
                 currentLongitude = (double) (location.getLongitude());/// * 1E6);
                 currentLatitude = (double) (location.getLatitude());// * 1E6);
//                Toast.makeText(getApplicationContext(), msg, 100).show();

            } else {
                // Logger.d("Location is null");
//                Toast.makeText(getApplicationContext(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        }

    public double getLatitude() {
        return currentLatitude;
    }

    public double getLongitude() {
        return currentLongitude;
    }



}
