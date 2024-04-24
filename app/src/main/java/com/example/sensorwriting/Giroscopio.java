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

public class Giroscopio extends AppCompatActivity implements SensorEventListener {
    private SensorManager mgr;
    private Sensor gyro;
    public static DecimalFormat DECIMAL_FORMATTER;
    TextView text;
    static float gyroX = 0, gyroY = 0, gyroZ = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giroscopio);
        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE); //Activando Servicio
        gyro = mgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE); //Activando Giroscopio
        text = (TextView) findViewById(R.id.text);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("0.0000", symbols);
    }
    @Override
    protected void onResume() {
        mgr.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);//Iniciando sensor
        super.onResume();
    }
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener(this);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float values[] = event.values;
            gyroX = event.values[0];
            gyroY = event.values[1];
            gyroZ = event.values[2];
            ((TextView) findViewById(R.id.textView6)).setText("X: " + (DECIMAL_FORMATTER.format(gyroX)));
            ((TextView) findViewById(R.id.textView7)).setText("Y: " + (DECIMAL_FORMATTER.format(gyroY)));
            ((TextView) findViewById(R.id.textView8)).setText("Z: " + (DECIMAL_FORMATTER.format(gyroZ)));
        }
    }
}
