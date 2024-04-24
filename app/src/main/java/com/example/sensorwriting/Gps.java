package com.example.sensorwriting;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

public class Gps extends AppCompatActivity {
    LocationManager locationManager;
    public static float latitud=0, longitud=0;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //acceso a los servicios de localización de Android
        locationListener = new LocationListener(){//recibir los eventos de localización
            @Override
            public void onLocationChanged(Location location){
                updateLocationInfo(location);
            } //Nueva localizacion
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle){}//Cambio de estado
            @Override
            public void onProviderEnabled(String s) {} //habiliando el proveedor
            @Override
            public void onProviderDisabled(String s) {} //Deshabiliando el proveedor
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }
    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    public void updateLocationInfo(Location location) {
        latitud= (float) location.getLatitude();
        longitud= (float) location.getLongitude();
        ((TextView) findViewById(R.id.latitude)).setText("" + latitud);
        ((TextView) findViewById(R.id.longitud)).setText("" + longitud);
    }
}