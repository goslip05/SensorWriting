package com.example.sensorwriting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Sensores extends AppCompatActivity {
    Button buttonListarA, buttonListarB, buttonListarC, buttonListarM, buttonListarG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensores);
        buttonListarA=(Button)findViewById(R.id.btn1);
        buttonListarB=(Button)findViewById(R.id.btn2);
        buttonListarC=(Button)findViewById(R.id.btn3);
        buttonListarM=(Button)findViewById(R.id.btn4);
        buttonListarG=(Button)findViewById(R.id.btn5);
        buttonListarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatosProximidad(v);
            }
        });
        buttonListarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatosAcelerometro(v);
            }
        });
        buttonListarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatosGiroscopio(v);
            }
        });
        buttonListarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatosMagnetometro(v);
            }
        });
        buttonListarG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatosGps(v);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            buttonListarG.setVisibility(View.GONE);
            Toast.makeText(Sensores.this, "No es posible registrar GPS", Toast.LENGTH_SHORT).show(); //Imprime Toast indicando ya se Inicio la lectura
        }
    }
    public void mostrarDatosProximidad(View v){
        Intent intent= new Intent(this, Proximidad.class);
        startActivity(intent);
    }
    public void mostrarDatosAcelerometro(View v){
        Intent intent= new Intent(this, Acelerometro.class);
        startActivity(intent);
    }
    public void mostrarDatosGiroscopio(View v){
        Intent intent= new Intent(this, Giroscopio.class);
        startActivity(intent);
    }
    public void mostrarDatosMagnetometro(View v){
        Intent intent= new Intent(this, Magnetometro.class);
        startActivity(intent);
    }
    public void mostrarDatosGps(View v){
        Intent intent= new Intent(this, Gps.class);
        startActivity(intent);
    }
}
