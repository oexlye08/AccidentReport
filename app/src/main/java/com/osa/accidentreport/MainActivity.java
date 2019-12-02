package com.osa.accidentreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted, View.OnClickListener {

    private static final int REQUEST_LOCATION_PERMISSION=1;
    private Button mLocationButton, mSubmitButton, mShowButton;
    private TextView mLocationTextVIew;
    private EditText mEditNama, mEditKendaraan, mEditKeterangan;
    FusedLocationProviderClient mfusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INISIALISASI
        mLocationButton=findViewById(R.id.buttonLokassi);
        mSubmitButton=findViewById(R.id.buttonSubmit);
        mShowButton=findViewById(R.id.buttonTampilkan);
        mEditNama=findViewById(R.id.editNama);
        mEditKendaraan=findViewById(R.id.editKendaraan);
        mEditKeterangan=findViewById(R.id.editKeterangan);
        mLocationTextVIew=findViewById(R.id.textLokasi);
        mfusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        //Setting listeners to button
    mSubmitButton.setOnClickListener(this);
    mShowButton.setOnClickListener(this);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);
        }else {
            mfusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null) {
                        mLocationTextVIew.setText("Latitude" + location.getLatitude()
                                + "\n Longitude" + location.getLongitude());
                    }
                }
            });
        }


        /**Update Lokasi contoh : GOJEK**/
        mfusedLocationClient.requestLocationUpdates(getLocationRequst(),
                new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult){
                        if(locationResult.getLastLocation()!=null){
                            Location location=locationResult.getLastLocation();
                   /* mLocationTextVIew.setText(
                            "Latitude: "+ location.getLatitude()+
                                    "\n Longitude"+location.getLongitude()
                    );*/

                            new FetchAddressTask(MainActivity.this, MainActivity.this)
                                    .execute(location);
                        }
                    }
                }, null
        );
    }

    private LocationRequest getLocationRequst(){
        LocationRequest locationRequest= new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        mLocationTextVIew.setText(result);
    }

    //Dibawah ini merupakan perintah untuk Menambahkan (CREATE)
    private void addEmployee(){

        final String name= mEditNama.getText().toString().trim();
        final String vehicle = mEditKendaraan.getText().toString().trim();
        final String note = mEditKeterangan.getText().toString().trim();
        final String location = mLocationTextVIew.getText().toString().trim();

        class AddEmployee extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_EMP_NAMA,name);
                params.put(Konfigurasi.KEY_EMP_VEHICLE, vehicle);
                params.put(Konfigurasi.KEY_EMP_NOTE,note);
                params.put(Konfigurasi. KEY_EMP_LOCATION, location);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Konfigurasi.URL_ADD, params);
                return res;
            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == mSubmitButton){
            addEmployee();
        }

        if(v == mShowButton){
            startActivity(new Intent(this,tampilSemuaLaporan.class));
        }

    }
}
