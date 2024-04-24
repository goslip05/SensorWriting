package com.example.sensorwriting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.sensorwriting.Seleccion.battery;
import static com.example.sensorwriting.Seleccion.cheAce;
import static com.example.sensorwriting.Seleccion.cheGir;
import static com.example.sensorwriting.Seleccion.cheGps;
import static com.example.sensorwriting.Seleccion.cheMag;
import static com.example.sensorwriting.Seleccion.chePro;


public class Seleccion extends AppCompatActivity {
    private static LectorDeSensores2 lectorDeSensores;
    EditText valSeg;
    String textoRuta;
    TextView text;
    LocationListener locationListener;
    LocationManager locationManager;
    static double battery;
    private SensorManager mSensorManager;
    private Sensor acelerometro, giroscopio, magnetometro, proximidad;
    Button buttonSTART,buttonSTOP;
    static float curX,curY,curZ,gyroX,gyroY,gyroZ,magX,magY,magZ,pro=0, latitud, longitud;
    public static CheckBox cheAce, cheGir, cheMag, chePro, cheGps;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_registro);
        valSeg=(EditText)findViewById(R.id.segu); //Campo de segundos
        text=(TextView)findViewById(R.id.textView3); //Texto de intervalo
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); //Inicio Recolección de datos para bateria
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        battery = (level / (float) scale)*100; //Fin Recolección de datos para bateria
        buttonSTART=(Button)findViewById(R.id.buttonIniciar);
        buttonSTART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionarBotonIniciar(v);
            }
        });
        buttonSTOP=(Button)findViewById(R.id.buttonSTOP);
        buttonSTOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                presionarBotonDetener(v);
            } catch (InterruptedException e) {e.printStackTrace();} }
        });
        mSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE); //Activando Servicio
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //ActivacionServicio de GPS
        acelerometro=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//Llamando Sensor
        giroscopio=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometro=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        proximidad=mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        cheAce=(CheckBox)findViewById(R.id.answer1);
        cheGir=(CheckBox)findViewById(R.id.answer2);
        cheMag=(CheckBox)findViewById(R.id.answer3);
        chePro=(CheckBox)findViewById(R.id.answer4);
        cheGps=(CheckBox)findViewById(R.id.answer5);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            cheGps.setVisibility(View.GONE);
            Toast.makeText(Seleccion.this, "No es posible registrar GPS", Toast.LENGTH_SHORT).show(); //Imprime Toast indicando ya se genero el archivo
        }
        else
        {
            cheGps.setVisibility(View.VISIBLE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            buttonSTOP.setEnabled(false);
            buttonSTART.setEnabled(false);
            cheAce.setEnabled(false);
            cheGir.setEnabled(false);
            cheGps.setEnabled(false);
            cheMag.setEnabled(false);
            chePro.setEnabled(false);
            valSeg.setEnabled(false);
            Toast.makeText(Seleccion.this, "No es posible generar archivo por permisos", Toast.LENGTH_SHORT).show(); //Imprime Toast indicando ya se genero el archivo
        }
        else
        {
            buttonSTOP.setEnabled(true);
            buttonSTART.setEnabled(true);
            cheAce.setEnabled(true);
            cheGir.setEnabled(true);
            cheGps.setEnabled(true);
            cheMag.setEnabled(true);
            chePro.setEnabled(true);
            valSeg.setEnabled(true);
        }
        buttonSTOP.setEnabled(false);
    }
    public void activacionDeGps(){
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
    public void updateLocationInfo(Location location) {
        latitud= (float) location.getLatitude();
        longitud= (float) location.getLongitude();
    }
    private SensorEventListener acelSensorListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == acelerometro & cheAce.isChecked()==true ) {
                float values[] = event.values;
                curX = event.values[0];
                curY = event.values[1];
                curZ = event.values[2];
            }
        }
    };
    private SensorEventListener giroSensorListener2 = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == giroscopio & cheGir.isChecked()==true ) {
                gyroX = event.values[0];
                gyroY = event.values[1];
                gyroZ = event.values[2];
            }
        }
    };
    private SensorEventListener magnSensorListener3 = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == magnetometro & cheMag.isChecked()==true ) {
                magX = event.values[0];
                magY = event.values[1];
                magZ = event.values[2];
            }
        }
    };
    private SensorEventListener proxSensorListener4 = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == proximidad) {
                pro = event.values[0];
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        if (acelerometro != null) {
            mSensorManager.registerListener(acelSensorListener, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (giroscopio != null) {
            mSensorManager.registerListener(giroSensorListener2, giroscopio, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometro != null) {
            mSensorManager.registerListener(magnSensorListener3, magnetometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximidad != null) {
            mSensorManager.registerListener(proxSensorListener4, proximidad, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(acelSensorListener);
        mSensorManager.unregisterListener(giroSensorListener2);
        mSensorManager.unregisterListener(magnSensorListener3);
        mSensorManager.unregisterListener(proxSensorListener4);
    }
    public void presionarBotonIniciar(View v) {
        if(cheAce.isChecked()==true || cheGir.isChecked()==true || cheMag.isChecked()==true || chePro.isChecked()==true || cheGps.isChecked()==true) {
            if(valSeg.getText().toString().trim().length()!=0.0 ){
                if(cheGps.isChecked()==true){
                    activacionDeGps();
                }
                double numer = Double.valueOf(valSeg.getText().toString()); //Convierte el texto insertado en texto de intervalo a seg.
                Toast.makeText(Seleccion.this, "Inicio de lecturas", Toast.LENGTH_SHORT).show();
                lectorDeSensores = new LectorDeSensores2(numer);
                lectorDeSensores.iniciar(); //Llama al metodo
                buttonSTOP.setEnabled(true);
                buttonSTART.setEnabled(false);
                cheAce.setEnabled(false);
                cheGir.setEnabled(false);
                cheGps.setEnabled(false);
                cheMag.setEnabled(false);
                valSeg.setEnabled(false);
                chePro.setEnabled(false);
            }else{
                Toast.makeText(Seleccion.this, "Por favor ingrese intervalo de lectura", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Seleccion.this, "Por favor seleccion sensor a registrar", Toast.LENGTH_SHORT).show();
        }
    }
    public void presionarBotonDetener(View v) throws InterruptedException {
        buttonSTART.setEnabled(true);
        buttonSTOP.setEnabled(false);
        cheAce.setEnabled(true);
        cheGir.setEnabled(true);
        cheGps.setEnabled(true);
        cheMag.setEnabled(true);
        chePro.setEnabled(true);
        valSeg.setEnabled(true);
        onPause();
        lectorDeSensores.detener(); //LLama al metodo que detiene el hilo
        lectorDeSensores.join();
        textoRuta= lectorDeSensores.file.getPath();
        ((TextView) findViewById(R.id.editText)).setText("Guardado en:"+textoRuta);
    }
}
class LectorDeSensores2 extends Thread {
    File file;
    String encabezado, encabezadoAce,encabezadoGir,encabezadoMag,encabezadoPro,encabezadoGps;
    private Boolean continuar;
    private double intervaloEnSegundos; //Toma los Segundos
    private String sep = ";", sep2="%"; //Separadores para archivo
    public LectorDeSensores2(double numer) { //Toma el los segundos de la var. numer para el intervaloenSegundos y pide NO continuar
        this.intervaloEnSegundos = numer;
        continuar = false;
    }
    public void iniciar() {//Inicia la lectura
        continuar = true;
        start();
    }
    public void detener() {//Termina para generar archivo
        continuar = false;
    }
    SimpleDateFormat hora=new SimpleDateFormat("yyyyMMdd HHmmss");
    String horaActual = hora.format(new Date());
    @Override
    public void run() {
        String nombre = "Sensores "+horaActual+".csv";
        ArrayList<String> lineasDelArchivo = new ArrayList<String>();
        while (continuar) {
            lineasDelArchivo.add(leeHora()+leeAce()+leeGiro()+leeMag()+leeProx()+leeGps()+leeBateria());
            try {
                Thread.sleep((long) (1000 * this.intervaloEnSegundos));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            guardarArchivo(nombre, lineasDelArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void guardarArchivo(String nombre, ArrayList<String> lineas) throws IOException {
        file=new File(Environment.getExternalStorageDirectory(),nombre);//AlmacenamientoInterno para archivos
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        encabezado = "Hora"+sep+encabezadoAce+encabezadoGir+encabezadoMag+encabezadoPro+encabezadoGps+"Bateria";
        bufferedWriter.write(encabezado + System.getProperty("line.separator"));
        for (String linea : lineas) {
            bufferedWriter.write(linea + System.getProperty("line.separator"));
        }
        bufferedWriter.close();
    }
    private String leeHora() {
        SimpleDateFormat hora=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String horaActual = hora.format(new Date());
        return horaActual+sep;
    }
    private String leeAce() {
        Float AcelerometroX = Seleccion.curX;
        Float AcelerometroY = Seleccion.curY;
        Float AcelerometroZ = Seleccion.curZ;
        if (cheAce.isChecked()==true){
            encabezadoAce="Acelerometro X"+sep+"Acelerometro Y"+sep+"Acelerometro Z"+sep;
        }else{encabezadoAce="";}
        if (cheAce.isChecked() == true) {
            String acelerometro= AcelerometroX+sep+AcelerometroY+sep+AcelerometroZ+sep;
            return acelerometro;
        }
        return "";
    }
    private String leeGiro() {
        Float GiroscopioX = Seleccion.gyroX;
        Float GiroscopioY = Seleccion.gyroY;
        Float GiroscopioZ = Seleccion.gyroZ;
        if (cheGir.isChecked()==true){
            encabezadoGir="Giroscopio X" + sep + "Giroscopio Y" + sep + "Giroscopio Z"+sep;
        }else{encabezadoGir="";}
        if (cheGir.isChecked() == true) {
            String giroscopio = GiroscopioX + sep + GiroscopioY + sep + GiroscopioZ+sep;
            return giroscopio;
        }
        return "";
    }
    private String leeProx() {
        Float Proximidad0 = Seleccion.pro;
        if (chePro.isChecked()==true){
            encabezadoPro="Proximidad" + sep;
        }else{encabezadoPro="";}
        if (chePro.isChecked() == true) {
            String proximidad= Proximidad0+sep;
            return proximidad;
        }
        return "";
    }
    private String leeMag() {
        Float MagnetometroX = Seleccion.magX;
        Float MagnetometroY = Seleccion.magY;
        Float MagnetometroZ = Seleccion.magZ;
        if (cheMag.isChecked()==true){
            encabezadoMag="Magnetometro X" +sep+"Magnetometro Y"+sep+"Magnetometro Z"+sep;
        }else{encabezadoMag="";}
        if (cheMag.isChecked() == true) {
            String magnetometro = MagnetometroX+sep+MagnetometroY+sep+MagnetometroZ+sep;
            return magnetometro;
        }
        return "";
    }
    private String leeGps() {
        Float GPSX = Seleccion.latitud;
        Float GPSY = Seleccion.longitud;
        if (cheGps.isChecked()==true){
            encabezadoGps="Latitud" +sep+"Longitud"+sep;
        }else{encabezadoGps="";}
        if (cheGps.isChecked() == true) {
            String gps= GPSX+sep+GPSY+sep;
            return gps;
        }
        return "";
    }
    private String leeBateria() {
        Double Bateria = Seleccion.battery;
        String bateria =  Bateria+sep2;
        return bateria;
    }
}