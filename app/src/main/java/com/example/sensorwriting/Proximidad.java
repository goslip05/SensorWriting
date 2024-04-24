package com.example.sensorwriting;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Proximidad extends AppCompatActivity implements SensorEventListener {
    static float pro = 0;
    SensorManager sm;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    public static DecimalFormat DECIMAL_FORMATTER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proximidad);
        sm= (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST);
        if (sensor==null)
            finish();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("0.0000", symbols);
    }
    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.values[0]<sensor.getMaximumRange()){
            getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);
            ((TextView) findViewById(R.id.txtActividad)).setText("Se detecta contacto");
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.GRAY);
            ((TextView) findViewById(R.id.txtActividad)).setText("NO hay contacto");
        }
        pro = event.values[0];
        ((TextView) findViewById(R.id.txtPro)).setText(""+ (DECIMAL_FORMATTER.format(pro)));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sensorEventListener,sensor,500*500);
        sm.registerListener(sensorEventListener, sensor, 2 * 1000 * 1000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(sensorEventListener,sensor);
    }
}
