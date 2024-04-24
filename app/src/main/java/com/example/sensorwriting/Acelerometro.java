package com.example.sensorwriting;

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

public class Acelerometro extends AppCompatActivity implements SensorEventListener {
    static float curX=0, curY=0,curZ=0;
    private SensorManager mSensor;
    public static DecimalFormat DECIMAL_FORMATTER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acelerometro);
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("0.0000", symbols);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float values[] = event.values;
            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];
            ((TextView) findViewById(R.id.txtAccX)).setText("X: " + (DECIMAL_FORMATTER.format(curX)));
            ((TextView) findViewById(R.id.txtAccY)).setText("Y: " + (DECIMAL_FORMATTER.format(curY)));
            ((TextView) findViewById(R.id.txtAccZ)).setText("Z: " + (DECIMAL_FORMATTER.format(curZ)));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensor.unregisterListener(this);
    }
}