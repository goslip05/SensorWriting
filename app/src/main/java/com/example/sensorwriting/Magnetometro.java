package com.example.sensorwriting;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Magnetometro extends AppCompatActivity implements SensorEventListener{
    private TextView value;
    private SensorManager sensorManager;
    public static DecimalFormat DECIMAL_FORMATTER;
    static float magX = 0, magY = 0, magZ = 0;
    private Sensor magne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magnetometro);
        value = (TextView) findViewById(R.id.value);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("0.0000", symbols);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE); //Activando Servicio
        magne = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); //Activando Giroscopio
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magne, SensorManager.SENSOR_DELAY_NORMAL);//Iniciando sensor
}
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magX = event.values[0];
            magY = event.values[1];
            magZ = event.values[2];
            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
            value.setText(DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");
            ((TextView) findViewById(R.id.textViewX)).setText("X: " + (DECIMAL_FORMATTER.format(magX)));
            ((TextView) findViewById(R.id.textViewY)).setText("Y: " + (DECIMAL_FORMATTER.format(magY)));
            ((TextView) findViewById(R.id.textViewZ)).setText("Z: " + (DECIMAL_FORMATTER.format(magZ)));
        }
    }
}