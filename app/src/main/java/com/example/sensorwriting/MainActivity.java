package com.example.sensorwriting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    Button buttonListarS,buttonListarSel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        }
        buttonListarS=(Button)findViewById(R.id.btnS);
        buttonListarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatosSensores(v);
            }
        });
        buttonListarSel=(Button)findViewById(R.id.btnSel);
        buttonListarSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarSensores(v);
            }
        });
    }
    public void mostrarDatosSensores(View v) {
            Intent intent = new Intent(this, Sensores.class);
            startActivity(intent);
        }
    public void seleccionarSensores(View v) {
            Intent intent = new Intent(this, Seleccion.class);
            startActivity(intent);
    }
}
